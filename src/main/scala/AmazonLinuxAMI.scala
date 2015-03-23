package ohnosequences.statika.ami

import ohnosequences.statika._
import ohnosequences.statika.aws._
import ohnosequences.awstools.regions.Region

sealed trait Arch
case object Arch32 extends Arch { override def toString = "32" }
case object Arch64 extends Arch { override def toString = "64" }

/*  Abtract class `AmazonLinuxAMI` provides parts of the user script as it's members, so that 
    one can extend it and redefine behaviour, of some part, reusing others.
*/
abstract class AmazonLinuxAMI(
    id: String
  , amiVersion: String
  ) extends AMI(id, amiVersion) {

  val region: Region
  val arch: Arch
  val javaHeap: Int // in G
  val workingDir: String
  
  /*  First of all, `initSetting` part sets up logging.
      Then it sets useful environment variables.
  */  
  def initSetting: String = """
    |
    |# redirecting output for logging
    |exec &> /log.txt
    |
    |echo "tail -f /log.txt" > /bin/show-log
    |chmod a+r /log.txt
    |chmod a+x /bin/show-log
    |ln -s /log.txt /root/log.txt
    |
    |function tagStep(){
    |  if [ $1 = 0 ]; then
    |    $tagOk$
    |  else
    |    $tagFail$
    |  fi
    |}
    |
    |cd /root
    |export HOME="/root"
    |export PATH="/root/bin:/opt/aws/bin:$PATH"
    |export ec2id=$(GET http://169.254.169.254/latest/meta-data/instance-id)
    |export EC2_HOME=/opt/aws/apitools/ec2
    |export AWS_DEFAULT_REGION=$region$
    |""".stripMargin.
      replace("$region$", region.toString).
      replace("$tagOk$", tag("$2")).
      replace("$tagFail$", tag("failure"))

  /*  This part should make any necessary for building preparations, 
      like installing build tools: java-7 and scala-2.11.6 from rpm's
  */
  def preparing: String = """
    |aws s3 cp s3://resources.ohnosequences.com/java7-oracle.rpm java7-oracle.rpm
    |aws s3 cp s3://resources.ohnosequences.com/scala-2.11.6.rpm scala-2.11.6.rpm
    |yum install -y java7-oracle.rpm scala-2.11.6.rpm
    |alternatives --install /usr/bin/java java /usr/java/default/bin/java 99999
    |alternatives --auto java
    |""".stripMargin

  /* This is the main part of the script: building applicator. */
  def building(
      artifactUrl: String,
      distName: String,
      bundleName: String
    ): String = s"""
    |mkdir -p ${workingDir}
    |cd ${workingDir}
    |
    |echo "object apply extends App { " > apply.scala
    |echo "  val results = ${distName}.installWithDeps(${bundleName}); " >> apply.scala
    |echo "  results foreach println; " >> apply.scala
    |echo "  if (results.hasFailures) sys.error(results.toString) " >> apply.scala
    |echo "}" >> apply.scala
    |cat apply.scala
    |
    |aws s3 cp ${artifactUrl} dist.jar
    |
    |scalac -cp dist.jar apply.scala
    |""".stripMargin

  /* Just running what we built. */
  def applying: String = s"""
    |java -d${arch} -Xmx${javaHeap}G -cp .:dist.jar apply
    |""".stripMargin

  /* Instance status-tagging. */
  def tag(state: String): String = s"""
    |echo
    |echo " -- ${state} -- "
    |echo
    |aws ec2 create-tags --resources $$ec2id  --tag Key=statika-status,Value=${state} > /dev/null
    |""".stripMargin

  // checks exit code of the previous step
  def tagStep(state: String) = """
    |tagStep $? $state$
    |""".stripMargin.replace("$state$", state)

  def fixLineEndings(s: String): String = s.replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n")

  /* Combining all parts to one script. */
  def userScript(
      artifactUrl: String,
      distName: String,
      bundleName: String
    ): String = fixLineEndings(
        "#!/bin/sh \n"       + initSetting + 
        tagStep("preparing") + preparing +
        tagStep("building")  + building(artifactUrl, distName, bundleName) + 
        tagStep("applying")  + applying +
        tagStep("success")
      )

}
