package ohnosequences.statika.ami

import ohnosequences.statika._
import ohnosequences.statika.aws._

/*  Abtract class `AmazonLinuxAMI` provides parts of the user script as it's members, so that 
    one can extend it and redefine behaviour, of some part, reusing others.
*/
abstract class AmazonLinuxAMI[MB <: AnyMetadata](id: String, amiVersion: String) 
  extends AMI[MB](id, amiVersion) {

  def region: String = "eu-west-1"

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
    |cd /root
    |export HOME="/root"
    |export PATH="/root/bin:/opt/aws/bin:$PATH"
    |export ec2id=$(GET http://169.254.169.254/latest/meta-data/instance-id)
    |export EC2_HOME=/opt/aws/apitools/ec2
    |export JAVA_HOME=/usr/lib/jvm/jre
    |export AWS_DEFAULT_REGION=$region$
    |env
    |
    |function tagStep(){
    |  if [ $1 = 0 ]; then
    |    $tagOk$
    |  else
    |    $tagFail$
    |  fi
    |}
    |""".stripMargin.
      replace("$region$", region).
      replace("$tagOk$", tag("$2")).
      replace("$tagFail$", tag("failure"))

  /*  This part should make any necessary for building preparations, 
      like installing build tools and setting credentials, etc.
  */
  def preparing(creds: AWSCredentials): String

  /* This is the main part of the script: building applicator. */
  def building(
      md: MetadataBound
    , distName: String
    , bundleName: String
    , creds: AWSCredentials = RoleCredentials
    ): String

  /* Just running what we built. */
  def applying: String

  /* Instance status-tagging. */
  def tag(state: String): String

  // checks exit code of the previous step
  def tagStep(state: String) = """
    |tagStep $? $state$
    |""".stripMargin.replace("$state$", state)

  /* Combining all parts to one script. */
  def userScript(
      md: MetadataBound
    , distName: String
    , bundleName: String
    , creds: AWSCredentials = RoleCredentials
    ): String = {
      "#!/bin/sh \n"       + initSetting + 
      tagStep("preparing") + preparing(creds) +
      tagStep("building")  + building(md, distName, bundleName, creds) + 
      tagStep("applying")  + applying +
      tagStep("success")
  }

}
