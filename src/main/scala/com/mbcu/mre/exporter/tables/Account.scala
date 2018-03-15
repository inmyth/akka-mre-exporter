package com.mbcu.mre.exporter.tables

import java.time.ZonedDateTime
import scalikejdbc.{SQLSyntaxSupport, WrappedResultSet}

case class Account(account : String, hash : String, ledgerIndex : Int, date : Long, humanDate : ZonedDateTime, txnType : String, txnAcc : String, transaction : String)
object Account extends SQLSyntaxSupport[Account] {

  override val tableName = "account"
  def apply(rs: WrappedResultSet) = new Account(
    rs.string("account"),
    rs.string("hash"),
    rs.int("ledger_index"),
    rs.long("date"),
    rs.dateTime("human_date"),
    rs.string("transaction_type"),
    rs.string("txn_account"),
    rs.string("transaction"),
  )
}

