
import sbtrelease._
import ReleaseStateTransformations._
import Ami44939930Build._

name := "ami-44939930.2013.03"

organization := "ohnosequences"

scalaVersion := "2.10.0"

publishMavenStyle := false

s3credentialsFile := Some("AwsCredentials.properties")

publishPrivate := false

publishTo <<= (s3credentials, version, publishPrivate)(s3publisher(statikaPrefix)) 

resolvers ++= Seq (
                    Resolver.typesafeRepo("releases")
                  , Resolver.sonatypeRepo("releases")
                  , Resolver.sonatypeRepo("snapshots")
                  , "Era7 Releases"  at "http://releases.era7.com.s3.amazonaws.com"
                  , "Era7 Snapshots" at "http://snapshots.era7.com.s3.amazonaws.com"
                  , PublicBundleSnapshots
                  , PublicBundleReleases
                  )

resolvers <++= s3credentials(PrivateBundleResolvers(statikaPrefix))

libraryDependencies ++= Seq (
                              "com.chuusai" %% "shapeless" % "1.2.3"
                            , "ohnosequences" %% "statika" % "0.8.1"
                            , "ohnosequences" %% "aws-scala-tools" % "0.2.3" % "test"
                            , "org.scalatest" %% "scalatest" % "1.9.1" % "test"
                            )

libraryDependencies ++= Seq("ohnosequences" %% "ami-bundle" % "0.3.0") 

scalacOptions ++= Seq("-feature"
                    , "-language:higherKinds"
                    , "-language:implicitConversions"
                    , "-deprecation"
                    , "-unchecked"
                    )
