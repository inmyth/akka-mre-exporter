package com.mbcu.mre.exporter.actors

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.mbcu.mre.exporter.Application.{file, system}
import com.mbcu.mre.exporter.actors.WsActor.WsConnected
import com.typesafe.config.ConfigFactory
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.io.Source.fromFile
import scala.util.{Failure, Success}

object MainActor {
  def props(configPath : String): Props = Props(new MainActor(configPath))


}

class MainActor(filePath : String) extends Actor {
  var wsActor : Option[ActorRef] = None
  var dbActor : Option[ActorRef] = None

  var accounts : Seq[String] = Seq.empty

  override def receive: Receive = {

    case "start" =>
      import scala.io.Source._
      val addresses = fromFile(filePath).getLines().toSeq
//      setupWs()
      val db = context.actorOf(Props(new DbActor()), name = "db")
      db ! "start"



  }

  def parse() : Unit = {
    val jsValue : JsValue = Json.parse("")

  }

  def setupWs() : Unit = {
    val url = ConfigFactory.load().getString("rippled.url")
    val ws = context.actorOf(Props(new WsActor(url)), name = "ws")
    wsActor = Some(ws)
    implicit val timeout = Timeout(5 seconds)
    val fws = ws ? "start"
    val result = Await.result(fws, timeout.duration)
    result match {
      case WsConnected => println("connected")
      case _ => println("connect fail")
    }
  }
}
