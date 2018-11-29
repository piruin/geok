package me.piruin.geok.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder

fun String.trimWhitespace() = replace("\\s".toRegex(), "")

internal inline fun <reified T> Gson.parse(json: String): T? = fromJson(json, typeOf<T>())

internal inline fun <reified T> GsonBuilder.adapterFor(adapter: Any): GsonBuilder {
    return registerTypeAdapter(typeOf<T>(), adapter)
}
