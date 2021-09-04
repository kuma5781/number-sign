name := """number-sign"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.6"

routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= Seq(
  guice,
  filters,
  jdbc,
  "org.specs2" %% "specs2-core" % "4.5.1",
  "org.specs2" %% "specs2-mock" % "4.5.1",
  "org.specs2" %% "specs2-scalacheck" % "4.5.1",
  "mysql" % "mysql-connector-java" % "8.0.15",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
