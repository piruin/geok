package me.piruin.geok.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.gregwoodfill.assert.`should equal json`
import me.piruin.geok.LatLng
import me.piruin.geok.geometry.Polygon
import org.amshove.kluent.`should be equal to`
import org.junit.Test

class PolygonSerializerTest {

    private val gson: Gson = GsonBuilder()
        .registerGeokTypeAdapter()
        .create()

    @Test
    fun serialize() {
        val polygon = Polygon(
            100.0 to 0.0,
            101.0 to 0.0,
            101.0 to 1.0,
            100.0 to 1.0,
            100.0 to 0.0
        )

        gson.toJson(polygon) `should equal json` """
            {
              "type":"Polygon",
              "bbox":[100.0,0.0,101.0,1.0],
              "coordinates":[
                [
                  [100.0,0.0],
                  [101.0,0.0],
                  [101.0,1.0],
                  [100.0,1.0],
                  [100.0,0.0]
                ]
              ]
            }
        """.trimIndent()
    }

    @Test
    fun deserialize() {
        val polygon = gson.parse<Polygon>(
            """
            {
              "type":"Polygon",
              "bbox":[100.0,0.0,101.0,1.0],
              "coordinates":[
                [
                  [100.0,0.0],
                  [101.0,0.0],
                  [101.0,1.0],
                  [100.0,1.0],
                  [100.0,0.0]
                ]
              ]
            }
            """.trimIndent()
        )

        polygon `should be equal to` Polygon(
            100.0 to 0.0,
            101.0 to 0.0,
            101.0 to 1.0,
            100.0 to 1.0,
            100.0 to 0.0
        )
    }

    @Test
    fun serializeWithHole() {
        val boundary = listOf(
            LatLng(0.0, 100.0),
            LatLng(0.0, 101.0),
            LatLng(1.0, 101.0),
            LatLng(1.0, 100.0),
            LatLng(0.0, 100.0)
        )
        val hole = mutableListOf(
            listOf(
                LatLng(0.2, 100.2),
                LatLng(0.2, 100.8),
                LatLng(0.8, 100.8),
                LatLng(0.8, 100.2),
                LatLng(0.2, 100.2)
            )
        )

        val polygon = Polygon(boundary, hole)

        gson.toJson(polygon) `should equal json` """
            {
                "type":"Polygon",
                "bbox":[100.0,0.0,101.0,1.0],
                "coordinates":[
                    [
                        [100.0,0.0],
                        [101.0,0.0],
                        [101.0,1.0],
                        [100.0,1.0],
                        [100.0,0.0]
                    ],
                    [
                        [100.2,0.2],
                        [100.8,0.2],
                        [100.8,0.8],
                        [100.2,0.8],
                        [100.2,0.2]
                    ]
                ]
            }""".trimWhitespace()
    }

    @Test
    fun deserializeWithHole() {
        val boundary = listOf(
            LatLng(0.0, 100.0),
            LatLng(0.0, 101.0),
            LatLng(1.0, 101.0),
            LatLng(1.0, 100.0),
            LatLng(0.0, 100.0)
        )
        val hole = mutableListOf(
            listOf(
                LatLng(0.2, 100.2),
                LatLng(0.2, 100.8),
                LatLng(0.8, 100.8),
                LatLng(0.8, 100.2),
                LatLng(0.2, 100.2)
            )
        )

        val expected = Polygon(boundary, hole)

        val polygon = gson.parse<Polygon>(
            """
            {
                "type":"Polygon","coordinates":[
                    [
                        [100.0,0.0],
                        [101.0,0.0],
                        [101.0,1.0],
                        [100.0,1.0],
                        [100.0,0.0]
                    ],
                    [
                        [100.2,0.2],
                        [100.8,0.2],
                        [100.8,0.8],
                        [100.2,0.8],
                        [100.2,0.2]
                    ]
                ]
            }""".trimWhitespace()
        )

        polygon!!.boundary `should be equal to` expected.boundary
        polygon.holes[0] `should be equal to` expected.holes[0]
    }
}
