package com.mbcu.mre.exporter

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.mbcu.mre.exporter.actors.MainActor
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import scalikejdbc._
import scalikejdbc.config.DBs
import scala.concurrent.Await

object Application extends App {

  import akka.actor.Props

  implicit val system: ActorSystem = akka.actor.ActorSystem("dumper")
  implicit val materializer: ActorMaterializer = akka.stream.ActorMaterializer()

  val file = "./trade_wallet_addresses.txt"
//  implicit val timeout = Timeout(3 seconds)
  DBs.setupAll()
  val mainActor = system.actorOf(Props(new MainActor(file)), name = "main")
  mainActor ! "start"
//  val fFile = mainActor ? "read all addresses"
//  val result = Await.result(fFile, timeout.duration).asInstanceOf[Seq[String]]
//  result.foreach(println)


}
