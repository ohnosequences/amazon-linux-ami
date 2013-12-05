package ohnosequences.statika.ami

import ohnosequences.statika._
import ohnosequences.statika.aws._

case object AMI149f7863 extends AmazonLinuxAMI[FatJarMetadata]("ami-149f7863", "2013.09") {

  // just installig java-7 and scala-2.10.3 from rpm's
  // TODO: take care of credentials (now it uses just role credentials)
  def preparing(creds: AWSCredentials) = """
    |yum remove -y java*
    |aws s3 cp s3://resources.ohnosequences.com/java7-oracle.rpm java7-oracle.rpm
    |aws s3 cp s3://resources.ohnosequences.com/scala-2.10.3.rpm scala-2.10.3.rpm
    |yum install -y java7-oracle.rpm scala-2.10.3.rpm
    |""".stripMargin

  def building(
      md: MetadataBound
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
    |java -cp .:dist.jar apply
    |""".stripMargin

  def tag(state: String): String = s"""
    |echo
    |echo " -- ${state} -- "
    |echo
    |aws ec2 create-tags --resources $$ec2id  --tag Key=statika-status,Value=${state} > /dev/null
    |""".stripMargin
}
