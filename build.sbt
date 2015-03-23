Nice.scalaProject

name := "amazon-linux-ami"

description := "Abstract library and a statika bundle for amazon-linux-ami"

organization := "ohnosequences"

bucketSuffix := "era7.com"

libraryDependencies ++= Seq(
  "ohnosequences" %% "aws-statika" % "2.0.0-SNAPSHOT",
  "ohnosequences" %% "aws-scala-tools" % "0.12.0"
)

dependencyOverrides ++= Set(
  "commons-codec" % "commons-codec" % "1.7"
)
