val Version = new {
  val CatsEffect = "3.2.8"
  val Munit = "0.7.29"
  val MunitCatsEffect = "1.0.5"
  val Scala2 = "2.13.6"
  val Scala3 = "3.0.2"
}

ThisBuild / crossScalaVersions := Version.Scala2 :: Version.Scala3 :: Nil
ThisBuild / developers := List(Developer("taig", "Niklas Klein", "mail@taig.io", url("https://taig.io/")))
ThisBuild / dynverVTagPrefix := false
ThisBuild / homepage := Some(url("https://github.com/taig/scala-pygments/"))
ThisBuild / licenses := List("MIT" -> url("https://raw.githubusercontent.com/taig/scala-pygments/main/LICENSE"))
ThisBuild / scalaVersion := Version.Scala2
ThisBuild / versionScheme := Some("early-semver")

//ThisBuild / Test / parallelExecution := false
//ThisBuild / Test / testOptions += Tests.Argument(TestFrameworks.MUnit, "-b")

lazy val root = project
  .in(file("."))
  .settings(noPublishSettings)
  .settings(
    name := "scala-pygments"
  )
  .aggregate(core.jvm, core.js, graalvmPython, benchmarks)

lazy val core = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/core"))
  .settings(
    name := "scala-pygments-core",
    libraryDependencies ++=
      "org.scalameta" %%% "munit" % Version.Munit % "test" ::
        "org.typelevel" %%% "cats-effect" % Version.CatsEffect % "test" ::
        "org.typelevel" %%% "munit-cats-effect-3" % Version.MunitCatsEffect % "test" ::
        Nil
  )

lazy val graalvmPython = project
  .in(file("modules/graalvm-python"))
  .settings(
    name := "scala-pygments-graalvm-python",
    libraryDependencies ++=
      "org.typelevel" %% "cats-effect" % Version.CatsEffect ::
        Nil
  )
  .dependsOn(core.jvm % "compile->compile;test->test")

lazy val benchmarks = project
  .in(file("modules/benchmarks"))
  .enablePlugins(JmhPlugin)
  .settings(noPublishSettings)
  .settings(
    name := "scala-pygments-benchmarks"
  )
  .dependsOn(graalvmPython)
