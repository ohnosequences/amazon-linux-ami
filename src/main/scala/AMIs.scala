package ohnosequences.statika.ami

import ohnosequences.statika._
import ohnosequences.statika.aws._

// Current AMIs (2013.09.2) with ephemeral storage and 65bit

object RegionMap {
  import AWSRegion._
  def amiId(region: AWSRegion): String = region match {
      case Tokyo              => "ami-f30c6ff2"
      case Singapore          => "ami-b8baeeea"
      case Sydney             => "ami-4fa83775"
      case Ireland            => "ami-5456b823"
      case SaoPaulo           => "ami-b99130a4"
      case NorthernVirginia   => "ami-1ba18d72"
      case NorthernCalifornia => "ami-883909cd"
      case Oregon             => "ami-8ef297be"
    }
}

case class amzn_ami_pv_64bit(reg: AWSRegion)(
  val javaHeap: Int,// in G
  val creds: AWSCredentials = RoleCredentials,
  val workingDir: String = "/media/ephemeral0/applicator"
  ) extends FatJarAmazonLinuxAMI(
    id = RegionMap.amiId(reg),
    amiVersion = "2013.09.2",
    region = reg,
    arch = Arch64
  )
