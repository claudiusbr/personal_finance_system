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
    libraryDependencies += "org.scala-lang.modules" % "scala-swing_2.12" % "2.0.2",
    libraryDependencies += "org.mockito" % "mockito-all" % "1.9.5" % "test",
    libraryDependencies += "joda-time" % "joda-time" % "2.9.9",
    libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.24",
    libraryDependencies += "com.h2database" % "h2" % "1.4.196"
  )
