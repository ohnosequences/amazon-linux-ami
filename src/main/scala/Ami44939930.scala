package ohnosequences.statika.ami

import ohnosequences.statika._
import ohnosequences.statika.aws._

@deprecated("This is old Amazon Linux AMI with slow user script, please, use AMI149f7863 instead", "v0.12.0")
case object AMI44939930 extends AmazonLinuxAMI("ami-44939930", "2013.03") {
  
  type MetadataBound = SbtMetadata


  /* Installing sbt-0.13.0 using rpm. */
  def sbtInstalling = """
    |curl http://scalasbt.artifactoryonline.com/scalasbt/sbt-native-packages/org/scala-sbt/sbt/0.13.0/sbt.rpm > sbt.rpm
    |yum install sbt.rpm -y 
    |""".stripMargin

  /*  This part sets up credentials if they are in an S3 bucket.
      It installs git, then installs s3cmd, then downloads credentials. 
  */  
  def credsSetting(creds: AWSCredentials) = creds match {
    // this way of setting credentials should be deprecated!
    case InBucket(bucket) => """
      |yum install git -y
      |
      |git clone https://github.com/s3tools/s3cmd.git
      |cd s3cmd/
      |python setup.py install
      |cd /root
      |
      |echo " -- Creating empty s3cmd config, it will use IAM role -- "
      |echo "[default]" > /root/.s3cfg
      |cat /root/.s3cfg
      |
      |s3cmd --config /root/.s3cfg get %s
      |""".stripMargin format bucket
    case _ => ""
  }

  def preparing(creds: AWSCredentials) = sbtInstalling + credsSetting(creds)
  
  def building[M <: MetadataBound](md: M
  , distName: String
  , bundleName: String
  , creds: AWSCredentials = RoleCredentials
  ): String = {"""
    |mkdir applicator
    |cd applicator
    |sbt 'set name := "applicator"' \
    |  'set scalaVersion := "2.10.3"' \
    |  'session save' \
    |  'reload plugins' \
    |  'set resolvers += "Era7 releases" at "http://releases.era7.com.s3.amazonaws.com"' \
    |  'set addSbtPlugin("ohnosequences" % "sbt-s3-resolver" % "0.7.0")' \
    |  'set addSbtPlugin("com.typesafe.sbt" % "sbt-start-script" % "0.10.0")' \
    |  'session save' \
    |  'reload return' \
    |  'set $credentials$' \
    |  'set resolvers ++= $resolvers$' \
    |  'set resolvers ++= { $privateResolvers$ map { r: S3Resolver => s3credentials.value map r.toSbtResolver } flatten }' \
    |  'set libraryDependencies ++= Seq ($artifact$)' \
    |  'set sourceGenerators in Compile <+= sourceManaged in Compile map { dir => val file = dir / "apply.scala"; IO.write(file, "$code$"); Seq(file) }' \
    |  'session save' \
    |  'add-start-script-tasks' \
    |  'start-script'
    |""".stripMargin.
      replace("$credentials$", creds match {
          case NoCredentials     => """s3credentials := None"""
          case RoleCredentials   => """s3credentials := Some(("", ""))"""
          case Explicit(usr,psw) => """s3credentials := Some(("%s", "%s"))""" format (usr, psw)
          case _                 => """s3credentialsFile := Some("/root/AwsCredentials.properties")"""
        }).
      replace("$resolvers$", md.resolvers.toString).
      replace("$privateResolvers$", md.privateResolvers.toString).
      replace("$artifact$", md.moduleID).
      replace("$code$", Seq(
          "object apply extends App {"
        , "  val results = %s.installWithDeps(%s);"
        , "  results foreach println;"
        , "  if (results.hasFailures) sys.error(results.toString)"
        , "}"
        ).mkString.format(distName, bundleName))
    }

  def applying = """
    |target/start
    |""".stripMargin

  def tag(state: String) = """
    |echo
    |echo " -- $state$ -- "
    |echo
    |ec2-create-tags  $ec2id  --region eu-west-1  --tag statika-status=$state$
    |""".stripMargin.replace("$state$", state)
}
