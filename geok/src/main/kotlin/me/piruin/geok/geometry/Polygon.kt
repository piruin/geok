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

import me.piruin.geok.Datum
import me.piruin.geok.LatLng
import me.piruin.geok.toRadians

data class Polygon(var boundary: MutableList<LatLng>,
    var holes: MutableList<MutableList<LatLng>> = arrayListOf()) : Geometry {

    override val type: String = "Polygon"

    constructor(vararg latlngs: LatLng) : this(latlngs.toMutableList())

    constructor(vararg xyPair: Pair<Double, Double>) :
      this(xyPair.map { LatLng(it.second, it.first) }.toMutableList())

    val isClosed: Boolean
        get() = boundary.isClosed

    fun contains(coordinate: LatLng): Boolean {
        if (boundary.contains(coordinate)) {
            return true
        }
        return holes.any { it.contains(coordinate) }
    }

    fun area(earthRadius: Double = Datum.WSG48.equatorialRad): Double {
        var area = boundary.area(earthRadius)
        holes.forEach { area -= it.area(earthRadius) }
        return area
    }

    val centroid: LatLng
        get() {
            val tmpBoundary = this.boundary.toMutableList()
            val centroid = doubleArrayOf(0.0, 0.0)
            if (!isClosed)
                tmpBoundary.add(tmpBoundary.get(0))

            for (point in tmpBoundary) {
                centroid[0] += point.latitude
                centroid[1] += point.longitude
            }

            centroid[0] = centroid[0] / tmpBoundary.size
            centroid[1] = centroid[1] / tmpBoundary.size
            return LatLng(centroid[0], centroid[1])
        }
}

val List<LatLng>.isClosed: Boolean
    get() = this[0] == this.last()

fun List<LatLng>.area(earthRadius: Double = Datum.WSG48.equatorialRad): Double {
    if (this.size < 3) {
        return 0.0
    }
    val diameter = earthRadius * 2
    val circumference = diameter * Math.PI
    val ySegment = ArrayList<Double>()
    val xSegment = ArrayList<Double>()

    // calculateArea segment x and y in degrees for each point
    val latitudeRef = this[0].latitude
    val longitudeRef = this[0].longitude
    for (i in 1 until size) {
        val latitude = this[i].latitude
        val longitude = this[i].longitude
        ySegment.add((latitude - latitudeRef) * circumference / 360.0)
        xSegment.add((longitude - longitudeRef) * circumference * Math.cos(latitude.toRadians()) / 360.0)
    }
    // calculateArea areas for each triangle segment
    val triangleArea = ArrayList<Double>()
    for (i in 1 until xSegment.size) {
        val x1 = xSegment[i - 1]
        val y1 = ySegment[i - 1]
        val x2 = xSegment[i]
        val y2 = ySegment[i]
        triangleArea.add((y1 * x2 - x1 * y2) / 2)
    }
    // get abolute value of area, it can't be negative
    return Math.abs(triangleArea.sum())
}
