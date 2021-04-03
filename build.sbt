import Dependencies._

ThisBuild / scalaVersion     := "2.13.4"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

val AkkaVersion = "2.6.13"
lazy val root = (project in file("."))
  .settings(
    name := "scheduleForMe",
    libraryDependencies += scalaTest % Test
  )
