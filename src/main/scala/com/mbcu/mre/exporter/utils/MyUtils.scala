package com.mbcu.mre.exporter.utils

import java.time.{Instant, ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatterBuilder

import com.mbcu.mre.exporter.tables.Account
import play.api.libs.json.{JsValue, Json}

object MyUtils {
  val formatter = new DateTimeFormatterBuilder()


  def toZonedDateTime(in : String) : ZonedDateTime = {
    ZonedDateTime.parse(in.toCharArray)
  }


  def toLinuxEpoch(rippleEpoch : Long): Long = rippleEpoch + 946684800


  def toHumanDate(rippleEpoch : Long) : ZonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(toLinuxEpoch(rippleEpoch)), ZoneId.of("Z"))

}

