organization := "org.streum"

name := "tabulator"

version := "0.0.1"

scalaVersion := "2.10.0"

libraryDependencies ++= Seq(
  "com.typesafe" %% "scalalogging-slf4j" % "1.0.0",
  "org.hdfgroup" % "hdf-java" % "2.6.1",
  "com.chuusai" %% "shapeless" % "1.2.3",	
  "org.scalatest" %% "scalatest" % "1.9.1" % "test"
)

scalacOptions ++= Seq("-deprecation", "-feature", "-language:existentials")

scalaSource in Compile <<= baseDirectory(_ / "src")

scalaSource in Test <<= baseDirectory(_ / "test")

