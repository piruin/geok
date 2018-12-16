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
import me.piruin.geok.geometry.MultiPoint
import me.piruin.geok.geometry.Point
import java.lang.reflect.Type

class MultiPointSerializer : JsonSerializer<MultiPoint>, JsonDeserializer<MultiPoint> {

    private val latLngType = typeOf<LatLng>()

    override fun serialize(src: MultiPoint, typeOfSrc: Type, ctx: JsonSerializationContext): JsonElement {
        return JsonObject().apply {
            add("type", JsonPrimitive(src.type))
            add("bbox", ctx.serialize(src.bbox))
            add("coordinates", JsonArray().apply {
                src.geometries.forEach {
                    add(ctx.serialize(it.coordinates))
                }
            })
        }
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, ctx: JsonDeserializationContext): MultiPoint {
        val obj = json.asJsonObject
        val coordinates = obj.getAsJsonArray("coordinates")
        val lineStrings = coordinates.map {
            Point(ctx.deserialize<LatLng>(it, latLngType))
        }
        return MultiPoint(lineStrings)
    }
}
