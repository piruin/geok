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

package me.piruin.geok

import kotlin.math.max
import kotlin.math.min

/**
 *
 * @see <a href="https://www.swtestacademy.com/intersection-convex-polygons-algorithm/">Intersection Convex Polygons Algorithm<a>
 * @return Intersection point of 2 line, `null` when lines dose not cross each other.
 */
infix fun Pair<LatLng, LatLng>.intersectionWith(line2: Pair<LatLng, LatLng>): LatLng? {
    val line1 = this
    val a1 = line1.second.y - line1.first.y
    val b1 = line1.first.x - line1.second.x
    val c1 = a1 * line1.first.x + b1 * line1.first.y

    val a2 = line2.second.y - line2.first.y
    val b2 = line2.first.x - line2.second.x
    val c2 = a2 * line2.first.x + b2 * line2.first.y

    val det = a1 * b2 - a2 * b1
    if (det.equalsTo(0.0)) {
        return null
    }
    val x = (b1 * c1 - b1 * c2) / det
    val y = (a1 * c2 - a2 * c1) / det
    val online1 = (min(line1.first.x, line1.second.x) < x || min(line1.first.x, line1.second.x).equalsTo(x)) &&
        (max(line1.first.x, line1.second.x) > x || max(line1.first.x, line1.second.x).equalsTo(x)) &&
        (min(line1.first.y, line1.second.y) < y || min(line1.first.y, line1.second.y).equalsTo(y)) &&
        (max(line1.first.y, line1.second.y) > y || max(line1.first.y, line1.second.y).equalsTo(y))
    val online2 = (min(line2.first.x, line2.second.x) < x || min(line2.first.x, line2.second.x).equalsTo(x)) &&
        (max(line2.first.x, line2.second.x) > x || max(line2.first.x, line2.second.x).equalsTo(x)) &&
        (min(line2.first.y, line2.second.y) < y || min(line2.first.y, line2.second.y).equalsTo(y)) &&
        (max(line2.first.y, line2.second.y) > y || max(line2.first.y, line2.second.y).equalsTo(y))
    return if (online1 && online2) LatLng(x to y) else null
}

/**
 * @see <a href="https://www.swtestacademy.com/intersection-convex-polygons-algorithm/">Intersection Convex Polygons Algorithm<a>
 * @return List of intersection points of this Line and Polygon
 */
infix fun Pair<LatLng, LatLng>.intersectionWith(polygon: List<LatLng>): List<LatLng> {
    val result = mutableListOf<LatLng>()
    polygon.forEachLine {
        this.intersectionWith(it)?.let { point -> result.addUnique(point) }
    }
    return result
}

/**
 * @see <a href="https://www.swtestacademy.com/intersection-convex-polygons-algorithm/">Intersection Convex Polygons Algorithm<a>
 * @return Intersection polygon of this and another polygon
 */
infix fun Collection<LatLng>.intersectionWith(other: List<LatLng>): List<LatLng> {
    val polygon1 = this.close()
    val polygon2 = other.close()
    val clippedPoint = mutableListOf<LatLng>()

    polygon1.forEach { if (it insideOf polygon2) clippedPoint.addUnique(it) }
    polygon2.forEach { if (it insideOf polygon1) clippedPoint.addUnique(it) }
    polygon1.forEachLine { line -> clippedPoint.addUnique(line intersectionWith polygon2) }

    if (clippedPoint.isEmpty())
        return clippedPoint
    return clippedPoint.sortedClockwise()
}
