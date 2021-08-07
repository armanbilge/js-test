import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val Scala213 = "2.13.6"

// ThisBuild / crossScalaVersions := Seq("2.12.14", Scala213)
ThisBuild / scalaVersion := Scala213

val http4sV = "1.0.0-M24"
val munitCatsEffectV = "1.0.5"


// No Publish
ThisBuild / githubWorkflowBuild := Seq(WorkflowStep.Sbt(List("test")))
ThisBuild / githubWorkflowPublish := Seq()
ThisBuild / githubWorkflowPublishTargetBranches := Seq()
ThisBuild / githubWorkflowPublishPreamble := Seq()
ThisBuild / githubWorkflowTargetTags := Seq()


// Projects
ThisBuild / testFrameworks += new TestFramework("munit.Framework")

lazy val `js-test` = project.in(file("."))
  .enablePlugins(NoPublishPlugin)
  .disablePlugins(MimaPlugin)
  .aggregate(shared.jvm, shared.js, frontend, backend)

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("shared"))
  .settings(
    name := "js-test-shared",
    libraryDependencies ++= Seq(
      "org.http4s"              %%% "http4s-circe"               % http4sV,
    )
  ).jsConfigure(_.enablePlugins(ScalaJSWeb))

lazy val frontend = project.in(file("frontend"))
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .dependsOn(shared.js)
  .settings(
      name := "js-test-server",
      libraryDependencies ++= Seq(
        "org.scala-js"                %%% "scalajs-dom" % "1.1.0",
        "org.http4s"              %%% "http4s-dom-fetch-client"        % http4sV,
        "org.typelevel"               %%% "munit-cats-effect-3"        % munitCatsEffectV         % Test,
      ),
      Compile / crossTarget := (Compile / resourceManaged).value,
      scalaJSUseMainModuleInitializer := true,
      
    )

lazy val backend = project.in(file("backend"))
  .enablePlugins(SbtWeb)
  .dependsOn(shared.jvm)
  .settings(
    name := "js-test-server",
    libraryDependencies ++= Seq(
      "org.http4s"              %%% "http4s-dsl"                 % http4sV,
      "org.http4s"              %%% "http4s-ember-server"        % http4sV,
      "org.http4s"              %%% "http4s-ember-client"        % http4sV,
      "org.http4s"              %%% "http4s-circe"               % http4sV,
      "ch.qos.logback"        % "logback-classic"              % "1.2.3",
      // "org.reflections" % "reflections" % "0.9.12", - For Debugging Resource Locations
      "org.typelevel"               %%% "munit-cats-effect-3"        % munitCatsEffectV         % Test,
    ),

    Compile / compile := ((Compile / compile) dependsOn scalaJSPipeline).value,
    scalaJSProjects := Seq(frontend),
    Assets / pipelineStages := Seq(scalaJSPipeline),
    Runtime / managedClasspath += (Assets / packageBin).value
  )
