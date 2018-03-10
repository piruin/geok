package me.piruin.geok.gson

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import me.piruin.geok.LatLng
import java.lang.reflect.Type

class LatLngSerializer : JsonSerializer<LatLng>, JsonDeserializer<LatLng> {

    override fun serialize(src: LatLng?,
                           typeOfSrc: Type?,
                           context: JsonSerializationContext?): JsonElement {
        return JsonArray(2).apply {
            add(src?.longitude)
            add(src?.latitude)
        }
    }

    override fun deserialize(json: JsonElement?,
                             typeOfT: Type?, context:
                             JsonDeserializationContext?): LatLng? {
        val jsonArray = json?.asJsonArray
        return when (jsonArray) {
            null -> null
            else -> LatLng(jsonArray[1].asDouble, jsonArray[0].asDouble)
        }
    }
}
