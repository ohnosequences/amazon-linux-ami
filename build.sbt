name := "ami-44939930"

description := "Abstract library and a statika bundle for ami-44939930"

bundleObjects := Seq(
  "ohnosequences.statika.ami.AmazonLinuxAMIBundle"
, "ohnosequences.statika.ami.tests.DummyDistribution"
)

libraryDependencies ++= Seq(
  // "ohnosequences" % "statika-cli_2.10.2" % "0.15.1" % "test"
  "ohnosequences" % "statika-cli_2.10.2" % "0.16.0-SNAPSHOT" % "test"
)
