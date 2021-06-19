package me.piruin.geok

import org.amshove.kluent.`should be equal to`
import org.junit.Test

class DistanceTest {

    @Test
    fun distanceCalulator() {
        val latlng1 = LatLng(40.6892, -74.0444)
        val latlng2 = LatLng(39.7802, -74.9453)

        val distance = distanceCalculator()

        distance.between(latlng1, latlng2) `should be equal to` 126748.67058978828
    }

    @Test
    fun latlngCollectionDistance() {
        val latlngs = mutableListOf(
            LatLng(16.4268129901041, 102.8380009059),
            LatLng(16.4266819930293, 102.8379568936),
            LatLng(16.4267047695460, 102.8378494011),
            LatLng(16.4268502721458, 102.8378330329),
            LatLng(16.4268937418855, 102.8378020293),
            LatLng(16.4268129901041, 102.8380009059)
        )

        latlngs.distance `should be equal to` latlngs.length
        latlngs.distance `should be equal to` 72.07422073608048
        latlngs.open().distance `should be equal to` 56.83458697665041
        latlngs.close().distance `should be equal to` 72.07422073608048
    }
}
