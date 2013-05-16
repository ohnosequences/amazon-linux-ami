package ohnosequences.statika.Ami44939930_2013_03

import org.scalatest.FunSuite
import shapeless._
import ohnosequences.statika.General._
import ohnosequences.awstools.ec2._
import java.io._
 
class ExampleSuite extends FunSuite {

    val ec2 = EC2.create(new File("AwsCredentials.properties"))

    val instances = {
      val specs = InstanceSpecs(
          instanceType = InstanceType.InstanceType("c1.medium")
        , amiId = "ami-44939930"
        , keyName = "statika-launcher"
        , deviceMapping = Map()
        , userData = Ami44939930_2013_03.userScript(Ami44939930Tester, "ami-44939930.2013.03")
        , instanceProfileARN = Some("arn:aws:iam::857948138625:instance-profile/statika-tester")
        )
      ec2.runInstances(1, specs)
    }

    if (instances.isEmpty) {
      println("Couldn't access an instance for testing")
    }
    val instance = instances.head

    println("Instance ID: " + instance.getInstanceId())

    print("Instance initialization...")
    while (instance.getState() != "running") {
      Thread sleep 1000; print(".")
    }; println("ok!")

    val addr = instance.getPublicDNS().get
    println("Instance address: " + addr)
    
    print("Instance status ckecks...")
    while (instance.getStatus() != Some(InstanceStatus("ok","ok"))) {
      Thread sleep 1000; print(".")
    }; println("ok!")
}
