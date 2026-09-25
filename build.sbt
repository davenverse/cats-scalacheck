ThisBuild / tlBaseVersion := "0.4" // current series x.y

ThisBuild / organization := "io.chrisdavenport"
ThisBuild / organizationName := "Christopher Davenport"
ThisBuild / startYear := Some(2018)
ThisBuild / licenses := Seq(License.MIT)
ThisBuild / developers := List(
  tlGitHubDev("christopherdavenport", "Christopher Davenport")
)

// sbt-davenverse published a snapshot from main on every push; preserve that.
ThisBuild / tlCiReleaseBranches := Seq()

val Scala213 = "2.13.18"
ThisBuild / crossScalaVersions := Seq("2.12.20", Scala213, "3.3.8")
ThisBuild / scalaVersion := Scala213

val catsV = "2.8.0"
val disciplineMunit = "2.0.0-M3"
val scalacheckV = "1.16.0"

// Compiler settings DavenversePlugin injected globally. sbt-typelevel-ci-release
// does not supply these (only sbt-typelevel-settings would).
lazy val davenverseCompat = Seq(
  libraryDependencies ++= (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, _)) =>
      Seq(
        compilerPlugin("org.typelevel" % "kind-projector" % "0.13.4" cross CrossVersion.full),
        compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
      )
    case _ => Nil
  }),
  scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((3, _)) => Seq("-Ykind-projector")
    case Some((2, 12)) => Seq("-Ypartial-unification")
    case _ => Nil
  })
)

lazy val root = tlCrossRootProject.aggregate(core)

lazy val core = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Full)
  .in(file("core"))
  .settings(davenverseCompat)
  .settings(
    name := "cats-scalacheck",
    libraryDependencies ++= Seq(
      "org.typelevel"               %%% "cats-core"                  % catsV,
      "org.scalacheck"              %%% "scalacheck"                 % scalacheckV,

      "org.typelevel"               %%% "cats-laws"                  % catsV % Test,
      "org.typelevel"               %%% "discipline-munit"           % disciplineMunit % Test
    )
  )

lazy val coreJVM = core.jvm
lazy val coreJS = core.js
lazy val coreNative = core.native

lazy val site = project.in(file("site"))
  .enablePlugins(TypelevelSitePlugin)
  .dependsOn(coreJVM)
  .settings(
    laikaTheme := tlSiteHelium.value.site
      .topNavigationBar(
        homeLink = laika.helium.config.IconLink.internal(laika.ast.Path.Root / "index.md", laika.helium.config.HeliumIcon.home)
      )
      .build
  )
