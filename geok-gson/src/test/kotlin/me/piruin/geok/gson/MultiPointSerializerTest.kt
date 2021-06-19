package me.piruin.geok.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.gregwoodfill.assert.`should equal json`
import me.piruin.geok.geometry.MultiPoint
import me.piruin.geok.geometry.Point
import org.amshove.kluent.`should be equal to`
import org.junit.Test

class MultiPointSerializerTest {

    private val gson: Gson = GsonBuilder()
        .registerGeokTypeAdapter()
        .create()

    private val json = """
        {
            "type": "MultiPoint",
            "coordinates": [
                [100.0, 0.0], [101.0, 1.0]
            ]
        }
    """.trimIndent()

    @Test
    fun serialize() {
        val points = MultiPoint(
            Point(100.0 to 0.0),
            Point(101.0 to 1.0)
        )

        gson.toJson(points) `should equal json` json
    }

    @Test
    fun deserialize() {
        gson.parse<MultiPoint>(json) `should be equal to` MultiPoint(
            Point(100.0 to 0.0),
            Point(101.0 to 1.0)
        )
    }
}
