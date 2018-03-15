package com.mbcu.mre.exporter.actors

import java.sql.Timestamp
import java.time.ZonedDateTime

import akka.Done
import akka.actor.Actor
import com.mbcu.mre.exporter.tables.Account
import akka.stream.scaladsl._
import org.joda.time.DateTime
import play.api.libs.json.{JsValue, Json}
import scalikejdbc.AutoSession

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scalikejdbc._


object DbActor {

  def parseAccountTx(raw :String) : Seq[Account] = {
    var res : Seq[Account] = Seq.empty[Account]
    val jsValue : JsValue = Json.parse(raw)
    val jsResult = jsValue \ "result"
    val account = (jsResult \ "account").as[String]
    res +:= new Account(account, "bb", 7, 57745634, ZonedDateTime.now(), "Payment", "r3PDtZSa5LiYp1Ysn1vMuMzB59RzV3W9QH", "aaaa")
    val jsTransactions = (jsResult \ "transactions").as[List[JsValue]]
    for (jsTransaction <- jsTransactions) {
      val hash = ((jsTransaction \ "tx") \ "hash").as[String]
      println(hash)
    }


    res

  }
}
class DbActor extends Actor {

implicit val session: AutoSession.type = AutoSession

  override def receive: Receive = {
    case "start" =>


      val account = "ee"
      val hash = "bb"
      val ledgerIndex = 6
      val date = ZonedDateTime.now
      val transaction = "adasdasd"
      val data = Seq(
        Seq('account -> "ff", 'hash -> "gg", 'ledgerIndex -> 8, 'date -> ZonedDateTime.now, 'transaction -> "dggdfgf"),
        Seq('account -> "uu", 'hash -> "kk", 'ledgerIndex -> 9, 'date -> ZonedDateTime.now, 'transaction -> "ikgj")
      )
      val c = Account.column
//      sql"""insert into ${Account.table} (${c.account}, ${c.hash}, ${c.ledgerIndex}, ${c.date}, ${c.transaction})
//           values ($account, $hash, $ledgerIndex, $date, $transaction)
//           ON DUPLICATE KEY UPDATE ${c.hash} = ${c.hash}"""
//        .update.apply()
//      sql"""insert into main.account (${c.account}, ${c.hash}, ${c.ledgerIndex}, ${c.date}, ${c.transaction})
//           values ($account, $hash, $ledgerIndex, $date, $transaction)
//           ON DUPLICATE KEY UPDATE ${c.hash} = ${c.hash}"""
//        .batchByName(data:_*)
//        .apply()
      val data2 = Seq(
         Seq("ff", "gg", 8, 411616880, ZonedDateTime.now(), "OfferCreate", "r9cZA1mLK5R5Am25ArfXFmqgNwjZgnfk59", "blablabla"),
         Seq("uu", "ll", 9, 411616790, ZonedDateTime.now(), "OfferCreate", "r3PDtZSa5LiYp1Ysn1vMuMzB59RzV3W9QH", "yfhgyfhgfhg")
      )

      sql"""insert into main.account (account, hash, ledger_index, date, human_date, transaction_type, txn_account, transaction)
           values (?,?,?,?,?,?,?,?)
           ON DUPLICATE KEY UPDATE hash = hash"""
        .batch(data2:_*)
        .apply()

//      withSQL {
//        insert.into(Account).namedValues(c.account -> sqls.?, c.hash -> sqls.?,c.ledgerIndex -> sqls.?, c.date -> sqls.?, c.transaction -> sqls.?)
//      }.batch(data: _*).apply()
//      withSQL {
//        insert.into(Account).values("aa", "dd", 5, ZonedDateTime.now)
//      }.update.apply()


      val accounts: List[Account] = sql"select * from account".map(rs => Account(rs)).list.apply()
      accounts.foreach(println)

  }


}
