package io.chrisdavenport.jstest

import cats.effect._
import cats.syntax.all._
import cats.data._
import org.http4s._
import org.http4s.implicits._
import org.http4s.ember.server.EmberServerBuilder
import fs2._

import java.nio.charset.StandardCharsets
import org.http4s.server.middleware.StaticHeaders
// import org.reflections._
// import org.reflections.scanners.ResourcesScanner
// import java.util.regex.Pattern

object Main extends IOApp { self => 

  def run(args: List[String]): IO[ExitCode] = {
    // IO{
    //   val reflections = new Reflections(new ResourcesScanner())
    //   val resources = reflections.getResources(Pattern.compile(".*\\.js"))
    //   println(resources)
    // } >>
    EmberServerBuilder.default[IO].withHttpApp(StaticHeaders.`no-cache`(app)).build.use(_ => IO.never)
    .as(ExitCode.Success)

  }

  def app = {
    import org.http4s.dsl.request._
    def getResource(pathInfo: String) = IO.delay(self.getClass.getResource(pathInfo))
    
    val foo = {
      import _root_.io.circe.syntax._
      import org.http4s.circe._
      HttpRoutes.of[IO]{
        case GET -> Root / "foo" => Unique[IO].unique.map(t => 
            Response[IO](Status.Ok).withEntity(Foo(t.hash, "Chris").asJson)
          ) 
      }
    }
    
    val static = HttpRoutes.of[IO]{
      case req@GET -> Root => 
          StaticFile.fromResource("/index.html", req.some)
            .getOrElseF(Response[IO](Status.NotFound).pure[IO])
      case req => // Other fun assets
          StaticFile.fromResource[IO](req.pathInfo.renderString, req.some)
          .orElse(OptionT.liftF(getResource(req.pathInfo.renderString)).flatMap(StaticFile.fromURL[IO](_,req.some)))
          .fold(Response[IO](Status.NotFound).pure[IO])(_.pure[IO])
          .flatten
    }
    (foo <+> static).orNotFound
  }


}