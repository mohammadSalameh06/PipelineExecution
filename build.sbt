import sbt.Keys.name
import sbtassembly.AssemblyKeys.assemblyJarName


ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.3"

lazy val root = (project in file("."))
  .settings(
      name := "jarfles",
assembly /assemblyJarName := "JarFile"

  )

libraryDependencies += "io.circe" %% "circe-core" % "0.14.1"
libraryDependencies += "io.circe" %% "circe-generic" % "0.14.1"
libraryDependencies += "io.circe" %% "circe-parser" % "0.14.1"

val circeVersion = "0.14.7"


libraryDependencies ++= Seq(
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion
)


//ThisBuild / mainClass := Some("ae.network.migration.pipeline.Main")
 