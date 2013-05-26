resolvers ++= Seq (
  Resolver.url("Era7 Ivy Snapshots", url("http://snapshots.era7.com.s3.amazonaws.com"))(
    Patterns("[organisation]/[module]/[revision]/[type]s/[artifact](-[classifier]).[ext]"))
, Resolver.url("Era7 Ivy Releases", url("http://releases.era7.com.s3.amazonaws.com"))(
    Patterns("[organisation]/[module]/[revision]/[type]s/[artifact](-[classifier]).[ext]"))
)

addSbtPlugin("ohnosequences" % "sbt-statika" % "0.1.1")
