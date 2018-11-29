/*
 * Copyright (c) 2018 Piruin Panichphol
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package me.piruin.geok.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.piruin.geok.geometry.Feature
import me.piruin.geok.geometry.FeatureCollection
import me.piruin.geok.geometry.Point
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should equal`
import org.junit.Test

class FeatureSerializerTest {

    private val gson: Gson = GsonBuilder()
            .registerGeokTypeAdapter()
            .create()

    @Test
    fun featureToJson() {
        val feature = Feature(
                Point(14.07776, 100.601282),
                People("JohnSnow", 15)
        )

        gson.toJson(feature) `should be equal to` """
    {"type":"Feature",
    "geometry": {"type":"Point", "coordinates":[100.601282,14.07776] },
    "properties":{"name":"JohnSnow","age":15}}
""".trimWhitespace()
    }

    @Test
    fun featureCollectionToJson() {
        val collection = FeatureCollection<People>()
        collection.features.apply {
            add(Feature(Point(14.07776, 100.601282), People("John", 15)))
            add(Feature(Point(14.08101, 100.620148), People("Arya", 13)))
        }

        gson.toJson(collection) `should be equal to` """
{
    "type":"FeatureCollection","features":[
        {  "type":"Feature",
          "geometry":{"type":"Point","coordinates":[100.601282,14.07776] },
          "properties":{"name":"John","age":15}
        },
        { "type":"Feature",
          "geometry":{"type":"Point","coordinates":[100.620148,14.08101]},
          "properties":{"name":"Arya","age":13}
        }
    ]
}
            """.trimWhitespace()
    }

    @Test
    fun PointFromJson() {
        val json = """
{  "type":"Feature",
  "geometry":{ "coordinates":[100.601282,14.07776], "type":"Point"},
  "properties":{"name":"John Snow","age":15}}
""".trimIndent()

        val feature = gson.parse<Feature<People>>(json)!!

        feature.geometry `should equal` Point(14.07776, 100.601282)
    }

    class People(val name: String, val age: Int)
}
