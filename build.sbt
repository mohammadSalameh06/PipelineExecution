ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.3"

lazy val root = (project in file("."))
  .settings(
    name := "jarfiles",
    assembly / assemblyJarName := "JarFile.jar",
    assembly / mainClass := Some("ae.network.migration.pipeline.Main"),
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % "0.14.7",
      "io.circe" %% "circe-generic" % "0.14.7",
      "io.circe" %% "circe-parser" % "0.14.7",
      "org.scalatest" %% "scalatest" % "3.2.16" % Test,
      "org.mockito" % "mockito-core" % "3.12.4" % Test
  )

)

// Scoverage settings
coverageEnabled := true
coverageFailOnMinimum := true // Fail build if coverage is below minimum
coverageHighlighting := true // Enable code highlighting in the coverage report
coverageExcludedFiles := "ae.network.migration.pipeline.processExecute.MainTest" // Exclude test files
coverageMinimumStmtTotal := 80
coverageMinimumBranchTotal := 80
coverageExcludedPackages := ""
