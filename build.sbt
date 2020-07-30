name := """ixias-sample-code"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.11"

libraryDependencies += guice
//libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

resolvers ++= Seq(
  "IxiaS Releases" at "http://maven.ixias.net.s3-ap-northeast-1.amazonaws.com/releases"
)
libraryDependencies ++= Seq(
  "net.ixias" %% "ixias"      % "1.1.25",
  "net.ixias" %% "ixias-aws"  % "1.1.25",
  "net.ixias" %% "ixias-play" % "1.1.25",
  "mysql" % "mysql-connector-java" % "5.1.+",
  "ch.qos.logback" % "logback-classic" % "1.1.+"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
