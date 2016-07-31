name := """duperemoval"""

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.google.guava" % "guava" % "19.0"
  ,"org.apache.commons" % "commons-lang3" % "3.4"
  ,"org.specs2" %% "specs2-core" % "3.6" % "test"
)