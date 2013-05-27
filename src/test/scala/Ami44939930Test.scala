package ohnosequences.statika.Ami44939930_2013_03

import org.scalatest.FunSuite
import shapeless._
import ohnosequences.statika.General._
import ohnosequences.statika.MetaData._
import ohnosequences.statika.Ami._
import ohnosequences.statika.gener8bundle._

class Ami44939930Test extends FunSuite {

  import MetaData._

  val ami = Ami44939930_2013_03

  // bundle, which pretends to be Ami44939930_2013_03, 
  //  because it needs to be self-dependent
  object Tester extends Bundle(ami :: HNil) {
    override val name = ami.name

    implicit object TesterMD extends MetaDataOf[Tester.type] {
      val artifact = ami.metadata.artifact
      val version = ami.metadata.version
      val s3CredentialsFile = ami.metadata.s3CredentialsFile
    }
  }

  test(s"launching an EC2 instance with $Tester.name (Tester.metadata) bundle"){

    Tester.metadata.s3CredentialsFile map { creds =>

      val userscript = Tester.ami.userScript(Tester)
      TestOnInstance.test(creds, Tester.ami.id, userscript)

    }

  }

}
