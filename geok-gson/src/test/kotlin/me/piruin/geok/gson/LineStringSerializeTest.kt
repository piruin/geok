package me.piruin.geok.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.gregwoodfill.assert.`should equal json`
import me.piruin.geok.geometry.LineString
import org.amshove.kluent.`should equal`
import org.junit.Test

class LineStringSerializeTest {

    private val gson: Gson = GsonBuilder()
            .registerGeokTypeAdapter()
            .create()

    @Test
    fun serializeLineString() {
        val lineString = LineString(
                100.0 to 0.0,
                101.0 to 1.0
        )

        gson.toJson(lineString) `should equal json` """
            {
              "type": "LineString",
              "coordinates": [
                [100.0, 0.0],
                [101.0, 1.0]
              ]
            }
            """.trimIndent()
    }

    @Test
    fun deserialize() {
        val lineString = gson.parse<LineString>("""
            {
              "type": "LineString",
              "coordinates": [
                [100.0, 0.0],
                [101.0, 1.0]
              ]
            }""".trimIndent())

        lineString `should equal` LineString(
                100.0 to 0.0,
                101.0 to 1.0
        )
    }
}
