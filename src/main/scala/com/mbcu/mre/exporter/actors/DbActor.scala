package com.mbcu.mre.exporter.actors

import java.sql.Timestamp
import java.time.ZonedDateTime

import akka.Done
import akka.actor.{Actor, ActorRef}
import com.mbcu.mre.exporter.tables.Account
import akka.stream.scaladsl._
import com.mbcu.mre.exporter.actors.DbActor.{BatchCompleted, StoreAccountTx}
import com.mbcu.mre.exporter.models.Marker
import com.mbcu.mre.exporter.utils.{MyLogging, MyUtils}
import org.joda.time.DateTime
import play.api.libs.json.{JsValue, Json}
import scalikejdbc.AutoSession

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scalikejdbc._


object DbActor {

  case class StoreAccountTx(raw : String)

  case class BatchCompleted(account : String, nextMarker : Option[Marker])

  def toAccTxBatchParams(ins : Seq[Account]) : Seq[Seq[Any]] = {
    //account, hash, ledger_index, date, human_date, transaction_type, txn_account, transaction
    ins.map(d => Seq(d.account, d.hash, d.ledgerIndex, d.date, d.humanDate, d.txnType, d.txnAcc, d.transaction))
  }

  def parseAccountTx(jsValue : JsValue) : Seq[Account] = {

    var res : Seq[Account] = Seq.empty[Account]
    val account = parseAccount(jsValue)
    val jsResult = jsValue \ "result"
    val jsTransactions = (jsResult \ "transactions").as[List[JsValue]]
    for (jsTransaction <- jsTransactions) {
      res +:= parseTransactionItem(jsTransaction)(account)
    }
    res
  }

  def parseAccount(jsValue : JsValue) : String = {
    val jsResult = jsValue \ "result"
    val account = (jsResult \ "account").as[String]
    account
  }

  def parseMarker(jsValue : JsValue) : Option[Marker] = {
    var res : Option[Marker] = None
    val jsResult = jsValue \ "result"
    if ((jsResult \ "marker").isDefined) res = (jsResult \ "marker").asOpt[Marker]
    res
  }

  def parseTransactionItem(t : JsValue)(account : String) : Account = {
    val tx = t \ "tx"
    val hash = (tx \ "hash").as[String]
    val txnAccount = (tx \ "Account").as[String]
    val date = (tx \ "date").as[Long]
    val humanDate = MyUtils.toHumanDate(date)
    val ledgerIndex = (tx \ "ledger_index").as[Int]
    val txnType = (tx \ "TransactionType").as[String]
    val transaction = t.toString()
    Account(account, hash, ledgerIndex, date, humanDate, txnType, txnAccount, transaction)
  }


}
class DbActor extends Actor with MyLogging{

implicit val session: AutoSession.type = AutoSession
var mainActor : Option[ActorRef] = None

  override def receive: Receive = {
    case "start" => mainActor = Some(sender())

    case StoreAccountTx(raw) =>
      val jsValue = Json.parse(raw)
      val account = DbActor.parseAccount(jsValue)
      val marker = DbActor.parseMarker(jsValue)
      val data = DbActor.toAccTxBatchParams(DbActor.parseAccountTx(jsValue))

      sql"""insert into main.account (account, hash, ledger_index, date, human_date, transaction_type, txn_account, transaction)
           values (?,?,?,?,?,?,?,?)
           ON DUPLICATE KEY UPDATE hash = hash"""
        .batch(data:_*)
        .apply()
      mainActor foreach(_ ! BatchCompleted(account, marker))

    case "select all" =>
      val accounts: List[Account] = sql"select * from account".map(rs => Account(rs)).list.apply()
      accounts.foreach(a => info(a.toString))
  }


}
