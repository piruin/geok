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
import me.piruin.geok.Datum
import me.piruin.geok.LatLng
import me.piruin.geok.area
import me.piruin.geok.centroid
import me.piruin.geok.close
import me.piruin.geok.distance
import me.piruin.geok.intersectionsWith
import me.piruin.geok.isClosed
import me.piruin.geok.open
import me.piruin.geok.safeSortedClockwise

data class Polygon(
    var boundary: List<LatLng>,
    var holes: List<List<LatLng>> = listOf()
) : Geometry {

    constructor(boundary: List<LatLng>, vararg holes: List<LatLng>) : this(boundary, holes.toList())
    constructor(vararg latlngs: LatLng) : this(latlngs.toList())
    constructor(vararg xyPair: Pair<Double, Double>) : this(xyPair.map { LatLng(it.second, it.first) })

    override val type: String = this::class.simpleName!!
    val bbox: BBox = BBox.from(boundary)

    init {
        require(boundary.size >= 3) { "Boundary of polygon should have at least 3 point" }
        this.boundary = boundary.safeSortedClockwise()
        this.holes = holes.map { it.safeSortedClockwise() }
    }

    val isClosed: Boolean
        get() = boundary.isClosed

    /**
     * @return Determines whether the specified coordinates are inside this Polygon.
     */
    fun contains(coordinate: LatLng): Boolean {
        if (boundary.contains(coordinate) || holes.any { it.contains(coordinate) } ||
            coordinate.insideOf(boundary) || holes.any { coordinate.insideOf(it) }
        )
            return true
        return false
    }

    /**
     * @return Determines whether the specified point are inside this Polygon.
     */
    fun contains(point: Point): Boolean = contains(point.coordinates)

    fun addHole(vararg holes: List<LatLng>) {
        val holesList = holes.map { it.safeSortedClockwise() }
        this.holes = this.holes.toMutableList().apply { addAll(holesList) }
    }

    /**
     * @return calculated area of polygon in square meter (m^2)
     */
    fun area(earthRadius: Double = Datum.WSG84.equatorialRad): Double {
        var area = boundary.area(earthRadius)
        holes.forEach { area -= it.area(earthRadius) }
        return area
    }

    /**
     * @return perimeter of Polygon in meter (m)
     */
    val perimeter: Double
        get() {
            val bound = if (!boundary.isClosed) boundary.close() else boundary
            return bound.distance
        }

    /**
     * @return centroid coordination of this Polygon
     */
    val centroid: LatLng
        get() {
            val bound = if (boundary.isClosed) boundary.open() else boundary
            return bound.centroid
        }

    /**
     * @return Intersection polygon with this polygon
     */
    fun intersectionWith(other: Polygon): Polygon? {
        val result = boundary intersectionsWith other.boundary
        if (result.size < 3)
            return null
        return Polygon(result)
    }
}
