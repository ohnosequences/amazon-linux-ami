package ohnosequences.statika.Ami44939930_2013_03

import org.scalatest.FunSuite
import shapeless._
import ohnosequences.statika.General._
import ohnosequences.statika.gener8bundle.TestOnInstance

class Ami44939930Test extends FunSuite {

  object Ami44939930Tester extends Bundle(
      name = Ami44939930_2013_03.name
    , version = Ami44939930_2013_03.version
    , artifact = Ami44939930_2013_03.artifact
    , dependencies = Ami44939930_2013_03 :: HNil)

  test("launching an EC2 instance with " + Ami44939930Tester + " bundle"){
    val userscript = Ami44939930_2013_03.userScript(Ami44939930Tester)
    TestOnInstance.test("AwsCredentials.properties", Ami44939930_2013_03.id, userscript)
  }

}
