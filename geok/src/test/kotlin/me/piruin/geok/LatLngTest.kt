package me.piruin.geok

import org.amshove.kluent.`should equal`
import org.junit.Test

class LatLngTest {

    @Test(expected = AssertionError::class)
    fun invalidLat() {
        LatLng(-90.1, 0.0)
    }

    @Test(expected = AssertionError::class)
    fun invalidLng() {
        LatLng(0.0, -180.1)
    }

    @Test
    fun createLatLng() {
        LatLng(-90.0, -180.0)
        LatLng(-90.0, 180.0)
        LatLng(90.0, 180.0)
        LatLng(90.0, -180.0)
    }

    @Test
    fun toUtm() {
        LatLng(16.423976, 102.841838).toUtm() `should equal` Utm(48, 'N', 269542.0, 1817061.8)
    }
}
