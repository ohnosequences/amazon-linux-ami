name := "ami-44939930"

description := "Abstract library and a statika bundle for ami-44939930"

homepage := Some(url("https://github.com/statika/ami-44939930"))

organization := "ohnosequences"

organizationHomepage := Some(url("http://ohnosequences.com"))

licenses := Seq("AGPLv3" -> url("http://www.gnu.org/licenses/agpl-3.0.txt"))

bundleObjects := Seq(
  "ohnosequences.statika.ami.AmazonLinuxAMIBundle"
, "ohnosequences.statika.ami.tests.DummyDistribution"
)

libraryDependencies ++= Seq(
  // "ohnosequences" % "statika-cli_2.10.2" % "0.15.1" % "test"
  "ohnosequences" % "statika-cli_2.10.2" % "0.16.0-SNAPSHOT" % "test"
)

statikaVersion := "0.15.0-SNAPSHOT"

awsStatikaVersion := "0.2.0-SNAPSHOT"
