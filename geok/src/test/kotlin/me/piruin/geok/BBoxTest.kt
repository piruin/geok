package me.piruin.geok

import me.piruin.geok.geometry.Polygon
import org.amshove.kluent.`should be equal to`
import org.junit.Test

class BBoxTest {

    @Test
    fun fromLatLng() {
        val latlng = listOf(
                LatLng(-10.0, -10.0),
                LatLng(10.0, -10.0),
                LatLng(10.0, 10.0),
                LatLng(-10.0, -10.0)
        )
        val bbox = BBox.fromLatLngs(latlng)

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
        val bbox = BBox.fromPolygon(polygon)

        with(bbox) {
            left `should be equal to` -10.0
            bottom `should be equal to` -20.0
            right `should be equal to` 10.0
            top `should be equal to` 20.0
        }
    }
}
