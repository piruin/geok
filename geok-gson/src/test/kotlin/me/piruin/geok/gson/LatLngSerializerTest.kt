package me.piruin.geok.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
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

        gson.toJson(latlng) `should be equal to` "[120.0,10.0]"
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
}
