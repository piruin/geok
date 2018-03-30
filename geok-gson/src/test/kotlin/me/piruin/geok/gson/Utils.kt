package me.piruin.geok.gson

fun String.trimWhitespace() = replace("\\s".toRegex(), "")
