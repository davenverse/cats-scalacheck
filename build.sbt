import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

lazy val root = project
  .in(file("."))
  .disablePlugins(MimaPlugin)
  .enablePlugins(NoPublishPlugin)
  .aggregate(
    coreJVM,
    coreJS
  )
  .settings(commonSettings)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("core"))
  .settings(commonSettings)
  .settings(
    name := "cats-scalacheck"
  )

lazy val coreJVM = core.jvm
lazy val coreJS = core.js

lazy val docs = project
  .in(file("modules/docs"))
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(MdocPlugin)
  .enablePlugins(NoPublishPlugin)
  .settings(commonSettings, micrositeSettings)
  .settings(
    name := "cats-scalacheck-docs",
    crossScalaVersions := Seq("2.12.11"),
    moduleName := "cats-scalacheck-docs",
    mdocIn := sourceDirectory.value / "main" / "mdoc",
    mdocVariables := Map(
      "VERSION" -> version.value
    )
  )
  .dependsOn(coreJVM)

val catsV = "2.2.0"
val catsTestkitV = "2.0.0"
val scalacheckV = "1.15.0"

lazy val contributors = Seq(
  "ChristopherDavenport" -> "Christopher Davenport"
)

lazy val commonSettings = Seq(
  organization := "io.chrisdavenport",
  crossScalaVersions := Seq("2.12.11", "2.13.2"),
  addCompilerPlugin("org.typelevel" % "kind-projector"     % "0.10.3" cross CrossVersion.binary),
  addCompilerPlugin("com.olegpy"   %% "better-monadic-for" % "0.3.1"),
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats-core"              % catsV,
    "org.scalacheck" %%% "scalacheck"            % scalacheckV,
    "org.typelevel" %%% "cats-laws"              % catsV        % Test,
    "org.typelevel" %%% "cats-testkit-scalatest" % catsTestkitV % Test
  )
)

lazy val micrositeSettings = Seq(
  micrositeName := "cats-scalacheck",
  micrositeDescription := "Cats Instances for Scalacheck",
  micrositeAuthor := "Christopher Davenport",
  micrositeGithubOwner := "ChristopherDavenport",
  micrositeGithubRepo := "cats-scalacheck",
  micrositeBaseUrl := "/cats-scalacheck",
  micrositeDocumentationUrl := "https://christopherdavenport.github.io/cats-scalacheck",
  micrositeFooterText := None,
  micrositeHighlightTheme := "atom-one-light",
  micrositePalette := Map(
    "brand-primary" -> "#3e5b95",
    "brand-secondary" -> "#294066",
    "brand-tertiary" -> "#2d5799",
    "gray-dark" -> "#49494B",
    "gray" -> "#7B7B7E",
    "gray-light" -> "#E5E5E6",
    "gray-lighter" -> "#F4F3F4",
    "white-color" -> "#FFFFFF"
  ),
  fork in tut := true,
  scalacOptions in Tut --= Seq(
    "-Xfatal-warnings",
    "-Ywarn-unused-import",
    "-Ywarn-numeric-widen",
    "-Ywarn-dead-code",
    "-Ywarn-unused:imports",
    "-Xlint:-missing-interpolator,_"
  ),
  libraryDependencies += "com.47deg" %% "github4s" % "0.20.1",
  micrositePushSiteWith := GitHub4s,
  micrositeGithubToken := sys.env.get("GITHUB_TOKEN")
)

// General Settings
inThisBuild(
  List(
    organization := "io.chrisdavenport",
    developers := List(
      Developer(
        "ChristopherDavenport",
        "Christopher Davenport",
        "chris@christopherdavenport.tech",
        url("https://github.com/ChristopherDavenport")
      )
    ),
    homepage := Some(url("https://github.com/ChristopherDavenport/cats-scalacheck")),
    licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
    pomIncludeRepository := { _ => false },
    scalacOptions in (Compile, doc) ++= Seq(
      "-groups",
      "-sourcepath",
      (baseDirectory in LocalRootProject).value.getAbsolutePath,
      "-doc-source-url",
      "https://github.com/ChristopherDavenport/cats-scalacheck/blob/v" + version.value + "€{FILE_PATH}.scala"
    )
  )
)
