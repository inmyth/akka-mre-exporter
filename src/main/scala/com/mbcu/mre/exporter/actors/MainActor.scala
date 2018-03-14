package com.mbcu.mre.exporter.actors

import akka.actor.{Actor, Props}

object MainActor {
  def props(configPath : String): Props = Props(new MainActor(configPath))

}

class MainActor(filePath : String) extends Actor {

  override def receive: Receive = {

    case "start" =>
      self ! "read all addresses"

    case "read all addresses" =>
      import scala.io.Source._
      val addresses = fromFile(filePath).getLines().toSeq
      addresses.foreach(println)

  }
}
