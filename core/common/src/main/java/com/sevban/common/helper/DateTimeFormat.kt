package com.sevban.common.helper

import java.time.LocalDate
import java.time.LocalDate.now
import java.time.format.DateTimeFormatter

val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

val LocalDate.dayOfMonthAndName: String
    get() = dayOfMonth.toString() + " " + month.name

fun LocalDate.isToday() = this.isEqual(now())