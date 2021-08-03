package io.chrisdavenport.jstest


import org.scalajs.dom
import org.scalajs.dom.document
import scala.scalajs.js.annotation.JSExportTopLevel

import cats.effect._
import cats.syntax.all._
import io.circe.Json
import org.http4s._
import org.http4s.circe._
import org.http4s.implicits._


object TutorialApp extends IOApp {

  val client = org.http4s.dom.FetchClient[IO]

  def run(args: List[String]): IO[ExitCode] = {
    document.addEventListener("DOMContentLoaded", { (e: dom.Event) =>
      setupUI()
    }) // Side-Effect Otherwise we miss the event

    IO(println("Hi there")) >>
    // setupUIDocument >>
    IO(ExitCode.Success)
  }


  def setupUI(): Unit = {
    val button = document.createElement("button")
    button.textContent = "Click me!"
    button.addEventListener("click", { (e: dom.MouseEvent) =>
      addClickedMessage()
    })
    document.body.appendChild(button)

    appendPar(document.body, "Hello World")
  }

  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    parNode.textContent = text
    targetNode.appendChild(parNode)
  }

  @JSExportTopLevel("addClickedMessage")
  def addClickedMessage(): Unit = {
    import cats.effect.unsafe.implicits.global
    client.expect[Json](Request[IO](Method.GET, uri"./foo"))
      .flatMap(json => 
        json.as[Foo].liftTo[IO].flatMap(foo => 
          IO(appendPar(document.body, s"Decoded: ${foo.toString()} - Raw: ${json.noSpaces}"))
        )
      ).unsafeRunAndForget()
    
  }
}

