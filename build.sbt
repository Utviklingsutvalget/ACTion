import play.PlayJava

name := "ACTion"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

javacOptions in Compile ++= Seq("-source", "1.7", "-target", "1.7")

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)

//resolvers += Resolver.sonatypeRepo("releases")

//libraryDependencies += "ws.securesocial" %% "securesocial" % "2.1.3"
