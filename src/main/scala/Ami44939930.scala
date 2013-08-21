package ohnosequences.statika.ami

// Abstract library and a statika bundle for ami-44939930

import ohnosequences.statika._

case object AMI44939930 extends AbstractAMI("ami-44939930", "2013.03"){

  def userScript[
      D <: DistributionAux
    , B <: BundleAux : distribution.IsMember
    ](distribution: D
    , bundle: B
    , credentials: Either[(String, String), String]
    ): String = {

      val mb = bundle.metadata
      val md = distribution.metadata

  val initSetting =
"""#!/bin/sh

# redirecting output for logging
exec &> /root/log.txt

echo
echo " -- Setting environment -- "
echo
cd /root
export HOME="/root"
export PATH="/root/bin:$PATH"
env
"""

  val credentialsSet = credentials match {
      case Left((usr,psw)) => """
echo "accessKey = %s" >  /root/AwsCredentials.properties
echo "secretKey = %s" >> /root/AwsCredentials.properties
      """ format (usr, psw)
      case Right(bucket) => """
echo
echo " -- Installing git -- "
echo
yum install git -y

echo
echo " -- Installing s3cmd -- "
echo
git clone https://github.com/s3tools/s3cmd.git
cd s3cmd/
python setup.py install
cd /root

echo
echo " -- Creating empty s3cmd config, it will use IAM role -- "
echo "[default]" > /root/.s3cfg
cat /root/.s3cfg

echo
echo " -- Getting credentials -- "
echo
s3cmd --config /root/.s3cfg get %s""" format bucket
  }

  val sbt = """
echo
echo " -- Installing sbt -- "
echo
curl http://scalasbt.artifactoryonline.com/scalasbt/sbt-native-packages/org/scala-sbt/sbt/0.12.3/sbt.rpm > sbt.rpm
yum install sbt.rpm -y 
"""

  def moduleID(m: MetaData): String = 
    "\"%s\" %%%% \"%s\" %% \"%s\"" format (m.organization, m.artifact, m.version)

  val building = """
echo
echo " -- Building Applicator -- "
echo
mkdir applicator
cd applicator
sbt 'set name := "applicator"' \
  'set scalaVersion := "2.10.2"' \
  'session save' \
  'reload plugins' \
  'set resolvers += "Era7 releases" at "http://releases.era7.com.s3.amazonaws.com"' \
  'set addSbtPlugin("ohnosequences" %% "sbt-s3-resolver" %% "0.5.0")' \
  'set addSbtPlugin("com.typesafe.sbt" %% "sbt-start-script" %% "0.8.0")' \
  'session save' \
  'reload return' \
  'set s3credentialsFile in Global := Some("/root/AwsCredentials.properties")' \
  'set resolvers ++= %s' \
  'set resolvers <++= s3credentials { cs => (%s map { r: S3Resolver => { cs map r.toSbtResolver } }).flatten }' \
  'set libraryDependencies ++= Seq ("ohnosequences" %%%% "statika" %% "%s", %s %s)' \
  'set sourceGenerators in Compile <+= sourceManaged in Compile map { dir => val file = dir / "apply.scala"; IO.write(file, "%s"); Seq(file) }' \
  'session save' \
  'add-start-script-tasks' \
  'start-script'
""" format (
    md.resolvers
  , md.privateResolvers
  , md.statikaVersion
  , moduleID(md)
  , if (moduleID(md) == moduleID(mb)) "" else ", "+moduleID(mb)
  , """object apply extends App {%s.installWithDeps(%s) map println}""" format (md.name, mb.name)
  )

  val running = """
echo
echo " -- Running -- "
echo
target/start
"""

    initSetting + credentialsSet + sbt +
    building + running
  }

}

case object AmazonLinuxAMIBundle extends Bundle() {

  val metadata = meta.AmazonLinuxAMIBundle

  val ami = AMI44939930

  override def install[D <: DistributionAux]
        (distribution: D): InstallResults = ami.checkID

}
