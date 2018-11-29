package me.piruin.geok.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import me.piruin.geok.geometry.Geometry
import me.piruin.geok.geometry.Point
import me.piruin.geok.geometry.Polygon
import java.lang.reflect.Type

class GeometrySerializer : JsonSerializer<Geometry>, JsonDeserializer<Geometry> {

    override fun serialize(src: Geometry, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return context.serialize(src)
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Geometry {
        val obj = json.asJsonObject
        return when (obj.get("type").asString) {
            "Point" -> context.deserialize(json, typeOf<Point>())
            "Polygon" -> context.deserialize(json, typeOf<Polygon>())
            else -> throw IllegalArgumentException("Not support geometry type")
        }
    }
}