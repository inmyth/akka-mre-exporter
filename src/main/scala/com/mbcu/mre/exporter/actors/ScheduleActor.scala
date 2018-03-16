package com.mbcu.mre.exporter.actors

import akka.actor.Actor
import com.mbcu.mre.exporter.actors.MainActor.LogRemainder


class ScheduleActor extends Actor {

  override def receive: Receive = {

    case LogRemainder =>
      sender() ! LogRemainder
  }

}