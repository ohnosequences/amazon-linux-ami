package ohnosequences.statika.ami

import ohnosequences.statika._
import ohnosequences.statika.aws._


case object AMI44939930 extends AmazonLinuxAMI("ami-44939930", "2013.03")


/*  Abtract class `AmazonLinuxAMI` provides parts of the user script as it's members, so that 
    one can extend it and redefine behaviour, of some part, reusing others.

    Note the `withTags` value, using it you can turn off status tags for the instance.
*/
abstract class AmazonLinuxAMI(id: String, amiVersion: String) 
          extends AbstractAMI(id,         amiVersion) {

  
  /*  First of all, `initSetting` part sets up logging.
      Then it sets useful environment variables.
  */  
  def initSetting = """
    |
    |# redirecting output for logging
    |exec &> /log.txt
    |
    |echo "tail -f /log.txt" > /bin/show-log
    |chmod a+r /log.txt
    |chmod a+x /bin/show-log
    |ln -s /log.txt /root/log.txt
    |
    |echo
    |echo " -- Setting environment -- "
    |echo
    |cd /root
    |export HOME="/root"
    |export PATH="/root/bin:/opt/aws/bin:$PATH"
    |export ec2id=$(GET http://169.254.169.254/latest/meta-data/instance-id)
    |export EC2_HOME=/opt/aws/apitools/ec2
    |export JAVA_HOME=/usr/lib/jvm/jre
    |env
    |""".stripMargin

  
  /* Installing sbt-0.13.0 using rpm. */
  def sbtInstalling = """
    |echo
    |echo " -- Installing sbt -- "
    |echo
    |curl http://scalasbt.artifactoryonline.com/scalasbt/sbt-native-packages/org/scala-sbt/sbt/0.13.0/sbt.rpm > sbt.rpm
    |yum install sbt.rpm -y 
    |""".stripMargin


  /*  This part sets up credentials if they are in an S3 bucket.
      It installs git, then installs s3cmd, then downloads credentials. 
  */  
  def credsSetting(creds: AWSCredentials) = creds match {
    // this way of setting credentials should be deprecated!
    case InBucket(bucket) => """
      |echo
      |echo " -- Installing git -- "
      |echo
      |yum install git -y
      |
      |echo
      |echo " -- Installing s3cmd -- "
      |echo
      |git clone https://github.com/s3tools/s3cmd.git
      |cd s3cmd/
      |python setup.py install
      |cd /root
      |
      |echo
      |echo " -- Creating empty s3cmd config, it will use IAM role -- "
      |echo "[default]" > /root/.s3cfg
      |cat /root/.s3cfg
      |
      |echo
      |echo " -- Getting credentials -- "
      |echo
      |s3cmd --config /root/.s3cfg get %s
      |""".stripMargin format bucket
    case _ => ""
  }

  
  /* This is the main part of the script: building applicator project. */
  def building[M <: MetadataBound]
    (md: M, distName: String, bundleName: String, creds: AWSCredentials = RoleCredentials): String = """
    |echo
    |echo " -- Building Applicator -- "
    |echo
    |mkdir applicator
    |cd applicator
    |sbt 'set name := "applicator"' \
    |  'set scalaVersion := "2.10.2"' \
    |  'session save' \
    |  'reload plugins' \
    |  'set resolvers += "Era7 releases" at "http://releases.era7.com.s3.amazonaws.com"' \
    |  'set addSbtPlugin("ohnosequences" %% "sbt-s3-resolver" %% "0.6.0")' \
    |  'set addSbtPlugin("com.typesafe.sbt" %% "sbt-start-script" %% "0.10.0")' \
    |  'session save' \
    |  'reload return' \
    |  'set %s' \
    |  'set resolvers ++= %s' \
    |  'set resolvers <++= s3credentials { cs => (%s map { r: S3Resolver => { cs map r.toSbtResolver } }).flatten }' \
    |  'set libraryDependencies ++= Seq (%s)' \
    |  'set sourceGenerators in Compile <+= sourceManaged in Compile map { dir => val file = dir / "apply.scala"; IO.write(file, "%s"); Seq(file) }' \
    |  'session save' \
    |  'add-start-script-tasks' \
    |  'start-script'
    |""".stripMargin format (
        creds match {
          case NoCredentials     => """s3credentials := None"""
          case RoleCredentials   => """s3credentials := Some(("", ""))"""
          case Explicit(usr,psw) => """s3credentials := Some(("%s", "%s"))""" format (usr, psw)
          case _                 => """s3credentialsFile := Some("/root/AwsCredentials.properties")"""
        }
      , md.resolvers
      , md.privateResolvers
      , md.moduleID
      , "object apply extends App { %s.installWithDeps(%s) foreach println }" format 
          (distName, bundleName)
      )

  /* Just running the applicator project (using sbt-start-script). */
  def applying = """
    |echo
    |echo " -- Running -- "
    |echo
    |target/start
    |""".stripMargin


  /* Instance status-tagging is optional. */
  def withTags: Boolean = true

  def tag(state: String) = if (!withTags) ""
    else "ec2-create-tags  $ec2id  --region eu-west-1  --tag statika-status=" + state


  /* Combining all parts to one script. */
  type MetadataBound = SbtMetadata

  override def userScript[M <: MetadataBound]
    (md: M, distName: String, bundleName: String, creds: AWSCredentials = RoleCredentials): String = {

    "#!/bin/sh \n"   + initSetting + 
    tag("preparing") + sbtInstalling + credsSetting(creds) +
    tag("building")  + building(md, distName, bundleName, creds) + 
    tag("applying")  + applying +
    { if (!withTags) "" else "[ $? ] && " + tag("success") + " || " + tag("failure") }

  }

}
