name := """sms-rest-gw"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

lazy val root = project.in(file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  jdbc,
  "org.xerial" % "sqlite-jdbc" % "3.16.1",
  "com.unboundid" % "unboundid-ldapsdk" % "3.2.0"
)