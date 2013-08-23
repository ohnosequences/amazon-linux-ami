package ohnosequences.statika.ami.tests

// For test purposes only!

import ohnosequences.statika._
import ami._

// a distribution containing only this bundle
case object DummyDistribution extends Distribution(
  ami = AMI44939930
, members = AmazonLinuxAMIBundle :: HNil
) {
  val metadata = generated.metadata.DummyDistribution

  def install[D <: DistributionAux](distribution: D): InstallResults = success(metadata.name+" is installed")
}
