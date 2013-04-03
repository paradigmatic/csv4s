organization := "org.streum"

name := "csv4s"

version := "0.1.0"

scalaVersion := "2.10.1"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "1.9.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.10.0" % "test"
)

scalacOptions ++= Seq("-deprecation", "-feature", "-language:existentials")

scalaSource in Compile <<= baseDirectory(_ / "src")

scalaSource in Test <<= baseDirectory(_ / "test")

