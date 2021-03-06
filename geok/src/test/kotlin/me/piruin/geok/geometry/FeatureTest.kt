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

package me.piruin.geok.geometry

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should not be`
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
            type `should be equal to` "Feature"
            geometry `should be equal to` polygon
            bbox `should not be` null
            properties `should be equal to` Properties()
        }
    }

    data class Properties(
        val prob0: String = "value0",
        val prob1: Double = 0.0
    )
}
