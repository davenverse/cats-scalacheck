import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

lazy val root = project.in(file("."))
  .disablePlugins(MimaPlugin)
  .aggregate(
    coreJVM,
    coreJS
  )
  .settings(noPublishSettings)
  .settings(commonSettings, releaseSettings)


lazy val core = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("core"))
  .settings(commonSettings, releaseSettings, mimaSettings)
  .settings(
    name := "cats-scalacheck"
  )

lazy val coreJVM = core.jvm
lazy val coreJS = core.js

lazy val docs = project.in(file("docs"))
  .settings(noPublishSettings)
  .settings(commonSettings, micrositeSettings)
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(TutPlugin)
  .dependsOn(coreJVM)

val catsV = "2.0.0-M4"
val scalacheckV = "1.14.0"

lazy val contributors = Seq(
  "ChristopherDavenport" -> "Christopher Davenport"
)

lazy val commonSettings = Seq(
  organization := "io.chrisdavenport",

  scalaVersion := "2.12.8",
  crossScalaVersions := Seq(scalaVersion.value, "2.11.12", "2.13.0"),

  addCompilerPlugin("org.typelevel" % "kind-projector" % "0.10.3" cross CrossVersion.binary),
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),

  libraryDependencies ++= Seq(
    "org.typelevel"               %%% "cats-core"                  % catsV,
    "org.scalacheck"              %%% "scalacheck"                 % scalacheckV,

    "org.typelevel"               %%% "cats-laws"                  % catsV % Test,
    "org.typelevel"               %%% "cats-testkit"               % catsV % Test
  )
)

lazy val releaseSettings = {
  import ReleaseTransformations._
  Seq(
    releaseCrossBuild := true,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      // For non cross-build projects, use releaseStepCommand("publishSigned")
      releaseStepCommandAndRemaining("+publishSigned"),
      setNextVersion,
      commitNextVersion,
      releaseStepCommand("sonatypeReleaseAll"),
      pushChanges
    ),
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    credentials ++= (
      for {
        username <- Option(System.getenv().get("SONATYPE_USERNAME"))
        password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
      } yield
        Credentials(
          "Sonatype Nexus Repository Manager",
          "oss.sonatype.org",
          username,
          password
        )
    ).toSeq,
    publishArtifact in Test := false,
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/ChristopherDavenport/cats-scalacheck"),
        "git@github.com:ChristopherDavenport/cats-scalacheck.git"
      )
    ),
    homepage := Some(url("https://github.com/ChristopherDavenport/cats-scalacheck")),
    licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
    publishMavenStyle := true,
    pomIncludeRepository := { _ =>
      false
    },
    pomExtra := {
      <developers>
        {for ((username, name) <- contributors) yield
        <developer>
          <id>{username}</id>
          <name>{name}</name>
          <url>http://github.com/{username}</url>
        </developer>
        }
      </developers>
    }
  )
}

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

// Not Used Currently
lazy val mimaSettings = {
  import sbtrelease.Version
  def mimaVersion(version: String) = {
    Version(version) match {
      case Some(Version(major, Seq(minor, patch), _)) if patch.toInt > 0 =>
        Some(s"${major}.${minor}.${patch.toInt - 1}")
      case _ =>
        None
    }
  }

  Seq(
    mimaFailOnNoPrevious := false,
    mimaFailOnProblem := mimaVersion(version.value).isDefined,
    mimaPreviousArtifacts := (mimaVersion(version.value) map {
      organization.value % s"${moduleName.value}_${scalaBinaryVersion.value}" % _
    }).toSet,
    mimaBinaryIssueFilters ++= {
      import com.typesafe.tools.mima.core._
      import com.typesafe.tools.mima.core.ProblemFilters._
      Seq()
    }
  )
}

lazy val noPublishSettings = {
  import com.typesafe.sbt.pgp.PgpKeys.publishSigned
  Seq(
    publish := {},
    publishLocal := {},
    publishSigned := {},
    publishArtifact := false
  )
}
