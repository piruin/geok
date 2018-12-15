package me.piruin.geok

import me.piruin.geok.geometry.PolygonTest
import org.junit.Test

class DistanceTest {

    @Test
    fun testDistance() {
        val latlng1 = LatLng(40.6892, -74.0444)
        val latlng2 = LatLng(39.7802, -74.9453)

        val distance = distanceCalculator()

        distance.between(latlng1, latlng2).shouldEqual(126748.67, 0.01)
    }

    @Test
    fun lineLength() {
        PolygonTest().polygon.boundary.length.shouldEqual(72.07, 0.01)
        PolygonTest().polygon.perimeter.shouldEqual(72.07, 0.01)
    }
}
