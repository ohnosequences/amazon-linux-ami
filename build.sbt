name := "ami-44939930"

description := "Abstract library and a statika bundle for ami-44939930"

organization := "ohnosequences"

isPrivate := false

bundlePackage := "ohnosequences.statika.ami"

bundleObject := "AmazonLinuxAMIBundle"

libraryDependencies ++= Seq(
  "ohnosequences" % "statika-cli_2.10.2" % "0.13.0" % "test"
  )
