import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val Scala213 = "2.13.6"

// ThisBuild / crossScalaVersions := Seq("2.12.14", Scala213)
ThisBuild / scalaVersion := Scala213

enablePlugins(ScalaJSPlugin)
scalaJSUseMainModuleInitializer := true
scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule))

val http4sV = "1.0-139-8a8323b-SNAPSHOT"
val munitCatsEffectV = "1.0.5"


// No Publish
ThisBuild / githubWorkflowBuild := Seq(WorkflowStep.Sbt(List("test")))
ThisBuild / githubWorkflowPublish := Seq()
ThisBuild / githubWorkflowPublishTargetBranches := Seq()
ThisBuild / githubWorkflowPublishPreamble := Seq()
ThisBuild / githubWorkflowTargetTags := Seq()


// Projects
name := "js-test"
testFrameworks += new TestFramework("munit.Framework")
libraryDependencies ++= Seq(


  "org.http4s"                  %%% "http4s-dsl"                 % http4sV,
  "org.http4s"                  %%% "http4s-ember-server"        % http4sV,
  "org.http4s"                  %%% "http4s-ember-client"        % http4sV,
  "org.http4s"                  %%% "http4s-circe"               % http4sV,


  "org.typelevel"               %%% "munit-cats-effect-3"        % munitCatsEffectV         % Test,
)
