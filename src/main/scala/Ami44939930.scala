package ohnosequences.statika

// Amazon Linux AMI, EBS-Backed 64-bit, EU Ireland

import shapeless._
import ohnosequences.statika.General._

object Ami44939930_2013_03 extends AmiBundle(
    id = "ami-44939930",
    amiVersion = "2013.03",
    version = "0.1.0-SNAPSHOT"
  )

object Ami44939930App extends App { installWithDeps(Ami44939930_2013_03) }
