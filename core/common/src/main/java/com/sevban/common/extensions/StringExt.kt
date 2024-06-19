package com.sevban.common.extensions

val String.Companion.EMPTY: String by lazy { "" }

fun String.toTitleCase() = split(" ").joinToString(" ") { it.replaceFirstChar(Char::titlecase) }