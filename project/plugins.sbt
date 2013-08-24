resolvers ++= Seq(
  Resolver.url("Era7 releases", url("http://releases.era7.com.s3.amazonaws.com"))(Resolver.ivyStylePatterns)
// , Resolver.url("Era7 snapshots", url("http://snapshots.era7.com.s3.amazonaws.com"))(Resolver.ivyStylePatterns)
)

addSbtPlugin("ohnosequences" % "sbt-statika-ohnosequences" % "0.2.0")
