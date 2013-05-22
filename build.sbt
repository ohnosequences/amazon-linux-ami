
import sbtrelease._
import ReleaseStateTransformations._
import ReleasePlugin._
import ReleaseKeys._

name := "ami-44939930.2013.03"

description := "Bundle checking that instance is run with ami-44939930 Amazon Machine Image"

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
                            , "ohnosequences" % "gener8bundle_2.10.0" % "0.9.0" % "test"
                            , "org.scalatest" %% "scalatest" % "1.9.1" % "test"
                            )

libraryDependencies ++= Seq("ohnosequences" %% "ami-bundle" % "0.3.0") 

scalacOptions ++= Seq("-feature"
                    , "-language:higherKinds"
                    , "-language:implicitConversions"
                    , "-deprecation"
                    , "-unchecked"
                    )

// sbt-release settings

releaseSettings

releaseProcess <<= thisProjectRef apply { ref =>
  Seq[ReleaseStep](
    checkSnapshotDependencies
  , inquireVersions
  , runTest
  , setReleaseVersion
  , commitReleaseVersion
  , tagRelease
  , publishArtifacts
  , setNextVersion
  , commitNextVersion
  , pushChanges
  )
}

// sbt-buildinfo settings

buildInfoSettings

sourceGenerators in Compile <+= buildInfo

buildInfoKeys := Seq[BuildInfoKey](name, version)

buildInfoPackage := "ohnosequences.statika.Ami44939930_2013_03"
