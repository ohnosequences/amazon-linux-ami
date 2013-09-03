package ohnosequences.statika.ami.tests

import ohnosequences.statika._
import ami._

class Ami44939930Test extends org.scalatest.FunSuite {

  import cli.StatikaEC2._
  import ohnosequences.awstools.ec2._
  import java.io._

  test("Running instance with Ami44939930 bundle test"){

    val userscript = DummyDistribution.userScript(AmazonLinuxAMIBundle, RoleCredentials)
    println(userscript)

    // for running test you need to have this file in your project folder
    val ec2 = EC2.create(new File("AwsCredentials.properties"))

    val specs = InstanceSpecs(
        instanceType = InstanceType.InstanceType("c1.medium")
      , amiId = DummyDistribution.ami.id
      , keyName = "statika-launcher" 
      , deviceMapping = Map()
      , userData = userscript
      , instanceProfileARN = DummyDistribution.metadata.instanceProfileARN
      )

    // we asked for 1 instance — we should get 1 instance
    // ec2.runInstancesAndWait(1, specs).length == 1
    ec2.applyAndWait(specs)
  }

}
