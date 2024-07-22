

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.3"


lazy val root = (project in file("."))
  .settings(
    name := "jarfles",
    assembly /assemblyJarName := "JarFile.jar"
  )
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % "0.14.7",
      "io.circe" %% "circe-generic" % "0.14.7",
      "io.circe" %% "circe-parser" % "0.14.7",
      "org.scalatest" %% "scalatest" % "3.2.16" % Test

)
