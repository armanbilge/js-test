package io.chrisdavenport.jstest

import io.circe._
import io.circe.syntax._

case class Foo(
  i: Int,
  name: String
)
object Foo {
  implicit val e : Encoder[Foo] = Encoder.instance[Foo](f => 
      Json.obj(
        "i" -> f.i.asJson,
        "name" -> f.name.asJson
      )
    )

  implicit val d: Decoder[Foo] = Decoder.instance[Foo]{ h => 
    for {
      i <- h.downField("i").as[Int]
      name <- h.downField("name").as[String]
    } yield Foo(i, name)
  }

}