
name := "finally-free"

version := "0.1"

scalaVersion := "2.12.6"


libraryDependencies += "org.typelevel" %% "cats-effect" % "1.0.0-RC2"
libraryDependencies += "org.typelevel" %% "cats-free" % "1.0.1"

scalacOptions += "-Ypartial-unification"

scalafmtOnCompile in ThisBuild := true
