import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

ThisBuild / crossScalaVersions := Seq("2.12.16", "2.13.8", "3.2.0")

val catsV = "2.6.1"
val catsTestkitV = "2.1.5"
val scalacheckV = "1.15.4"

lazy val root = project.in(file("."))
  .disablePlugins(MimaPlugin)
  .enablePlugins(NoPublishPlugin)
  .aggregate(
    coreJVM,
    coreJS
  )

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("core"))
  .settings(
    name := "cats-scalacheck",
    libraryDependencies ++= Seq(
      "org.typelevel"               %%% "cats-core"                  % catsV,
      "org.scalacheck"              %%% "scalacheck"                 % scalacheckV,

      "org.typelevel"               %%% "cats-laws"                  % catsV % Test,
      "org.typelevel"               %%% "cats-testkit-scalatest"     % catsTestkitV % Test
    ),
    mimaVersionCheckExcludedVersions := {
      if (isDotty.value) Set("0.3.0") else Set()
    }
  )

lazy val coreJVM = core.jvm
lazy val coreJS = core.js

lazy val site = project.in(file("site"))
  .disablePlugins(MimaPlugin)
  .enablePlugins(NoPublishPlugin)
  .enablePlugins(DavenverseMicrositePlugin)
  .settings(
    name := "cats-scalacheck-docs",
    moduleName := "cats-scalacheck-docs",
    mdocVariables := Map(
      "VERSION" -> version.value
    ),
    micrositeName := "cats-scalacheck",
    micrositeDescription := "Cats Instances for Scalacheck",
    micrositeAuthor := "Christopher Davenport",
    micrositeGithubOwner := "ChristopherDavenport",
  )
  .dependsOn(coreJVM)
