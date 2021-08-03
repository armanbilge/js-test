package io.chrisdavenport.jstest

import cats.effect._
import cats.effect.unsafe.implicits._
import org.http4s._
import org.http4s.implicits._
import org.http4s.node.serverless.ServerlessApp

import scala.scalajs.js

object Main {

  val awsLambdaAdapter = js.Dynamic.global.require("serverless-http")
    .asInstanceOf[js.Function1[js.Any, js.Any]]

  def main(args: Array[String]): Unit = {
    js.Dynamic.global.lambdaHandler = awsLambdaAdapter(ServerlessApp.unsafeExportApp(app))
  }

  def app = {
    import org.http4s.dsl.io._
    HttpRoutes.of[IO]{
      case GET -> Root / "foo" => Ok()
      case GET -> Root / "bar" => Accepted()
      case GET -> Root / "bad" => Forbidden()
    }.orNotFound
  }

}