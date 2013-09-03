package ohnosequences.statika.ami

// Abstract library and a statika bundle for ami-44939930

import ohnosequences.statika._

case object AMI44939930 extends AbstractAMI("ami-44939930", "2013.03"){

  def userScript[
      D <: DistributionAux
    , B <: BundleAux : distribution.IsMember
    ](distribution: D
    , bundle: B
    , credentials: Credentials = RoleCredentials
    ): String = {

      val mb = bundle.metadata
      val md = distribution.metadata

  val initSetting =
"""#!/bin/sh

# redirecting output for logging
exec &> /log.txt

echo "tail -f /log.txt" > /bin/show-log
chmod a+r /log.txt
chmod a+x /bin/show-log

echo
echo " -- Setting environment -- "
echo
cd /root
export HOME="/root"
export PATH="/root/bin:/opt/aws/bin:$PATH"
export ec2id=$(GET http://169.254.169.254/latest/meta-data/instance-id)
export EC2_HOME=/opt/aws/apitools/ec2
export JAVA_HOME=/usr/lib/jvm/jre
env

echo
echo " -- Tagging instance -- "
echo
ec2-create-tags  $ec2id  --region eu-west-1  --tag statika-status=userscript
"""

  val credentialsSet = credentials match {
      case InBucket(bucket) => """
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
      case _ => ""
  }

  val sbt = """
echo
echo " -- Installing sbt -- "
echo
ec2-create-tags  $ec2id  --region eu-west-1  --tag statika-status=sbt
curl http://scalasbt.artifactoryonline.com/scalasbt/sbt-native-packages/org/scala-sbt/sbt/0.12.3/sbt.rpm > sbt.rpm
yum install sbt.rpm -y 
"""

  val tagCode = """    Seq(\"ec2-create-tags\", Seq(\"GET\", \"http://169.254.169.254/latest/meta-data/instance-id\").!!.replaceAll(\"\\n\",\"\"), \"--region\", \"eu-west-1\", \"--tag\", \"statika-status=%s\").!"""

  val building = """
echo
echo " -- Building Applicator -- "
echo
ec2-create-tags  $ec2id  --region eu-west-1  --tag statika-status=building
mkdir applicator
cd applicator
sbt 'set name := "applicator"' \
  'set scalaVersion := "2.10.2"' \
  'session save' \
  'reload plugins' \
  'set resolvers += "Era7 releases" at "http://releases.era7.com.s3.amazonaws.com"' \
  'set resolvers += Resolver.url("Era7 Releases", url("http://releases.era7.com.s3.amazonaws.com"))(Resolver.ivyStylePatterns)' \
  'set addSbtPlugin("ohnosequences" %% "sbt-s3-resolver" %% "0.5.2")' \
  'set addSbtPlugin("com.typesafe.sbt" %% "sbt-start-script" %% "0.8.0")' \
  'session save' \
  'reload return' \
  '%s' \
  'set resolvers ++= %s' \
  'set resolvers <++= s3credentials { cs => (%s map { r: S3Resolver => { cs map r.toSbtResolver } }).flatten }' \
  'set libraryDependencies ++= Seq ("ohnosequences" %%%% "statika" %% "%s", %s)' \
  'set sourceGenerators in Compile <+= sourceManaged in Compile map { dir => val file = dir / "apply.scala"; IO.write(file, "%s"); Seq(file) }' \
  'session save' \
  'add-start-script-tasks' \
  'start-script'
""" format (
    credentials match {
      case NoCredentials =>     "set s3credentials in Global := None"
      case RoleCredentials =>   """set s3credentials in Global := Some(("", ""))"""
      case Explicit(usr,psw) => """set s3credentials in Global := Some(("%s", "%s"))""" format (usr, psw)
      case _ => """set s3credentialsFile in Global := Some("/root/AwsCredentials.properties")"""
    }
  , md.resolvers
  , md.privateResolvers
  , md.statikaVersion
  , { val mds = md.toString
      val mbs = mb.toString
      if (mds == mbs) mds else mds+", "+mbs
    }
  , Seq(
      "import sys.process._;"
    , "object apply extends App {"
    , "  val results = %s.installWithDeps(%s);"
    , "  results foreach println;"
    , "  if (results exists (_.isLeft)) { "
    , tagCode format "failure"
    , "  } else {"
    , tagCode format "success"
    , "  }"
    , "}"
    ).mkString format (md.name, mb.name)
  )

  val running = """
echo
echo " -- Running -- "
echo
ec2-create-tags  $ec2id  --region eu-west-1  --tag statika-status=running
target/start
"""

    initSetting + credentialsSet + sbt +
    building + running
  }

}

case object AmazonLinuxAMIBundle extends Bundle() {

  val metadata = generated.metadata.AmazonLinuxAMIBundle

  val ami = AMI44939930

  def install[D <: DistributionAux](distribution: D): InstallResults = ami.checkID

}
