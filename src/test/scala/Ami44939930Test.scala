package ohnosequences.statika.Ami44939930_2013_03

import org.scalatest.FunSuite
import shapeless._
import ohnosequences.statika.General._
import ohnosequences.statika.Ami._
import ohnosequences.statika.gener8bundle.TestOnInstance
import buildinfo.MetaData._

class Ami44939930Test extends FunSuite {

  object tester extends Bundle(Ami44939930_2013_03 :: HNil) {
    override val name = Ami44939930_2013_03.name
  }

  test("launching an EC2 instance with " + tester + " bundle"){

    tester.metadata.s3CredentialsFile map { creds =>

      val userscript = tester.ami.userScript(tester)
      TestOnInstance.test(creds, tester.ami.id, userscript)

    }

  }

}
