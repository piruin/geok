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
import com.gregwoodfill.assert.`should equal json`
import me.piruin.geok.LatLng
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should equal`
import org.junit.Test

class LatLngSerializerTest {

    private val gson: Gson = GsonBuilder()
        .adapterFor<LatLng>(LatLngSerializer())
        .create()

    @Test
    fun serialiazeLatLng() {
        val latlng = LatLng(10.0, 120.0)

        gson.toJson(latlng) `should equal json` "[120.0,10.0]"
    }

    @Test
    fun serializeLatLng2() {
        val latlng = LatLng(0.5, 102.5)

        gson.toJson(latlng) `should be equal to` "[102.5,0.5]"
    }

    @Test
    fun deserialize() {
        val deserialize = gson.parse<LatLng>("[120.0,10.0]")!!

        deserialize `should equal` LatLng(10.0, 120.0)
    }

    @Test
    fun deserialize2() {
        val deserialize = gson.parse<LatLng>("[102.5,0.5]")!!

        deserialize `should equal` LatLng(0.5, 102.5)
    }

    @Test
    fun serializeLatLngList() {
        val latlngList = arrayListOf(
            LatLng(0.0, 100.0),
            LatLng(1.0, 101.0)
        )

        gson.toJson(latlngList) `should equal json` "[[100.0,0.0],[101.0,1.0]]"
    }

    @Test
    fun deserializeLatLngList() {
        val deserialize = gson.parse<List<LatLng>>("[[100.0,0.0],[101.0,1.0]]")

        deserialize `should equal` arrayListOf(
            LatLng(0.0, 100.0),
            LatLng(1.0, 101.0)
        )
    }

    @Test
    fun serializeObjWithLatLgn() {
        val place = Place("NECTEC", LatLng(14.07776, 100.601282))

        gson.toJson(place) `should equal json` """
            {
                "name":"NECTEC",
                "coordinates":[100.601282,14.07776]
            }
        """.trimIndent()
    }

    @Test
    fun serializeObjWithNullLatLgn() {
        val place = Place("NECTEC")

        gson.toJson(place) `should equal` """{"name":"NECTEC"}"""
    }

    class Place(val name: String, val coordinates: LatLng? = null)
}
