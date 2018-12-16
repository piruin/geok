package me.piruin.geok.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.gregwoodfill.assert.`should equal json`
import me.piruin.geok.geometry.MultiPolygon
import me.piruin.geok.geometry.Polygon
import org.amshove.kluent.`should equal`
import org.junit.Test

class MultiPolygonSerializerTest {

    val gson: Gson = GsonBuilder()
            .registerGeokTypeAdapter()
            .create()

    val json = """
        {
          "type": "MultiPolygon",
          "bbox": [100.0, 0.0, 103.0, 3.0],
          "coordinates": [
            [
              [
                [102.0, 2.0],
                [103.0, 2.0],
                [103.0, 3.0],
                [102.0, 3.0],
                [102.0, 2.0]
              ]
            ],
            [
              [
                [100.0, 0.0],
                [101.0, 0.0],
                [101.0, 1.0],
                [100.0, 1.0],
                [100.0, 0.0]
              ]
            ]
          ]
        }
        """.trimIndent()

    @Test
    fun serializeMultiPolygon() {
        val polygons = MultiPolygon(Polygon(
                102.0 to 2.0,
                103.0 to 2.0,
                103.0 to 3.0,
                102.0 to 3.0,
                102.0 to 2.0
        ), Polygon(
                100.0 to 0.0,
                101.0 to 0.0,
                101.0 to 1.0,
                100.0 to 1.0,
                100.0 to 0.0
        ))

        gson.toJson(polygons) `should equal json` json
    }

    @Test
    fun fromJson() {
        gson.parse<MultiPolygon>(json) `should equal` MultiPolygon(Polygon(
                102.0 to 2.0,
                103.0 to 2.0,
                103.0 to 3.0,
                102.0 to 3.0,
                102.0 to 2.0
        ), Polygon(
                100.0 to 0.0,
                101.0 to 0.0,
                101.0 to 1.0,
                100.0 to 1.0,
                100.0 to 0.0
        ))
    }
}
