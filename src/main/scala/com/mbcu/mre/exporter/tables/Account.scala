package com.mbcu.mre.exporter.tables


import java.sql.Timestamp

import org.joda.time.DateTime
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


class Account(tag : Tag) extends Table[(String, String, Int, Timestamp)](tag, "ACCOUNT"){
  def id = column[String]("Account", O.PrimaryKey) // This is the primary key column
  def hash = column[String]("hash", O.PrimaryKey)
  def ledgerIndex = column[Int]("ledger_index")
  def date = column[Timestamp]("date")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id, hash, ledgerIndex, date)
}

