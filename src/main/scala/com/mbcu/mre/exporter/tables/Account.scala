package com.mbcu.mre.exporter.tables

import java.time.ZonedDateTime
import scalikejdbc.{SQLSyntaxSupport, WrappedResultSet}

case class Account(account : String, hash : String, ledgerIndex : Int, date : ZonedDateTime, transaction : String)
object Account extends SQLSyntaxSupport[Account] {

  override val tableName = "account"
  def apply(rs: WrappedResultSet) = new Account(
    rs.string("account"),
    rs.string("hash"),
    rs.int("ledger_index"),
    rs.dateTime("date"),
    rs.string("transaction")
  )
}

