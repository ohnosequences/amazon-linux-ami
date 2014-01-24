package ohnosequences.statika.ami

import ohnosequences.statika._
import ohnosequences.statika.aws._

// TODO: move it to aws-scala-tools

sealed abstract class AWSRegion(str: String) { override def toString = str }

case object ApNorthEast1 extends AWSRegion("ap-northeast-1") // Tokyo
case object ApSouthEast1 extends AWSRegion("ap-southeast-1") // Singapore
case object ApSouthEast2 extends AWSRegion("ap-southeast-2") // Sydney
case object EuWest1 extends AWSRegion("eu-west-1") // Ireland
case object SaEast1 extends AWSRegion("sa-east-1") // São Paulo
case object UsEast1 extends AWSRegion("us-east-1") // Northern Virginia
case object UsWest1 extends AWSRegion("us-west-1") // Northern California
case object UsWest2 extends AWSRegion("us-west-2") // Oregon

// synonims
object AWSRegion {
  val Tokyo              = ApNorthEast1
  val Singapore          = ApSouthEast1
  val Sydney             = ApSouthEast2
  val Ireland            = EuWest1
  val SaoPaulo           = SaEast1
  val NorthernVirginia   = UsEast1
  val NorthernCalifornia = UsWest1
  val Oregon             = UsWest2
}
