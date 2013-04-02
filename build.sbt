organization := "org.streum"

name := "tabulator"

version := "0.0.1"

scalaVersion := "2.10.1"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "1.9.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.10.0" % "test"
)

scalacOptions ++= Seq("-deprecation", "-feature", "-language:existentials")

scalaSource in Compile <<= baseDirectory(_ / "src")

scalaSource in Test <<= baseDirectory(_ / "test")

