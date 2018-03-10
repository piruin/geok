package me.piruin.geok.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.parse(json: String): T? = this.fromJson(json, object : TypeToken<T>() {}.type)

inline fun <reified T> GsonBuilder.adapterFor(adapter: Any): GsonBuilder {
    return this.registerTypeAdapter(object : TypeToken<T>() {}.type, adapter)
}
