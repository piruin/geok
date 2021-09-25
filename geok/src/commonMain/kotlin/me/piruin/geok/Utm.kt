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

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

data class Utm(val zone: Int, val hemisphere: Char, val easting: Double, val northing: Double) {

    override fun toString(): String {
        return "$zone $hemisphere $easting, $northing"
    }

    /**
     * This method port from Professor Steven Dutch's JavaScript "Convert Between Geographic and UTM Coordinates"
     *
     * @see <a href="http://www.uwgb.edu/dutchs/UsefulData/ConvertUTMNoOZ.HTM">Convert Between Geographic and UTM Coordinates</a>
     * @see <a href="http://www.uwgb.edu/dutchs/UsefulData/UTMFormulas.HTM">Converting UTM to Latitude and Longitude (Or Vice Versa)</a>
     *
     */
    fun toLatLng(datum: Datum = Datum.WSG84): LatLng {
        require(easting between (160000.0 and 840000.0)) { "Outside permissible range of easting values" }
        require(northing between (0.0 and 10000000.0)) { "Outside permissible range of northing values" }

        val a = datum.equatorialRad

        val k = 1.0 // Local scale
        val k0 = 0.9996 // scale on central meridian
        val b = datum.polarRad // polar axis.

        val e = sqrt(1.0 - (b / a) * (b / a)) // eccentricity
        val e0 = e / sqrt(1.0 - e * e) // Called e prime in reference
        val esq = (1.0 - (b / a) * (b / a)) // e squared for use in expansions
        val e0sq = e * e / (1.0 - e * e) // e0 squared - always even powers

        val x = easting
        val y = northing

        val utmz = zone
        val zcm = 3.0 + 6.0 * (utmz - 1.0) - 180.0 // Central meridian of zone
        val e1 = (1.0 - sqrt(1.0 - e * e)) / (1.0 + sqrt(1.0 - e * e)) // Called e1 in USGS PP 1395 also
        val M0 = 0.0 // In case origin other than zero lat - not needed for standard UTM
        var M = M0 + y / k0 // Arc length along standard meridian.
        if ('S' == hemisphere) {
            M = M0 + (y - 10000000.0) / k
        }
        val mu = M / (a * (1.0 - esq * (1.0 / 4.0 + esq * (3.0 / 64.0 + 5.0 * esq / 256.0))))
        var phi1 = mu + e1 * (3.0 / 2.0 - (27.0 * e1 * e1 / 32.0)) * sin(2.0 * mu) + e1 * e1 *
            (21.0 / 16.0 - 55.0 * e1 * e1 / 32.0) * sin(4.0 * mu) // Footprint Latitude
        phi1 += e1 * e1 * e1 * (sin(6.0 * mu) * 151.0 / 96.0 + e1 * sin(8.0 * mu) * 1097.0 / 512.0)

        val C1 = e0sq * cos(phi1).pow(2.0)
        val T1 = tan(phi1).pow(2.0)
        val N1 = a / sqrt(1.0 - (e * sin(phi1)).pow(2.0))
        val R1 = N1 * (1.0 - e * e) / (1.0 - (e * sin(phi1)).pow(2.0))
        val D = (x - 500000.0) / (N1 * k0)

        var phi = (D * D) * (
            1.0 / 2.0 - D * D
                * (5.0 + 3.0 * T1 + 10.0 * C1 - 4.0 * C1 * C1 - 9.0 * e0sq) / 24.0
            )
        phi += D.pow(6.0) *
            (61.0 + 90.0 * T1 + 298.0 * C1 + 45.0 * T1 * T1 - 252.0 * e0sq - 3.0 * C1 * C1) / 720.0
        phi = phi1 - (N1 * tan(phi1) / R1) * phi

        val drad = PI / 180.0
        val latd = phi / drad

        val lng = D * (
            1 + D * D * (
                (-1 - 2 * T1 - C1) / 6 + D * D *
                    (5 - 2 * C1 + 28 * T1 - 3 * C1 * C1 + 8 * e0sq + 24 * T1 * T1) / 120
                )
            ) / cos(phi1)
        val lngd = zcm + lng / drad

        return LatLng(latd.round(6), lngd.round(6))
    }
}
