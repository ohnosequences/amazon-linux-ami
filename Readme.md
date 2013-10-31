### Amazon Linux AMI for Statika

This is a library that implements `AbstractAMI` interface from [aws-statika](https://github.com/ohnosequences/aws-statika) for the Amazon Linux AMI.

#### Usage

Add a dependency to your sbt project

```scala
resolvers += "Era7 maven releases" at "http://releases.era7.com.s3.amazonaws.com"

libraryDependencies += "ohnosequences" %% "amazon-linux-ami" % "0.12.0"
```
