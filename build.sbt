import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

ThisBuild / crossScalaVersions := Seq("2.12.18", "2.13.8", "3.1.3")

val catsV = "2.8.0"
val disciplineMunit = "2.0.0-M3"
val scalacheckV = "1.16.0"

lazy val root = project.in(file("."))
  .disablePlugins(MimaPlugin)
  .enablePlugins(NoPublishPlugin)
  .aggregate(
    coreJVM,
    coreJS,
    coreNative
  )

lazy val core = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Full)
  .in(file("core"))
  .settings(
    name := "cats-scalacheck",
    libraryDependencies ++= Seq(
      "org.typelevel"               %%% "cats-core"                  % catsV,
      "org.scalacheck"              %%% "scalacheck"                 % scalacheckV,

      "org.typelevel"               %%% "cats-laws"                  % catsV % Test,
      "org.typelevel"               %%% "discipline-munit"           % disciplineMunit % Test
    ),
    mimaVersionCheckExcludedVersions := {
      if (isDotty.value) Set("0.3.0") else Set()
    }
  )
  .nativeSettings(
    mimaVersionCheckExcludedVersions += "0.3.1"
  )

lazy val coreJVM = core.jvm
lazy val coreJS = core.js
lazy val coreNative = core.native

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
