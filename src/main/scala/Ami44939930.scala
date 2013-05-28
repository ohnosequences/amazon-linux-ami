package ohnosequences.statika.Ami44939930_2013_03

// Amazon Linux AMI, EBS-Backed 64-bit, EU Ireland

import shapeless._
import ohnosequences.statika.General._
import ohnosequences.statika.MetaData._
import ohnosequences.statika.Ami._

object Ami44939930_2013_03 extends AmiBundle("ami-44939930", "2013.03") {

  import MetaData._

  def userScript[ B <: BundleAux : DependsOn[this.type]#λ ]( b: B )
    (implicit md: MetaDataOf[b.type]): String = {
s"""#!/bin/sh

# redirecting output for logging
exec &> /root/log.txt

echo
echo " -- Setting environment -- "
echo
cd /root
export HOME="/root"
export PATH="/root/bin:$$PATH"
env

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
s3cmd --config /root/.s3cfg get s3://private.snapshots.statika.ohnosequences.com/credentials/AwsCredentials.properties

echo
echo " -- Installing sbt -- "
echo
curl http://scalasbt.artifactoryonline.com/scalasbt/sbt-native-packages/org/scala-sbt/sbt/0.12.3/sbt.rpm > sbt.rpm
yum install sbt.rpm -y 

echo
echo " -- Installing conscript -- "
echo
curl https://raw.github.com/n8han/conscript/master/setup.sh | sh
# cp /root/bin/cs /bin/cs
# chmod a+x /bin/cs
# cs --setup

echo
echo " -- Installing giter8 -- "
echo
cs n8han/giter8
# cp /root/bin/g8 /bin/g8
# chmod a+x /bin/g8

echo
echo " -- Running g8 -- "
echo
g8 ohnosequences/statika-bundle.g8 -b feature/bundle-tester '--name=BundleTester' '--ami=Ami44939930_2013_03' '--class_name=${b.name}' '--bundle_version=${md.version}' '--artifact_name=${md.artifact}' '--credentials=/root/AwsCredentials.properties'
cd bundletester

echo
echo " -- Building ${b.name} -- "
echo
sbt start-script

echo
echo " -- Running ${b.name} -- "
echo
target/start
"""
  }
}

object Ami44939930App extends App { installWithDeps(Ami44939930_2013_03) }
