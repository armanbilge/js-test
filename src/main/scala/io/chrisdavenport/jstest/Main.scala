package io.chrisdavenport.jstest

import cats.effect._
import org.http4s._
import org.http4s.client._
import org.http4s.implicits._
import org.http4s.ember.client.EmberClientBuilder
import cats.effect.std.Console
import _root_.io.circe.Json
// import org.http4s.circe._
object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    EmberClientBuilder.default[IO].build.use(c => 
      getJoke(c).flatMap(Console[IO].println(_))  
    ).as(ExitCode.Success)
  }

  def getJoke(client: Client[IO]): IO[Status] = client.status(Request[IO](Method.GET, uri"https://icanhazdadjoke.com/"))

}