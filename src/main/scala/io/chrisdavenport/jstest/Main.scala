package io.chrisdavenport.jstest

import cats.effect._
import cats.effect.unsafe.implicits._
import org.http4s._
import org.http4s.implicits._
import org.http4s.node.serverless.ServerlessApp

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.annotation.JSExportTopLevel

object Main {

  @JSImport("serverless-http", JSImport.Namespace)
  @js.native
  val awsLambdaAdapter: js.Function1[js.Any, js.Any] = js.native

  @JSExportTopLevel("lambdaHandler")
  val lambdaHandler = awsLambdaAdapter(ServerlessApp.unsafeExportApp(app))

  def main(args: Array[String]): Unit = ()

  def app = {
    import org.http4s.dsl.io._
    HttpRoutes.of[IO]{
      case GET -> Root / "foo" => Ok()
      case GET -> Root / "bar" => Accepted()
      case GET -> Root / "bad" => Forbidden()
    }.orNotFound
  }

}