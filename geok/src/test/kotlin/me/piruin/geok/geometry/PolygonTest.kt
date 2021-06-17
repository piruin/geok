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

import me.piruin.geok.BBox
import me.piruin.geok.LatLng
import me.piruin.geok.Utm
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.junit.Ignore
import org.junit.Test
import kotlin.math.roundToInt

class PolygonTest {

    val polygon = Polygon(
        LatLng(16.4268129901041, 102.8380009059),
        LatLng(16.4266819930293, 102.8379568936),
        LatLng(16.4267047695460, 102.8378494011),
        LatLng(16.4268502721458, 102.8378330329),
        LatLng(16.4268937418855, 102.8378020293),
        LatLng(16.4268129901041, 102.8380009059)
    )

    @Test
    fun contain() {
        polygon.contains(LatLng(16.4268129901041, 102.8380009059)) `should be` true
        polygon.contains(LatLng(16.4268502721458, 102.8378330329)) `should be` true
    }

    @Test
    fun isClose() {
        polygon.isClosed `should be` true
    }

    @Test
    fun area() {
        polygon.area().roundToInt() `should be equal to` 268
    }

    @Test
    fun bbox() {
        polygon.bbox `should be equal to` BBox(
            left = 102.8378020293,
            bottom = 16.4266819930293,
            right = 102.8380009059,
            top = 16.4268937418855
        )
    }

    @Ignore
    @Test
    fun centroid() {
        val boundary = Polygon(
            98.3432525117 to 7.82002427060229,
            98.3434401322 to 7.81997546731602,
            98.3436993774 to 7.81984612009375,
            98.3438997787 to 7.81976395641512,
            98.3443444709 to 7.819589152106,
            98.3444144772 to 7.81956097493145,
            98.3444239906 to 7.81952327705373,
            98.3443940576 to 7.8193176915304,
            98.344314997 to 7.8190894007154,
            98.3443018769 to 7.81901584006111,
            98.3442867794 to 7.81899695939897,
            98.343921432 to 7.81911263611518,
            98.3437296833 to 7.81920436671021,
            98.3434939411 to 7.81929517678774,
            98.3433884209 to 7.81931254759861,
            98.3431356439 to 7.81905382113133,
            98.3429054628 to 7.81933634314872,
            98.3428266146 to 7.81933344001763,
            98.3428941219 to 7.81945102277898,
            98.3429116108 to 7.81951066351522,
            98.3429473824 to 7.8196843045481,
            98.3430250791 to 7.82003262732049,
            98.3432525117 to 7.82002427060229
        )

        boundary.centroid.toUtm() `should be equal to` Utm(47, 'N', 427634.2, 864397.6)
    }
}
