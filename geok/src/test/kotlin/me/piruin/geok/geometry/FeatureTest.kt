package me.piruin.geok.geometry

import org.amshove.kluent.`should equal`
import org.junit.Test

class FeatureTest {

    @Test
    fun polygonFeature() {
        val polygon = Polygon(
          100.0 to 0.0,
          101.0 to 0.0,
          101.0 to 1.0,
          100.0 to 1.0,
          100.0 to 0.0
        )

        val feature = Feature(polygon, Properties())

        with(feature) {
            type `should equal` "Feature"
            geometry `should equal` polygon
            properties `should equal` Properties()
        }
    }

    data class Properties(val prob0: String = "value0", val prob1: Double = 0.0)
}
