package com.mbcu.mre.exporter.actors

import akka.actor.{Actor, ActorRef, Cancellable, Props}
import akka.dispatch.ExecutionContexts._
import akka.pattern.ask
import akka.util.Timeout
import com.mbcu.mre.exporter.actors.DbActor.{BatchCompleted, StoreAccountTx, parseTransactionItem}
import com.mbcu.mre.exporter.actors.MainActor.{LogBigAccounts, LogRemainder, Shutdown}
import com.mbcu.mre.exporter.actors.WsActor.{SendJs, WsConnected, WsGotText}
import com.mbcu.mre.exporter.models.AccountTx
import com.mbcu.mre.exporter.utils.MyLogging
import com.typesafe.config.ConfigFactory
import play.api.libs.json.{JsValue, Json}

import scala.collection.mutable.ListBuffer
import scala.concurrent.{Await, ExecutionContext, ExecutionContextExecutor, Future}
import scala.concurrent.duration._
import scala.io.Source.fromFile
import scala.util.{Failure, Success}

object MainActor {
  def props(configPath : String): Props = Props(new MainActor(configPath))

  case class Shutdown(code : Int)

  object LogRemainder

  object LogBigAccounts
}

class MainActor(filePath : String) extends Actor with MyLogging{
  var wsActor : Option[ActorRef] = None
  var dbActor : Option[ActorRef] = None
  private var cancellable : Option[Cancellable] = None
  val minLedger : Int = ConfigFactory.load().getInt("data.ledgerIndexMin")
  val maxLedger: Int = ConfigFactory.load().getInt("data.ledgerIndexMax")

  implicit val ec: ExecutionContextExecutor = global
  var accounts : ListBuffer[String] = ListBuffer.empty
  var bigAccounts : ListBuffer[String] = ListBuffer.empty

  override def receive: Receive = {

    case "start" =>
      import scala.io.Source._

      accounts ++= fromFile(filePath).getLines().toSeq
      setupWs()
      setupScheduleLog()
      val db = context.actorOf(Props(new DbActor()), name = "db")
      dbActor = Some(db)
      db ! "start"

    case WsConnected => self ! "start from head"

    case "start from head" =>
      if (accounts.isEmpty){
        self ! Shutdown(0)
      }
      else {

        val acc = accounts.head
        info(s"""Processing $acc""")
        val newAccTx = Json.toJson(new AccountTx(acc, minLedger, maxLedger, None))
        wsActor foreach(_ ! SendJs(newAccTx))
      }

    case WsGotText(text) =>
      val jsValue = Json.parse(text)
      val jsResult = jsValue \ "result"
      val jsTransactions = (jsResult \ "transactions").as[List[JsValue]]
      jsTransactions match {
        case a if a.size > 200 =>
          val acc = DbActor.parseAccount(jsValue)
          bigAccounts += acc
          self ! LogBigAccounts
        case _ => dbActor foreach(_ ! StoreAccountTx(jsValue))
      }


    case BatchCompleted(account, marker) =>
      marker match {
        case Some(m) =>
          val newAccTx = Json.toJson(new AccountTx(account, minLedger, maxLedger, marker))
          wsActor foreach(_ ! SendJs(newAccTx))

        case _ =>
          info(s"""Finished processing : ${accounts.head}""")
          accounts.remove(0)
          self ! "start from head"
      }
    case LogRemainder =>
      val s = accounts mkString "\n"
      info(
        s"""
           |START
           |$s
           |END
         """.stripMargin)

    case LogBigAccounts =>
      val s = bigAccounts mkString "\n"
      info(
        s"""
           |BIG ACCOUNTS
           |$s
           |END
         """.stripMargin)


    case Shutdown(code) =>
      self ! LogBigAccounts
      self ! LogRemainder
      info(s"Stopping application, code $code")
      implicit val executionContext: ExecutionContext = context.system.dispatcher
      context.system.scheduler.scheduleOnce(Duration.Zero)(System.exit(code))
  }

  def setupScheduleLog() : Unit = {
    val scheduleActor = context.actorOf(Props(classOf[ScheduleActor]))
    cancellable =Some(
      context.system.scheduler.schedule(
        10 second,
        600 second,
        scheduleActor,
        LogRemainder))
  }

  def setupWs() : Unit = {
    val url = ConfigFactory.load().getString("rippled.url")
    val ws = context.actorOf(Props(new WsActor(url)), name = "ws")
    wsActor = Some(ws)
    ws ! "start"
  }
}
