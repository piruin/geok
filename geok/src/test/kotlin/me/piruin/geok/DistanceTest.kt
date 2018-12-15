package me.piruin.geok

import org.junit.Test

class DistanceTest {

    @Test
    fun testDistance() {
        val latlng1 = LatLng(40.6892, -74.0444)
        val latlng2 = LatLng(39.7802, -74.9453)

        val distance = distanceCalculator()

        distance.between(latlng1, latlng2).shouldEqual(126.74, 0.01)
    }
}
