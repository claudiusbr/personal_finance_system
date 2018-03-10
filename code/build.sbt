import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "uk.ac.bbk.dcs.cdemou01",
      scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "PersonalFinanceSystem",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "org.mockito" % "mockito-all" % "1.9.5" % "test"
  )
