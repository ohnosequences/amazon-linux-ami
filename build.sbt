name := "amazon-linux-ami"

description := "Abstract library and a statika bundle for amazon-linux-ami"

homepage := Some(url("https://github.com/ohnosequences/amazon-linux-ami"))

organization := "ohnosequences"

organizationHomepage := Some(url("http://ohnosequences.com"))

licenses := Seq("AGPLv3" -> url("http://www.gnu.org/licenses/agpl-3.0.txt"))

publishMavenStyle := true

bucketSuffix := "era7.com"

// no dependencies — no resolvers
publicResolvers := Seq()

awsStatikaVersion := "0.5.0"
