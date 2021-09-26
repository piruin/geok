/*
 * Copyright (c) 2021 Piruin Panichphol
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
import me.piruin.geok.centroid

data class MultiPoint(override val coordinates: List<Point>) : GeometryCollection<Point> {

    constructor(vararg point: Point) : this(point.toList())

    override val type: String = this::class.simpleName!!
    override val bbox: BBox = BBox.from(coordinates.map { it.coordinates })

    val points: List<Point>
        get() = coordinates

    /**
     * @return centroid coordination of this Polygon
     */
    val centroid: LatLng
        get() = coordinates.map { it.coordinates }.centroid
}
