package me.piruin.geok.geometry

import me.piruin.geok.BBox
import me.piruin.geok.shouldBe
import me.piruin.geok.shouldBeEqualTo
import kotlin.test.Test

class MultiPolygonTest {

    @Test
    fun simple() {
        val polygons = MultiPolygon(
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
        )

        polygons.type shouldBeEqualTo "MultiPolygon"
        polygons.bbox shouldBeEqualTo BBox(left = 100.0, bottom = 0.0, right = 103.0, top = 3.0)
        polygons.size shouldBe 2
    }
}
