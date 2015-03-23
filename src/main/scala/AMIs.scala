package ohnosequences.statika.ami

import ohnosequences.statika._
import ohnosequences.statika.aws._
import ohnosequences.awstools.regions.Region

// Current AMIs (2014.09.2) with ephemeral storage and 64bit
// See http://aws.amazon.com/amazon-linux-ami/

object RegionMap {
  import Region._
  def amiId(region: Region): String = region match {
      case NorthernVirginia   => "ami-b0682cd8"
      case Oregon             => "ami-4bc29b7b"
      case NorthernCalifornia => "ami-dc908999"
      case Ireland            => "ami-693db01e"
      // case Frankfurt          => "ami-6201327f"
      case Singapore          => "ami-56ba9104"
      case Tokyo              => "ami-32879933"
      case Sydney             => "ami-19007423"
      case SaoPaulo           => "ami-c99925d4"
      // case Beijin             => "ami-881d8fb1"
      case GovCloud           => "ami-2f32530c"
    }
}

case class amzn_ami_pv_64bit(val region: Region)(
  val javaHeap: Int // in G
) extends AmazonLinuxAMI(
    id = RegionMap.amiId(reg),
    amiVersion = "2014.09.2",
) {

  val arch = Arch64
  val workingDir = "/media/ephemeral0/applicator"
}

// TODO: make it dependent on instance type and choose PV or HVM
// See http://aws.amazon.com/amazon-linux-ami/instance-type-matrix/
