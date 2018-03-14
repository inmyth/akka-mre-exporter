package com.mbcu.mre.exporter.actors

import akka.Done
import akka.actor.Actor
import akka.stream.alpakka.slick.scaladsl.{Slick, SlickSession}
import akka.stream.scaladsl._
import com.mbcu.mre.exporter.models.Account
import slick.jdbc.GetResult
import slick.lifted.TableQuery

import scala.concurrent.Future


class DbActor extends Actor {

  override def receive: Receive = {
    case "start" =>
      implicit val session = SlickSession.forConfig("slick-mysql")
//      implicit val getUserResult = GetResult(r => User(r.nextInt, r.nextString))
      val accs = TableQuery[Account]
      val done: Future[Done] =
        Slick
          .source(sql"SELECT ID, NAME FROM ALPAKKA_SLICK_SCALADSL_TEST_USERS".as[Account])
          .log("user")
          .runWith(Sink.ignore)
  }

}
