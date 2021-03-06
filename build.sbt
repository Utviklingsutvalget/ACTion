name := "ACTion"

version := "1.0-SNAPSHOT"

lazy val ACTion = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

javacOptions in Compile ++= Seq("-source", "1.8", "-target", "1.8")

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.webjars" %% "webjars-play" % "2.4.0",
  "org.webjars" % "foundation" % "5.5.2",
  "org.webjars" % "ckeditor" % "4.4.7-1",
  "org.webjars" % "angularjs" % "1.4.1",
  "org.webjars" % "datetimepicker" % "2.3.4",
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "com.google.api-client" % "google-api-client" % "1.17.0-rc",
  "org.json" % "json" % "20140107",
  "com.timgroup" % "jgravatar" % "1.0",
  "mysql" % "mysql-connector-java" % "5.1.6",
  "com.google.http-client" % "google-http-client-jackson2" % "1.11.0-beta",
  "org.jsoup" % "jsoup" % "1.7.2",
  "com.googlecode.libphonenumber" % "libphonenumber" % "3.1",
  "com.feth" %% "play-authenticate" % "0.7.0-SNAPSHOT"
)


libraryDependencies += evolutions

pipelineStages := Seq(rjs)

RjsKeys.appDir := new File("app/assets")

libraryDependencies in Test += "junit" % "junit" % "4.12"


// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

resolvers += Resolver.sonatypeRepo("snapshots")