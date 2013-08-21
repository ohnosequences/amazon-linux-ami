package ohnosequences.statika.ami.tests

// For test purposes only!

import ohnosequences.statika._
import ami._

// a distribution containing only this bundle
case object DummyDistribution extends Distribution(
  ami = AMI44939930
, members = AmazonLinuxAMIBundle :: HNil
) {
  private val m = meta.AmazonLinuxAMIBundle

  val metadata = new MetaDataOf[this.type] {
    val name = "ohnosequences.statika.ami.tests.DummyDistribution"
    val organization = m.organization 
    val artifact = m.artifact 
    val version = m.version 
    val statikaVersion = m.statikaVersion 
    val resolvers = m.resolvers
    val privateResolvers = Seq()
  }

  val resourceBucket = ""
  def getResourcePath[B <: BundleAux](bundle: B, relativePath: Path): Path = ""
}
