val scala3Version = "3.3.3"
val circeVersion = "0.14.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "hello-world",
    version := "3.5.0",
    scalaVersion := scala3Version,
    libraryDependencies += "com.softwaremill.sttp.client4" %% "circe" % "4.0.0-M17",
    libraryDependencies += "com.lihaoyi" %% "upickle" % "3.1.0",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % circeVersion)
  )
