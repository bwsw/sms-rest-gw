name := """sms-rest-gw"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

lazy val root = project.in(file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  "org.xerial" % "sqlite-jdbc" % "3.16.1",
  "com.typesafe.play" % "play-slick_2.11" % "2.0.2",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.2",
  "com.unboundid" % "unboundid-ldapsdk" % "3.2.0"
)