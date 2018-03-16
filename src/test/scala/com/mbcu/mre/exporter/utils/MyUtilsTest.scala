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


  test("ripple epoch to date") {
    val t = 569937733L
    val res = MyUtils.toHumanDate(t)
    assert(res.getYear === 2018)
    assert(res.getMonthValue === 1)
    assert(res.getDayOfMonth === 22)
    assert(res.getSecond === 13)
    assert(res.getZone === ZoneId.of("Z"))
    assert(res.toString === "2018-01-22T12:02:13Z")
  }
}
