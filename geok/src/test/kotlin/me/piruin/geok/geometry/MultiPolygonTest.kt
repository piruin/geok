package me.piruin.geok.geometry

import me.piruin.geok.BBox
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.junit.Test

class MultiPolygonTest {

    @Test
    fun simple() {
        val polygons = MultiPolygon(Polygon(
                102.0 to 2.0,
                103.0 to 2.0,
                103.0 to 3.0,
                102.0 to 3.0,
                102.0 to 2.0
        ), Polygon(
                100.0 to 0.0,
                101.0 to 0.0,
                101.0 to 1.0,
                100.0 to 1.0,
                100.0 to 0.0
        ))

        polygons.type `should be equal to` "MultiPolygon"
        polygons.bbox `should equal` BBox(left = 100.0, bottom = 0.0, right = 103.0, top = 3.0)
        polygons.geometries.size `should be` 2
    }
}
