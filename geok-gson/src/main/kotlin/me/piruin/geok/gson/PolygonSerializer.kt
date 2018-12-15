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
import me.piruin.geok.geometry.Polygon
import java.lang.reflect.Type

class PolygonSerializer : JsonSerializer<Polygon>, JsonDeserializer<Polygon> {

    private val listLatLngType = typeOf<List<LatLng>>()

    override fun serialize(src: Polygon, typeOfSrc: Type, ctx: JsonSerializationContext): JsonElement {
        return JsonObject().apply {
            add("type", JsonPrimitive(src.type))
            add("bbox", ctx.serialize(src.bbox))
            add("coordinates", JsonArray().apply {
                add(ctx.serialize(src.boundary))
                src.holes.forEach { add(ctx.serialize(it)) }
            })
        }
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, ctx: JsonDeserializationContext): Polygon {
        val obj = json.asJsonObject
        val coordinates = obj.getAsJsonArray("coordinates")

        var polygon: Polygon? = null
        coordinates.forEachIndexed { index, jsonElement ->
            when (index) {
                0 -> polygon = Polygon(ctx.deserialize<MutableList<LatLng>>(jsonElement, listLatLngType))
                else -> polygon?.holes?.add(ctx.deserialize<MutableList<LatLng>>(jsonElement, listLatLngType))
            }
        }
        return polygon!!
    }
}
