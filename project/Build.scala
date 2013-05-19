import sbt._
import Keys._

// sbt-release plugin
import sbtrelease.ReleasePlugin._
import sbtrelease._
import ReleaseStateTransformations._
import sbtrelease.ReleasePlugin.ReleaseKeys._

object Ami44939930Build extends Build {

  lazy val Ami44939930Project = Project(
    id = "Ami44939930Project",
    base = file("."),
    settings = Defaults.defaultSettings ++ releaseSettings ++ Seq(

        releaseProcess <<= thisProjectRef apply { ref =>
          Seq[ReleaseStep](
            checkSnapshotDependencies,              // : ReleaseStep
            inquireVersions,                        // : ReleaseStep
            runTest,                                // : ReleaseStep
            setReleaseVersion,                      // : ReleaseStep
            commitReleaseVersion,                   // : ReleaseStep, performs the initial git checks
            tagRelease,                             // : ReleaseStep
            publishArtifacts,                       // : ReleaseStep, checks whether `publishTo` is properly set up
            setNextVersion,                         // : ReleaseStep
            commitNextVersion,                      // : ReleaseStep
            pushChanges                             // : ReleaseStep, also checks that an upstream branch is properly configured
          )
        },
        releaseVersion := { ver => ver } // don't cut snapshots!
      )
  )

}
