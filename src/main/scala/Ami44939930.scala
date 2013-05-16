package ohnosequences.statika.Ami44939930_2013_03

// Amazon Linux AMI, EBS-Backed 64-bit, EU Ireland

import shapeless._
import ohnosequences.statika.General._
import ohnosequences.statika.AmiBundle

object Ami44939930_2013_03 extends AmiBundle(
    id = "ami-44939930",
    amiVersion = "2013.03",
    version = "0.1.0-SNAPSHOT"
  ) {

  def userScript[B <: BundleAux](b: B, artifact: String)
      (implicit s: Selector[b.Deps, this.type]): String =
s"""#!/bin/sh

# redirecting output for logging
exec &> /root/log.txt

cd /root

# intalling s3cmd
yum install git -y
git clone https://github.com/s3tools/s3cmd.git
cd s3cmd/
python setup.py install
cd /root

# empty config, s3cmd will use IAM role
echo [default] > /root/.s3cfg
# getting credentials
s3cmd get s3://private.snapshots.statika.ohnosequences.com/credentials/AwsCredentials.properties

# sbt
curl http://scalasbt.artifactoryonline.com/scalasbt/sbt-native-packages/org/scala-sbt/sbt/0.12.3/sbt.rpm > sbt.rpm
yum install sbt.rpm -y 

# conscript
curl https://raw.github.com/n8han/conscript/master/setup.sh | sh
cs --setup

# giter8
cs n8han/giter8
cp /root/bin/g8 /bin/g8
chmod a+x /bin/g8

# running g8
g8 ohnosequences/statika-bundle.g8 -b feature/bundle-tester '--name=BundleTester' '--ami=Ami44939930_2013_03' '--class_name=${b.name}' '--bundle_version=${b.version}' '--artifact_name=${artifact}' '--credentials=/root/AwsCredentials.properties'
cd bundletester
sbt run &> /root/sbt-run.out
"""
}

object Ami44939930App extends App { installWithDeps(Ami44939930_2013_03) }

object Ami44939930Tester extends Bundle(
    name = "Ami44939930Tester"
  , version = Ami44939930_2013_03.version
  , dependencies = Ami44939930_2013_03 :: HNil)
