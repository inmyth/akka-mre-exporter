package com.mbcu.mre.exporter

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.mbcu.mre.exporter.actors.MainActor

object Application extends App {

  import akka.actor.Props

  implicit val system: ActorSystem = akka.actor.ActorSystem("dumper")
  implicit val materializer: ActorMaterializer = akka.stream.ActorMaterializer()

  val file = "./trade_wallet_addresses.txt"

  val mainActor = system.actorOf(Props(new MainActor(file)), name = "main")
  mainActor ! "start"


}
