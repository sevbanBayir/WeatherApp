package com.sevban.common.extensions

fun String.toTitleCase() = split(" ").joinToString(" ") { it.replaceFirstChar(Char::titlecase) }