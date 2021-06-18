/*
 * Copyright (c) 2018 Piruin Panichphol
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package me.piruin.geok

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
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
    fun sortedClockwise() {
        val cw = listOf(
            LatLng(1.0 to 0.0),
            LatLng(1.0 to 1.0),
            LatLng(2.0 to 1.0),
            LatLng(2.0 to 0.0)
        )

        val ccw = listOf(
            LatLng(1.0 to 0.0),
            LatLng(2.0 to 0.0),
            LatLng(2.0 to 1.0),
            LatLng(1.0 to 1.0)
        )

        ccw.sortedClockwise() `should be equal to` cw
        cw.sortedCounterClockwise() `should be equal to` ccw
    }

    @Test
    fun sortedClockwiseNegativeValue() {
        val cw = listOf(
            LatLng(-1.0 to 0.0),
            LatLng(-1.0 to 1.0),
            LatLng(1.0 to 1.0),
            LatLng(1.0 to 0.0)
        )

        val ccw = listOf(
            LatLng(-1.0 to 0.0),
            LatLng(1.0 to 0.0),
            LatLng(1.0 to 1.0),
            LatLng(-1.0 to 1.0)
        )

        ccw.sortedClockwise() `should be equal to` cw
        cw.sortedCounterClockwise() `should be equal to` ccw
    }

    @Test
    fun toStringNotShowElevationWhenNaN() {
        LatLng(16.423976, 102.841838).toString() `should be equal to` "[102.841838, 16.423976]"
    }

    @Test
    fun toUtm() {
        LatLng(16.423976, 102.841838).toUtm() `should be equal to` Utm(48, 'N', 269542.0, 1817061.8)
    }

    @Test
    fun intersectionPoint() {
        val line1 = LatLng(0.0 to 2.0) to LatLng(2.0 to 0.0)
        val line2 = LatLng(0.0 to 0.0) to LatLng(2.0 to 2.0)

        line1 intersectionPointWith line2 `should be equal to` LatLng(1.0 to 1.0)
    }

    @Test
    fun intersectionPointOfParallel() {
        val line1 = LatLng(0.0 to 2.0) to LatLng(2.0 to 2.0)
        val line2 = LatLng(0.0 to 0.0) to LatLng(2.0 to 0.0)

        line1 intersectionPointWith line2 `should be` null
    }

    @Test
    fun pointInsideOfBoundary() {
        val boundary = listOf(
            LatLng(0.0 to 0.0),
            LatLng(0.0 to 2.0),
            LatLng(2.0 to 2.0),
            LatLng(2.0 to 0.0),
            LatLng(0.0 to 0.0),
        )

        val point = LatLng(1.0 to 1.0)
        point insideOf boundary `should be` true

        val outsidePoint = LatLng(-1.0 to 0.0)
        outsidePoint insideOf boundary `should be` false
        val outsidePoint2 = LatLng(2.1 to 0.3)
        outsidePoint2 insideOf boundary `should be` false

        val onlinePoint = LatLng(0.0 to 0.5)
        onlinePoint insideOf boundary `should be` true

        boundary.forEach { it insideOf boundary `should be` true }
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

        line intersectionPointsWith boundary `should be equal to` expected
    }

    @Test
    fun forEachLine() {
        val boundary = listOf(
            LatLng(0.0 to 0.0),
            LatLng(0.0 to 2.0),
            LatLng(2.0 to 2.0),
            LatLng(2.0 to 0.0),
            LatLng(0.0 to 0.0),
        )

        var index = 0
        boundary.forEachLine { line ->
            when (index++) {
                0 -> line `should be equal to` (LatLng(0.0 to 0.0) to LatLng(0.0 to 2.0))
                1 -> line `should be equal to` (LatLng(0.0 to 2.0) to LatLng(2.0 to 2.0))
                2 -> line `should be equal to` (LatLng(2.0 to 2.0) to LatLng(2.0 to 0.0))
                3 -> line `should be equal to` (LatLng(2.0 to 0.0) to LatLng(0.0 to 0.0))
                4 -> throw AssertionError()
            }
        }
    }

    @Test
    fun intersectionPointPolygon() {
        val polygon1 = listOf(
            LatLng(0.0 to 1.0),
            LatLng(1.0 to 2.0),
            LatLng(3.0 to 2.0),
            LatLng(4.0 to 1.0),
            LatLng(3.0 to 0.0),
            LatLng(1.0 to 0.0),
        )

        val polygon2 = listOf(
            LatLng(2.0 to 1.0),
            LatLng(3.0 to 1.9),
            LatLng(3.9 to 1.0),
            LatLng(3.0 to 0.1)
        )

        polygon1 intersectionPointsWith polygon2 `should be equal to` listOf(
            LatLng(2.0 to 1.0),
            LatLng(3.0 to 1.9),
            LatLng(3.9 to 1.0),
            LatLng(3.0 to 0.1),
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

        polygon1 intersectionPointsWith polygon2 `should be equal to` listOf(
            LatLng(1.2 to 1.0),
            LatLng(1.0 to 2.0),
            LatLng(3.0 to 2.0),
            LatLng(3.2 to 1.0),
        )
    }
}
