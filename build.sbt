import play.PlayJava

name := "ACTion"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

javacOptions in Compile ++= Seq("-source", "1.7", "-target", "1.7")

scalaVersion := "2.10.4"

resolvers += Resolver.url("Objectify Play Repository", url("http://schaloner.github.com/releases/"))(Resolver.ivyStylePatterns)

resolvers += Resolver.url("Objectify Play Snapshot Repository", url("http://schaloner.github.com/snapshots/"))(Resolver.ivyStylePatterns)

libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "org.json" % "json" % "20140107",
  "be.objectify" %% "deadbolt-java" % "2.0-SNAPSHOT",
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)
