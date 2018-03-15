package com.mbcu.mre.exporter.utils

import java.time.ZoneId

import org.scalatest.FunSuite

class MyUtilsTest extends FunSuite {


  test("parsing date into ZonedDateTime") {
    val s = "2018-01-10T23:58:11+00:00"
    val test = MyUtils.toZonedDateTime(s)
    assert(test.getDayOfMonth === 10)
    assert(test.getSecond === 11)
    assert(test.getZone === ZoneId.of("Z"))
  }



}
