package com.mbcu.mre.exporter.models

import play.api.libs.json._
import play.api.libs.functional.syntax._


object  Marker {
  implicit val jsonFormat: OFormat[Marker] = Json.format[Marker]

  object Implicits {
    implicit val markerWrites: Writes[Marker] {
      def writes(a: Marker): JsValue
    } = new Writes[Marker] {
      def writes(a: Marker): JsValue = Json.obj(
        "ledger" -> a.ledger,
        "seq" -> a.seq
      )
    }

    implicit val markerReads: Reads[Marker] = (
      (JsPath \ "leger").read[Int] and
      (JsPath \ "seq").read[Int]
    ) (Marker.apply _)
  }
}
case class Marker (ledger: Int, seq : Int)


object AccountTx {

  implicit val jsonFormat: OFormat[AccountTx] = Json.format[AccountTx]
  /*

  {
    "id": 1,
    "command": "account_tx",
    "account": "r9cZA1mLK5R5Am25ArfXFmqgNwjZgnfk59",
    "ledger_index_min": -1,
    "ledger_index_max": -1,
    "binary": false,
    "count": false,
    "limit": 10,
    "forward": false
  }
   */
  object Implicits {
    implicit val accountTxWrites: Writes[AccountTx] {
      def writes(a: AccountTx): JsValue
    } = new Writes[AccountTx] {
      def writes(a: AccountTx): JsValue = Json.obj(
        "account" -> a.account,
        "ledger_index_min" -> a.ledger_index_min,
        "ledger_index_max" -> a.ledger_index_max,
        "marker" -> a.marker,
        "limit" -> a.limit,
        "forward" -> a.forward,
        "binary" -> a.binary,
        "command" -> a.command

      )
    }

    implicit val accountTxReads: Reads[AccountTx] = (
        (JsPath \ "account").read[String] and
        (JsPath \ "ledger_index_min").read[Int] and
        (JsPath \ "ledger_index_max").read[Int] and
        (JsPath \ "marker").readNullable[Marker] and
        (JsPath \ "limit").read[Int] and
        (JsPath \ "forward").read[Boolean] and
        (JsPath \ "binary").read[Boolean] and
        (JsPath \ "command").read[String]

      ) (AccountTx.apply _)
  }

}
case class AccountTx (
                     account : String,
                     ledger_index_min : Int,
                     ledger_index_max : Int,
                     marker : Option[Marker],
                     limit : Int = 100,
                     forward : Boolean = false,
                     binary : Boolean = false,
                     command : String = "account_tx"
                     )



