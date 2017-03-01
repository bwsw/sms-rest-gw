name := """sms-rest-gw"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

lazy val root = project.in(file(".")).enablePlugins(PlayScala, DockerPlugin)

libraryDependencies ++= Seq(
  "org.xerial" % "sqlite-jdbc" % "3.16.1",
  "com.typesafe.play" % "play-slick_2.11" % "2.0.2",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.2",
  "com.pauldijou" % "jwt-play_2.11" % "0.11.0",
  "com.plivo" % "plivo-java" % "3.0.9",
  "com.unboundid" % "unboundid-ldapsdk" % "3.2.0"
)