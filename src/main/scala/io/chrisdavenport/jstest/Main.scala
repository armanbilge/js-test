package io.chrisdavenport.jstest

import cats.effect._
import cats.effect.unsafe.implicits._
import org.http4s._
import org.http4s.implicits._
import org.http4s.node.serverless.ServerlessApp

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object Main {

  @JSImport("serverless-http", JSImport.Namespace)
  @js.native
  val awsLambdaAdapter: js.Function1[js.Any, js.Any] = js.native

  def main(args: Array[String]): Unit =
    js.Dynamic.global.exports.lambdaHandler = awsLambdaAdapter(ServerlessApp.unsafeExportApp(app))

  def app = {
    import org.http4s.dsl.io._
    HttpRoutes.of[IO]{
      case GET -> Root / "hello" / "foo" => Ok()
      case GET -> Root / "hello" / "bar" => Accepted()
      case GET -> Root / "hello" / "bad" => Forbidden()
    }.orNotFound
  }

}