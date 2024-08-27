import laika.helium.config._

ThisBuild / tlBaseVersion := "0.3"
ThisBuild / startYear := Some(2018)
ThisBuild / organization := "io.chrisdavenport"
ThisBuild / organizationName := "Davenverse"
ThisBuild / licenses := Seq(License.Apache2)
ThisBuild / homepage := Some(url(s"https://davenverse.github.io/cats-scalacheck"))
ThisBuild / developers := List(tlGitHubDev("ChristopherDavenport", "Christopher Davenport"))

ThisBuild / tlSitePublishBranch := Some("main")
ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.temurin("17"))

ThisBuild / tlVersionIntroduced := Map("3" -> "0.3.1")
ThisBuild / crossScalaVersions := Seq("2.12.19", "2.13.14", "3.3.3")

lazy val root = tlCrossRootProject.aggregate(core)

lazy val core = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Full)
  .in(file("core"))
  .settings(
    name := "cats-scalacheck",
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-core"        % "2.11.0",
      "org.scalacheck" %%% "scalacheck"      % "1.17.1",
      "org.typelevel" %%% "cats-laws"        % "2.11.0"   % Test,
      "org.typelevel" %%% "discipline-munit" % "2.0.0-M3" % Test
    )
  )
  .nativeSettings(
    tlVersionIntroduced := List("2.12", "2.13", "3").map(_ -> "0.3.2").toMap
  )

lazy val site = project
  .in(file("site"))
  .enablePlugins(TypelevelSitePlugin)
  .dependsOn(core.jvm)
  .settings(
    scalaVersion := "3.3.3",
    tlSiteHelium ~= {
      _.site
        .topNavigationBar(
          homeLink =
            TextLink.external("https://davenverse.github.io/cats-scalacheck", "Cats-Scalacheck")
        )
        .site
        .mainNavigation(
          appendLinks = List(
            ThemeNavigationSection(
              "Related Projects",
              TextLink.external("https://github.com/typelevel/cats", "Cats"),
              TextLink.external("https://github.com/typelevel/scalacheck", "Scalacheck")
            )
          )
        )
    }
  )
