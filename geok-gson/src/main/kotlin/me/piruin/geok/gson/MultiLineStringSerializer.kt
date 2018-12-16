package me.piruin.geok.gson

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import me.piruin.geok.LatLng
import me.piruin.geok.geometry.LineString
import me.piruin.geok.geometry.MultiLineString
import java.lang.reflect.Type

class MultiLineStringSerializer : JsonSerializer<MultiLineString>, JsonDeserializer<MultiLineString> {

    private val listLatLngType = typeOf<List<LatLng>>()

    override fun serialize(src: MultiLineString, typeOfSrc: Type, ctx: JsonSerializationContext): JsonElement {
        return JsonObject().apply {
            add("type", JsonPrimitive(src.type))
            add("bbox", ctx.serialize(src.bbox))
            add("coordinates", JsonArray().apply {
                src.lines.forEach {
                    add(ctx.serialize(it.coordinates))
                }
            })
        }
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, ctx: JsonDeserializationContext): MultiLineString {
        val obj = json.asJsonObject
        val coordinates = obj.getAsJsonArray("coordinates")
        val lineStrings = coordinates.map {
            LineString(ctx.deserialize<List<LatLng>>(it, listLatLngType))
        }
        return MultiLineString(lineStrings)
    }
}
