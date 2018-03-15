package com.mbcu.mre.exporter.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatterBuilder

import com.mbcu.mre.exporter.tables.Account
import play.api.libs.json.{JsValue, Json}

object MyUtils {
  val formatter = new DateTimeFormatterBuilder()


  def toZonedDateTime(in : String) : ZonedDateTime = {
    ZonedDateTime.parse(in.toCharArray)
  }




}
