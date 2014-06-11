import play.PlayJava

name := "ACTion"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

javacOptions in Compile ++= Seq("-source", "1.7", "-target", "1.7")

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "be.objectify" %% "deadbolt-java" % "2.3.0-RC1",
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)
