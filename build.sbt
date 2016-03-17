name := """play-java-intro1"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

lazy val root = (project in file(".")).enablePlugins(PlayJava,PlayEbean)

libraryDependencies += "org.webjars" % "bootstrap" % "2.1.1"



libraryDependencies += filters

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)