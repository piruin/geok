package me.piruin.geok

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import kotlin.math.abs
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class IntersectionTest {

    val EPSILON = 0.000001

    @Test
    fun crossProduct() {
        val l = ArrayList<LatLng>()
        l.add(LatLng(0 to 0))
        l.add(LatLng(1 to 1))
        for (i in 0..49) {
            l.add(LatLng(Random.nextDouble() to Random.nextDouble()))
        }

        for (p1 in l) {
            for (p2 in l) {
                val r1: Double = (p1 to p2).crossProduct()
                val r2: Double = (p2 to p1).crossProduct()
                val isAntisymmetric: Boolean = abs(r1 + r2) < EPSILON
                assertEquals(true, isAntisymmetric, "[ $p1, $p2]")
            }
        }
    }

    @Test
    fun intersectionPoint() {
        val line1 = LatLng(0 to 2) to LatLng(2 to 0)
        val line2 = LatLng(0 to 0) to LatLng(2 to 2)
        val line3 = LatLng(1 to 0) to LatLng(1 to 2)
        val line4 = LatLng(0 to 1) to LatLng(2 to 1)

        line1 segmentIntersectionWith line2 `should be equal to` (LatLng(1 to 1) to LatLng(1 to 1))
        line1 segmentIntersectionWith line3 `should be equal to` (LatLng(1 to 1) to LatLng(1 to 1))
        line1 segmentIntersectionWith line4 `should be equal to` (LatLng(1 to 1) to LatLng(1 to 1))
        line3 segmentIntersectionWith line4 `should be equal to` (LatLng(1 to 1) to LatLng(1 to 1))
    }

    @Test
    fun intersectionPointNew() {
        val line1 = LatLng(0 to 2) to LatLng(2 to 0)
        val line2 = LatLng(0 to 0) to LatLng(2 to 2)
        val line3 = LatLng(1 to 0) to LatLng(1 to 2)
        val line4 = LatLng(0 to 1) to LatLng(2 to 1)

        line1.intersectionWith(line2).first `should be equal to` LatLng(1 to 1)
        line1.intersectionWith(line3).first `should be equal to` LatLng(1 to 1)
        line1.intersectionWith(line4).first `should be equal to` LatLng(1 to 1)
        line2.intersectionWith(line3).first `should be equal to` LatLng(1 to 1)
        line2.intersectionWith(line4).first `should be equal to` LatLng(1 to 1)
        line3.intersectionWith(line4).first `should be equal to` LatLng(1 to 1)
    }

    @Test
    fun intersectionPointNewNotSegment() {
        val line1 = LatLng(-2 to 4) to LatLng(0 to 2)
        val line2 = LatLng(-2 to -2) to LatLng(0 to 0)

        line1.intersectionWith(line2).first `should be equal to` LatLng(1 to 1)
    }

    @Test
    fun intersectionPointOfParallel() {
        val line1 = LatLng(0.0 to 2.0) to LatLng(2.0 to 2.0)
        val line2 = LatLng(0.0 to 0.0) to LatLng(2.0 to 0.0)

        line1 segmentIntersectionWith line2 `should be` null
    }

    @Test
    fun lineIntersectionPointsWithPolygon() {
        val boundary = listOf(
            LatLng(0.0 to 0.0),
            LatLng(0.0 to 2.0),
            LatLng(2.0 to 2.0),
            LatLng(2.0 to 0.0),
            LatLng(0.0 to 0.0),
        )
        val line = LatLng(-1.0 to -1.0) to LatLng(3.0 to 3.0)
        val expected = listOf(LatLng(0.0 to 0.0), LatLng(2.0 to 2.0))

        line intersectionsWith boundary `should be equal to` expected
    }

    @Test
    fun intersectionPointPolygon() {
        val polygon1 = listOf(
            LatLng(0 to 1),
            LatLng(1 to 2),
            LatLng(3 to 2),
            LatLng(4 to 1),
            LatLng(3 to 0),
            LatLng(1 to 0),
        )

        val polygon2 = listOf(
            LatLng(2 to 1),
            LatLng(3 to 1.9),
            LatLng(3.9 to 1),
            LatLng(3 to 0.1)
        )

        polygon1 intersectionsWith polygon2 `should be equal to` listOf(
            LatLng(2 to 1),
            LatLng(3 to 1.9),
            LatLng(3.9 to 1),
            LatLng(3 to 0.1),
        )
    }

    @Test
    fun intersectionPointPolygon2() {
        val polygon1 = listOf(
            LatLng(0.0 to 1.0),
            LatLng(1.0 to 2.0),
            LatLng(3.0 to 2.0),
            LatLng(4.0 to 1.0),
            LatLng(3.0 to 0.0),
            LatLng(1.0 to 0.0),
        )

        val polygon2 = listOf(
            LatLng(1.2 to 1.0),
            LatLng(0.8 to 3.0),
            LatLng(3.2 to 3.0),
            LatLng(3.2 to 1.0)
        )

        polygon1 intersectionsWith polygon2 `should be equal to` listOf(
            LatLng(1.2 to 1.0),
            LatLng(1.0 to 2.0),
            LatLng(3.0 to 2.0),
            LatLng(3.2 to 1.7999999999999998),
            LatLng(3.2 to 1.0),
        )
    }
}
