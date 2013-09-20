name := "ami-44939930"

description := "Abstract library and a statika bundle for ami-44939930"

homepage := Some(url("https://github.com/statika/ami-44939930"))

organization := "ohnosequences"

organizationHomepage := Some(url("http://ohnosequences.com"))

licenses := Seq("AGPLv3" -> url("http://www.gnu.org/licenses/agpl-3.0.txt"))


publishMavenStyle := true

publishBucketSuffix := "era7.com"

// no dependencies — no resolvers
publicResolvers := Seq()

libraryDependencies ++= Seq( "ohnosequences" %% "aws-statika" % "0.2.0" )
