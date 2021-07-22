package me.piruin.geok

import me.piruin.geok.geometry.Polygon
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import kotlin.test.Test

class BBoxTest {

    @Test
    fun fromLatLng() {
        val latlng = listOf(
            LatLng(-10.0, -10.0),
            LatLng(10.0, -10.0),
            LatLng(10.0, 10.0),
            LatLng(-10.0, -10.0)
        )
        val bbox = BBox.from(latlng)

        with(bbox) {
            left `should be equal to` -10.0
            bottom `should be equal to` -10.0
            right `should be equal to` 10.0
            top `should be equal to` 10.0
        }
    }

    @Test
    fun fromPolygon() {
        val polygon = Polygon(
            LatLng(-20.0, -10.0),
            LatLng(20.0, -10.0),
            LatLng(20.0, 10.0),
            LatLng(-20.0, -10.0)
        )
        val bbox = BBox.from(polygon.boundary)

        with(bbox) {
            left `should be equal to` -10.0
            bottom `should be equal to` -20.0
            right `should be equal to` 10.0
            top `should be equal to` 20.0
        }
    }

    @Test
    fun contains() {
        val bbox = BBox.from(LatLng(-20.0 to -10.0) to LatLng(20.0 to 10.0))

        bbox.contains(0 to 0) `should be` true
        bbox.contains(-20.0 to -10.0) `should be` true
        bbox.contains(20.0000000000 to 10.000000000) `should be` true

        bbox.contains(20.0000000009 to 9.9987871245) `should be` false
    }
}
