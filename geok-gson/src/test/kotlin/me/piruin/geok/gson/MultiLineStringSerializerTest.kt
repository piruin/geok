package me.piruin.geok.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.gregwoodfill.assert.`should equal json`
import me.piruin.geok.geometry.LineString
import me.piruin.geok.geometry.MultiLineString
import org.amshove.kluent.`should equal`
import org.junit.Test

class MultiLineStringSerializerTest {

    val gson: Gson = GsonBuilder()
        .registerGeokTypeAdapter()
        .create()

    val json = """
        {
          "type": "MultiLineString",
          "coordinates": [
            [ [100.0, 0.0], [101.0, 1.0] ],
            [ [102.0, 2.0], [103.0, 3.0] ]
          ]
        }
    """.trimIndent()

    @Test
    fun serialize() {
        val lines = MultiLineString(
            LineString(100.0 to 0.0, 101.0 to 1.0),
            LineString(102.0 to 2.0, 103.0 to 3.0)
        )

        gson.toJson(lines) `should equal json` json
    }

    @Test
    fun deserialize() {
        gson.parse<MultiLineString>(json) `should equal` MultiLineString(
            LineString(100.0 to 0.0, 101.0 to 1.0),
            LineString(102.0 to 2.0, 103.0 to 3.0)
        )
    }
}
