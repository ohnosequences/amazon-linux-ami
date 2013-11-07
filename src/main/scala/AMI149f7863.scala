package ohnosequences.statika.ami

import ohnosequences.statika._
import ohnosequences.statika.aws._

case object AMI149f7863 extends AmazonLinuxAMI("ami-149f7863", "2013.09") {

  type Metadata = FatJarMetadata


  // just installig scala-2.10.3 from rpm
  // TODO: take care of credentials (now it uses just role credentials)
  def preparing(creds: AWSCredentials) = """
    |curl http://www.scala-lang.org/files/archive/scala-2.10.3.rpm > scala.rpm
    |yum install scala.rpm java-1.7.0-openjdk -y
    |""".stripMargin

  def building(
      md: Metadata
    , distName: String
    , bundleName: String
    , creds: AWSCredentials = RoleCredentials
    ): String = s"""
    |mkdir applicator
    |cd applicator
    |
    |echo "object apply extends App { " > apply.scala
    |echo "  val results = ${distName}.installWithDeps(${bundleName}); " >> apply.scala
    |echo "  results foreach println; " >> apply.scala
    |echo "  if (results.hasFailures) sys.error(results.toString) " >> apply.scala
    |echo "}" >> apply.scala
    |cat apply.scala
    |
    |aws s3 cp ${md.artifactUrl} dist.jar
    |
    |scalac -cp dist.jar apply.scala
    |""".stripMargin

  def applying: String = """
    |java7 -cp .:dist.jar apply
    |""".stripMargin

  def tag(state: String): String = s"""
    |echo
    |echo " -- ${state} -- "
    |echo
    |aws ec2 create-tags --resources $$ec2id  --tag Key=statika-status,Value=${state} > /dev/null
    |""".stripMargin
}
