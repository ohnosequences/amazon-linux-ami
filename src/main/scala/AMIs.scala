package ohnosequences.statika.ami

import ohnosequences.statika._
import ohnosequences.statika.aws._

// AMIs with ephemeral storage

case class ami_1ba18d72(
  val javaHeap: Int,// in G
  val creds: AWSCredentials = RoleCredentials,
  val workingDir: String = "/media/ephemeral0/applicator"
  ) extends FatJarAmazonLinuxAMI(
    id = "ami-1ba18d72",
    amiVersion = "2013.09.2",
    region = UsEast1,
    arch = Arch64
  )

case class ami_8ef297be(
  val javaHeap: Int,// in G
  val creds: AWSCredentials = RoleCredentials,
  val workingDir: String = "/media/ephemeral0/applicator"
  ) extends FatJarAmazonLinuxAMI(
    id = "ami-8ef297be",
    amiVersion = "2013.09.2",
    region = UsWest2,
    arch = Arch64
  )

case class ami_883909cd(
  val javaHeap: Int,// in G
  val creds: AWSCredentials = RoleCredentials,
  val workingDir: String = "/media/ephemeral0/applicator"
  ) extends FatJarAmazonLinuxAMI(
    id = "ami-883909cd",
    amiVersion = "2013.09.2",
    region = UsWest1,
    arch = Arch64
  )

case class ami_5456b823(
  val javaHeap: Int,// in G
  val creds: AWSCredentials = RoleCredentials,
  val workingDir: String = "/media/ephemeral0/applicator"
  ) extends FatJarAmazonLinuxAMI(
    id = "ami-5456b823",
    amiVersion = "2013.09.2",
    region = EuWest1,
    arch = Arch64
  )

case class ami_b8baeeea(
  val javaHeap: Int,// in G
  val creds: AWSCredentials = RoleCredentials,
  val workingDir: String = "/media/ephemeral0/applicator"
  ) extends FatJarAmazonLinuxAMI(
    id = "ami-b8baeeea",
    amiVersion = "2013.09.2",
    region = ApSouthEast1,
    arch = Arch64
  )

case class ami_f30c6ff2(
  val javaHeap: Int,// in G
  val creds: AWSCredentials = RoleCredentials,
  val workingDir: String = "/media/ephemeral0/applicator"
  ) extends FatJarAmazonLinuxAMI(
    id = "ami-f30c6ff2",
    amiVersion = "2013.09.2",
    region = ApNorthEast1,
    arch = Arch64
  )

case class ami_4fa83775(
  val javaHeap: Int,// in G
  val creds: AWSCredentials = RoleCredentials,
  val workingDir: String = "/media/ephemeral0/applicator"
  ) extends FatJarAmazonLinuxAMI(
    id = "ami-4fa83775",
    amiVersion = "2013.09.2",
    region = ApSouthEast2,
    arch = Arch64
  )

case class ami_b99130a4(
  val javaHeap: Int,// in G
  val creds: AWSCredentials = RoleCredentials,
  val workingDir: String = "/media/ephemeral0/applicator"
  ) extends FatJarAmazonLinuxAMI(
    id = "ami-b99130a4",
    amiVersion = "2013.09.2",
    region = SaEast1,
    arch = Arch64
  )
  
