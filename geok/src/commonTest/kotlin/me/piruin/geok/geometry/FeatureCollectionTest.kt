package me.piruin.geok.geometry

import me.piruin.geok.BBox
import me.piruin.geok.shouldBeEqualTo
import me.piruin.geok.shouldNotBe
import kotlin.test.Test

class FeatureCollectionTest {

    private val collection = FeatureCollection<Any>(
        Point(
            100.0 to 0.0
        ).toFeature(),
        LineString(
            100.0 to 0.0,
            101.0 to 1.0
        ).toFeature(),
        Polygon(
            100.0 to 0.0,
            101.0 to 0.0,
            101.0 to 1.0,
            100.0 to 1.0,
            100.0 to 0.0
        ).toFeature(),
        MultiPoint(
            Point(100.0 to 0.0),
            Point(101.0 to 1.0)
        ).toFeature(),
        MultiPolygon(
            Polygon(
                102.0 to 2.0,
                103.0 to 2.0,
                103.0 to 3.0,
                102.0 to 3.0,
                102.0 to 2.0
            ),
            Polygon(
                100.0 to 0.0,
                101.0 to 0.0,
                101.0 to 1.0,
                100.0 to 1.0,
                100.0 to 0.0
            )
        ).toFeature()
    )

    @Test
    fun bbox() {
        collection.bbox shouldBeEqualTo BBox(left = 100.0, bottom = 0.0, right = 103.0, top = 3.0)
        collection.features.filterNot { it.geometry is Point }.forEach {
            it.bbox shouldNotBe null
        }
    }
}
