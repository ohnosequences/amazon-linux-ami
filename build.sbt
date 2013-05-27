name := "ami-44939930.2013.03"

description := "Bundle checking that instance is run with ami-44939930 Amazon Machine Image"

organization := "ohnosequences"

isPrivate := false

libraryDependencies ++= Seq("ohnosequences" %% "ami-bundle" % "0.5.0") 


bundlePackage := "ohnosequences.statika.Ami44939930_2013_03"

bundleObject := "Ami44939930_2013_03"
