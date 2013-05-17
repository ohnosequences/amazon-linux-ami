
import sbtrelease._
import ReleaseStateTransformations._
import Ami44939930Build._

name := "ami-44939930.2013.03"

organization := "ohnosequences"

scalaVersion := "2.10.0"

publishMavenStyle := true

publishPrivate := false

publishTo <<= (version, publishPrivate) { (v: String, p: Boolean) =>
  Some(s3resolver(isSnapshot = v.trim.endsWith("SNAPSHOT"), isPrivate = p, publisher = true))
}

resolvers ++= Seq (
                    "Typesafe Releases"   at "http://repo.typesafe.com/typesafe/releases"
                  , "Sonatype Releases"   at "https://oss.sonatype.org/content/repositories/releases"
                  , "Sonatype Snapshots"  at "https://oss.sonatype.org/content/repositories/snapshots"
                  , "Era7 Releases"       at "http://releases.era7.com.s3.amazonaws.com"
                  , "Era7 Snapshots"      at "http://snapshots.era7.com.s3.amazonaws.com"
                  )

resolvers ++= s3resolvers

libraryDependencies ++= Seq (
                              "com.chuusai" %% "shapeless" % "1.2.3"
                            , "ohnosequences" %% "statika" % "0.7.1"
                            , "org.scalatest" %% "scalatest" % "1.9.1" % "test"
                            , "ohnosequences" % "aws-scala-tools_2.10" % "0.2.3" % "test"
                            )

libraryDependencies ++= Seq("ohnosequences" %% "ami-bundle" % "0.1.1") 

scalacOptions ++= Seq("-feature"
                    , "-language:higherKinds"
                    , "-language:implicitConversions"
                    , "-deprecation"
                    , "-unchecked"
                    )
