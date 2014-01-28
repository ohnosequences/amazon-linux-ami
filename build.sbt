Nice.scalaProject

name := "amazon-linux-ami"

description := "Abstract library and a statika bundle for amazon-linux-ami"

organization := "ohnosequences"

bucketSuffix := "era7.com"

libraryDependencies ++= Seq(
  "ohnosequences" %% "aws-statika" % "1.0.1",
  "ohnosequences" %% "aws-scala-tools" % "0.6.1"
)

dependencyOverrides ++= Set(
  "commons-codec" % "commons-codec" % "1.7"
)
