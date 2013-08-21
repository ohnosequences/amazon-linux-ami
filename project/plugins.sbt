resolvers ++= Seq(
  Resolver.url("Era7 releases", url("http://releases.era7.com.s3.amazonaws.com"))(Resolver.ivyStylePatterns)
)

addSbtPlugin("ohnosequences" % "sbt-statika" % "0.5.0")
