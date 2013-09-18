name := "ami-44939930"

description := "Abstract library and a statika bundle for ami-44939930"

homepage := Some(url("https://github.com/statika/ami-44939930"))

organization := "ohnosequences"

organizationHomepage := Some(url("http://ohnosequences.com"))

licenses := Seq("AGPLv3" -> url("http://www.gnu.org/licenses/agpl-3.0.txt"))


publishMavenStyle := true

publishTo <<= (isSnapshot, s3credentials) { 
                (snapshot,   credentials) => 
  val prefix = if (snapshot) "snapshots" else "releases"
  credentials map S3Resolver(
      "Era7 "+prefix+" S3 bucket"
    , "s3://"+prefix+".era7.com"
    , Resolver.mavenStylePatterns
    ).toSbtResolver
}



// temporary

statikaVersion := "0.15.0-SNAPSHOT"

awsStatikaVersion := "0.2.0-SNAPSHOT"
