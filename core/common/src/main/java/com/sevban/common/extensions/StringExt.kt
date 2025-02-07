package com.sevban.common.extensions

import com.sevban.common.helper.dateFormatter
import com.sevban.common.helper.timeFormatter
import java.time.LocalDate
import java.time.LocalDateTime

val String.Companion.EMPTY: String by lazy { "" }

fun String.toTitleCase() = split(" ").joinToString(" ") { it.replaceFirstChar(Char::titlecase) }

fun String.toLocalDate(): LocalDate = LocalDateTime.parse(this, dateFormatter).toLocalDate()

fun String.toLocalDateTime(): LocalDateTime = LocalDateTime.parse(this, dateFormatter)

fun String.toHourAndMinute(): String =
    LocalDateTime.parse(this, dateFormatter).format(timeFormatter)