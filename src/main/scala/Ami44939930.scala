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

  def userScript[D <: HList, B <: Bundle[D]](b: B, artifact: String)
      (implicit s: Selector[b.Deps, this.type]): String =
s"""#!/bin/sh
cd /root

# redirecting output for logging
exec &> /root/log.txt

# sbt
curl http://scalasbt.artifactoryonline.com/scalasbt/sbt-native-packages/org/scala-sbt/sbt/0.12.3/sbt.rpm > sbt.rpm
yum install -y sbt.rpm

# conscript
curl https://raw.github.com/n8han/conscript/master/setup.sh | sh
cs --setup

# giter8
cs n8han/giter8
cp /root/bin/g8 /bin/g8
chmod a+x /bin/g8

# running g8
g8 ohnosequences/statika-bundle.g8 -b feature/bundle-tester '--name=BundleTester' '--ami=Ami44939930_2013_03' '--class_name=${b.name}' '--bundle_version=${b.version}' '--artifact_name=${artifact}'
cd BundleTester
sbt run &> /root/sbt-run.out
"""
}

object Ami44939930App extends App { installWithDeps(Ami44939930_2013_03) }
