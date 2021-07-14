package io.chrisdavenport.jstest

import cats.effect._
import org.http4s._
import org.http4s.implicits._
import org.http4s.ember.server.EmberServerBuilder

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    EmberServerBuilder.default[IO].withHttpApp(app).build.use(_ => IO.never).as(ExitCode.Success)
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