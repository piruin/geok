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

import kotlin.math.abs
import kotlin.math.min

const val EPISILON = 0.00005

/**
 * touches the other, they do intersect.
 * @see <a href="https://martin-thoma.com/how-to-check-if-two-line-segments-intersect">How to check if two line segments intersect</a>
 * @return value of the cross product
 */
fun Pair<LatLng, LatLng>.crossProduct(): Double {
    return first.x * second.y - second.x * first.y
}

/**
 * Check if bounding boxes do intersect. If one bounding box
 * touches the other, they do intersect.
 * @see <a href="https://martin-thoma.com/how-to-check-if-two-line-segments-intersect">How to check if two line segments intersect</a>
 * @return <code>true</code> if they intersect,
 *         <code>false</code> otherwise.
 */
infix fun Pair<LatLng, LatLng>.bboxIntersectWith(other: Pair<LatLng, LatLng>): Boolean {
    return BBox.from(this).intersectWith(BBox.from(other))
}

/**
 * Checks if a Point is on a given line
 * @see <a href="https://martin-thoma.com/how-to-check-if-two-line-segments-intersect">How to check if two line segments intersect</a>
 * @return <code>true</code> if point is on line,
 *         otherwise <code>false</code>
 */
infix fun LatLng.ifOn(line: Pair<LatLng, LatLng>): Boolean {
    // Move the image, so that line.first is on (0|0)
    val tmpLine = LatLng(0 to 0) to LatLng(line.second.x - line.first.x to line.second.y - line.first.y)
    val tmpPoint = LatLng(this.x - line.first.x, this.y - line.first.y)
    val r = (tmpLine.second to tmpPoint).crossProduct()
    return abs(r) < EPISILON
}

/**
 * Checks if a point is right of a line. If the point is on the
 * line, it is not right of the line.
 * @see <a href="https://martin-thoma.com/how-to-check-if-two-line-segments-intersect">How to check if two line segments intersect</a>
 * @return <code>true</code> if the point is right of the line,
 *         <code>false</code> otherwise
 */
infix fun LatLng.isRightOf(line: Pair<LatLng, LatLng>): Boolean {
    // Move the image, so that line.first is on (0|0)
    val tmpLine = LatLng(0 to 0) to LatLng(line.second.x - line.first.x to line.second.y - line.first.y)
    val tmpPoint = LatLng(this.x - line.first.x, this.y - line.first.y)
    return (tmpLine.second to tmpPoint).crossProduct() < 0.0
}

/**
 * Check if line segment first touches or crosses the line that is
 * defined by line segment second.
 * @see <a href="https://martin-thoma.com/how-to-check-if-two-line-segments-intersect">How to check if two line segments intersect</a>
 * @return <code>true</code> if line segment first touches or crosses line second,
 *         <code>false</code> otherwise.
 */
infix fun Pair<LatLng, LatLng>.segmentTouchesOrCrosses(other: Pair<LatLng, LatLng>): Boolean {
    val thisLine = this
    return other.first ifOn thisLine ||
        other.second ifOn thisLine ||
        (other.first isRightOf thisLine xor other.second.isRightOf(this))
}

/**
 * Check if line segments intersect
 * @return <code>true</code> if lines do intersect,
 *         <code>false</code> otherwise
 */
infix fun Pair<LatLng, LatLng>.isIntersectWith(other: Pair<LatLng, LatLng>): Boolean {
    return this bboxIntersectWith other &&
        this segmentTouchesOrCrosses other &&
        other segmentTouchesOrCrosses this
}

/**
 * Get where that given 2 lines will be intersect with each other if both line have infinite length.
 * Use <code>segmentIntersectionWith</code> for get intersection of finite line.
 *
 * @see segmentIntersectionWith
 * @return the intersection point of 2 line, it might be a line or a single point.
 * If it is a line, then x1 = x2 and y1 = y2.
 */
infix fun Pair<LatLng, LatLng>.intersectionWith(other: Pair<LatLng, LatLng>): Pair<LatLng, LatLng> {
    var a = this
    var b = other
    val x1: Double
    val y1: Double
    val x2: Double
    val y2: Double

    if (a.first.x == a.second.x) {
        // Case (A)
        // As a is a perfect vertical line, it cannot be represented
        // nicely in a mathematical way. But we directly know that
        //
        x1 = a.first.x
        x2 = x1
        if (b.first.x == b.second.x) {
            // Case (AA): all x are the same!
            // Normalize
            if (a.first.y > a.second.y) {
                a = a.swap()
            }
            if (b.first.y > b.second.y) {
                b = b.swap()
            }
            if (a.first.y > b.first.y) {
                a = b.also { b = a }
            }

            // Now we know that the y-value of a.first is the
            // lowest of all 4 y values
            // this means, we are either in case (AAA):
            //   a: x--------------x
            //   b:    x---------------x
            // or in case (AAB)
            //   a: x--------------x
            //   b:    x-------x
            // in both cases:
            // get the relavant y intervall
            y1 = b.first.y
            y2 = min(a.second.y, b.second.y)
        } else {
            // Case (AB)
            // we can mathematically represent line b as
            //     y = m*x + t <=> t = y - m*x
            // m = (y1-y2)/(x1-x2)
            val m = (b.first.y - b.second.y) /
                (b.first.x - b.second.x)
            val t = b.first.y - m * b.first.x
            y1 = m * x1 + t
            y2 = y1
        }
    } else if (b.first.x == b.second.x) {
        // Case (B)
        // essentially the same as Case (AB), but with
        // a and b switched
        x1 = b.first.x
        x2 = x1

        val tmp = a
        a = b
        b = tmp
        val m = (b.first.y - b.second.y) /
            (b.first.x - b.second.x)
        val t = b.first.y - m * b.first.x
        y1 = m * x1 + t
        y2 = y1
    } else {
        // Case (C)
        // Both lines can be represented mathematically
        val ma = (a.first.y - a.second.y) /
            (a.first.x - a.second.x)
        val mb = (b.first.y - b.second.y) /
            (b.first.x - b.second.x)
        val ta = a.first.y - ma * a.first.x
        val tb = b.first.y - mb * b.first.x
        if (ma == mb) {
            // Case (CA)
            // both lines are in parallel. As we know that they
            // intersect, the intersection could be a line
            // when we rotated this, it would be the same situation
            // as in case (AA)

            // Normalize
            if (a.first.x > a.second.x) {
                a.swap()
            }
            if (b.first.x > b.second.x) {
                b.swap()
            }
            if (a.first.x > b.first.x) {
                a = b.also { b = a }
            }

            // get the relavant x intervall
            x1 = b.first.x
            x2 = min(a.second.x, b.second.x)
            y1 = ma * x1 + ta
            y2 = ma * x2 + ta
        } else {
            // Case (CB): only a point as intersection:
            // y = ma*x+ta
            // y = mb*x+tb
            // ma*x + ta = mb*x + tb
            // (ma-mb)*x = tb - ta
            // x = (tb - ta)/(ma-mb)
            x1 = (tb - ta) / (ma - mb)
            y1 = ma * x1 + ta
            x2 = x1
            y2 = y1
        }
    }
    return LatLng(x1 to y1) to LatLng(x2 to y2)
}

val Pair<LatLng, LatLng>.isVerticalLine: Boolean get() = first.x == second.x
val Pair<LatLng, LatLng>.isHorizontalLine: Boolean get() = first.y == second.y

/**
 * @return the intersection point of 2 line, it might be a line or a single point.
 * If it is a line, then x1 = x2 and y1 = y2.
 */
infix fun Pair<LatLng, LatLng>.segmentIntersectionWith(other: Pair<LatLng, LatLng>): Pair<LatLng, LatLng>? {
    if (this.isHorizontalLine || this.isVerticalLine ||
        other.isHorizontalLine || other.isVerticalLine
    ) {
        // case of line of perfect vertical & perfect horizontal
        val result = this.intersectionWith(other)
        if (result.bboxIntersectWith(this) && result.bboxIntersectWith(other))
            return result
    }
    if (!this.isIntersectWith(other))
        return null
    val result = intersectionWith(other)
    // Make sure result is on both lines, this require for some case!!
    if (result.bboxIntersectWith(this) && result.bboxIntersectWith(other))
        return result
    return null
}

/**
 * @see <a href="https://www.swtestacademy.com/intersection-convex-polygons-algorithm/">Intersection Convex Polygons Algorithm<a>
 * @return List of intersection points of this Line and Polygon
 */
infix fun Pair<LatLng, LatLng>.intersectionsWith(polygon: Collection<LatLng>): List<LatLng> {
    val result = mutableListOf<LatLng>()
    polygon.forEachLine {
        this.segmentIntersectionWith(it)?.let { point -> result.addUnique(point.toList()) }
    }
    return result
}

/**
 * @see <a href="https://www.swtestacademy.com/intersection-convex-polygons-algorithm/">Intersection Convex Polygons Algorithm<a>
 * @return Intersection polygon of this and another polygon
 */
infix fun Collection<LatLng>.intersectionsWith(other: Collection<LatLng>): List<LatLng> {
    val polygon1 = this.close()
    val polygon2 = other.close()
    val clippedPoint = mutableListOf<LatLng>()

    polygon1.forEach { if (it insideOf polygon2) clippedPoint.addUnique(it) }
    polygon2.forEach { if (it insideOf polygon1) clippedPoint.addUnique(it) }
    polygon1.forEachLine { line -> clippedPoint.addUnique(line intersectionsWith polygon2) }

    // Require for some edge case, please don't worry about performance (dear Myself).
    polygon2.forEachLine { line -> clippedPoint.addUnique(line intersectionsWith polygon1) }

    if (clippedPoint.isEmpty())
        return clippedPoint
    return clippedPoint.sortedClockwise()
}
