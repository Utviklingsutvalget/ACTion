name := "ACTion"

version := "1.0-SNAPSHOT"

lazy val ACTion = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

javacOptions in Compile ++= Seq("-source", "1.8", "-target", "1.8")

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

libraryDependencies += evolutions
libraryDependencies += "org.webjars" %% "webjars-play" % "2.4.0"
libraryDependencies += "org.webjars" % "foundation" % "5.5.2"
libraryDependencies += "org.webjars" % "ckeditor" % "4.4.7-1"
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.3.2"
libraryDependencies += "com.google.api-client" % "google-api-client" % "1.17.0-rc"
libraryDependencies += "org.json" % "json" % "20140107"
libraryDependencies += "com.timgroup" % "jgravatar" % "1.0"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.6"
libraryDependencies += "com.google.http-client" % "google-http-client-jackson2" % "1.11.0-beta"
libraryDependencies += "org.jsoup" % "jsoup" % "1.7.2"
libraryDependencies += "com.googlecode.libphonenumber" % "libphonenumber" % "3.1"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator