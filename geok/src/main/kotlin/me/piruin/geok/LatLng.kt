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

import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

data class LatLng(val latitude: Double, val longitude: Double, val elevation: Double? = null) {

    val x get() = longitude
    val y get() = latitude
    val z get() = elevation

    constructor(xyPair: Pair<Number, Number>) : this(xyPair.second.toDouble(), xyPair.first.toDouble())

    init {
        require(latitude between (-90.0 and 90.0)) { "latitude should between -90.0 and 90 [$latitude]" }
        require(longitude between (-180.0 and 180.0)) { "longitude should between -180.0 and 180 [$longitude]" }
    }

    /**
     * @return distance in meter (m)
     */
    infix fun distanceTo(latlng: LatLng) = distanceCalculator().between(this, latlng)

    override fun toString(): String {
        return "[$longitude, $latitude" + if (elevation != null) ", $elevation]" else "]"
    }

    /**
     * This method port from Professor Steven Dutch's JavaScript "Convert Between Geographic and UTM Coordinates"
     *
     * @see <a href="http://www.uwgb.edu/dutchs/UsefulData/ConvertUTMNoOZ.HTM">Convert Between Geographic and UTM Coordinates</a>
     * @see <a href="http://www.uwgb.edu/dutchs/UsefulData/UTMFormulas.HTM">Converting UTM to Latitude and Longitude (Or Vice Versa)</a>
     *
     */
    fun toUtm(datum: Datum = Datum.WSG84): Utm {
        require(latitude between (-80.0 to 84.0)) { "latitude $latitude is outside utm grid" }

        val a = datum.equatorialRad
        val f = 1.0 / datum.flat // polar flattening.
        val b = datum.polarRad
        val e = sqrt(1 - (b / a) * (b / a)) // eccentricity

        val scale = 0.9996 // scale on central meridian

        val latRad = latitude.toRadians() // Convert latitude to radians
        val utmZone = 1 + floor((longitude + 180) / 6) // calculate utm zone
        var latZone = when { // Latitude zone: A-B S of -80, C-W -80 to +72, X 72-84, Y,Z N of 84
            latitude > -80 && latitude < 72 -> (floor((latitude + 80) / 8) + 2).toInt()
            latitude > 72 && latitude < 84 -> 21
            latitude > 84 -> 23
            else -> 0
        }

        val zcm = 3 + 6 * (utmZone - 1) - 180 // Central meridian of zone
        val esq = (1 - (b / a) * (b / a)) // e squared for use in expansions
        val e0sq = e * e / (1 - e * e) // e0 squared - always even powers
        val N = a / sqrt(1 - (e * sin(latRad)).pow(2.0))

        val T = tan(latRad).pow(2.0)
        val C = e0sq * cos(latRad).pow(2.0)
        val A = (longitude - zcm).toRadians() * cos(latRad)

        var easting = scale * N * A * (
            1 + A * A * (
                (1 - T + C) / 6 + A * A
                    * (5 - 18 * T + T * T + 72 * C - 58 * e0sq) / 120
                )
            ) // Easting relative to Central meridian
        easting += 500000

        var M = latRad * (1.0 - esq * (1.0 / 4.0 + esq * (3.0 / 64.0 + 5 * esq / 256)))
        M -= sin(2.0 * latRad) * (esq * (3.0 / 8.0 + esq * (3.0 / 32.0 + 45 * esq / 1024)))
        M += sin(4.0 * latRad) * (esq * esq * (15.0 / 256.0 + esq * 45.0 / 1024))
        M -= sin(6.0 * latRad) * (esq * esq * esq * (35.0 / 3072))
        M *= a // Arc length along standard meridian

        var northing = scale * (
            M + N * tan(latRad) *
                (
                    A * A * (
                        1.0 / 2.0 + A * A * (
                            (5.0 - T + (9.0 * C) + (4.0 * C * C)) / 24.0 + A * A
                                * (61.0 - (58.0 * T) + (T * T) + (600.0 * C) - (330.0 * e0sq)) / 720
                            )
                        )
                    )
            ) // Northing from equator
        if (this.latitude < 0) {
            northing += 10000000.0
        }
        return Utm(utmZone.toInt(), if (latRad > 0) 'N' else 'S', easting.round(1), northing.round(1))
    }

    /**
     * @see <a href="https://wrf.ecse.rpi.edu/Research/Short_Notes/pnpoly.html">PNPOLY - Point Inclusion in Polygon Test</a>
     * @return Determines whether this LatLng are inside of given Polygon.
     */
    infix fun insideOf(polygon: List<LatLng>): Boolean {
        if (polygon.contains(this)) // Point of Polygon consider to be inside
            return true

        var i = 0
        var j = polygon.size - 1
        var result = false

        while (i < polygon.size) {
            if (polygon[i].y > this.y != polygon[j].y > this.y &&
                this.x < (polygon[j].x - polygon[i].x) * (this.y - polygon[i].y) /
                (polygon[j].y - polygon[i].y) + polygon[i].x
            ) result = !result
            j = i++
        }
        return result
    }

    override fun hashCode(): Int {
        var result = latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        result = 31 * result + (elevation?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as LatLng

        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (elevation != other.elevation) return false

        return true
    }

    companion object {
        /**
         * Default precision value for comparing LatLng data,
         * 6 decimal should be enough for more case (accurate at 11 cm).
         *
         * @see <a href="https://stackoverflow.com/a/22680065">Accuracy of digit</a>
         * @see <a href="https://developers.google.com/maps/documentation/javascript/reference/coordinates#LatLng.toUrlValue>Google Maps API</a>
         */
        const val DEFAULT_PRECISION = 0.000001

        var PRECISION = DEFAULT_PRECISION
            set(value) {
                if (value > 0.1)
                    throw IllegalArgumentException("PRECISION value must not more than 0.1")
                field = value
            }
    }
}

fun Double.equalsTo(other: Double?, delta: Double = LatLng.PRECISION): Boolean {
    if (other == null)
        return false
    return abs(this - other) <= delta
}
