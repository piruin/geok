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
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min

fun Collection<LatLng>.close(): List<LatLng> {
    check(!isEmpty())
    check(size >= 3)
    if (isClosed)
        return ArrayList(this)
    return ArrayList(this).apply { add(first()) }
}

fun Collection<LatLng>.open(): List<LatLng> {
    check(!isEmpty())
    if (!isClosed)
        return ArrayList(this)
    return ArrayList(this).apply { remove(last()) }
}

fun <T> Iterable<T>.count(): Int {
    var size = 0
    val iterable = iterator()
    while (iterable.hasNext()) {
        iterable.next()
        size++
    }
    return size
}

val Iterable<LatLng>.isClosed: Boolean
    get() = count() > 0 && first() == last()

@Deprecated("Replace with distance", replaceWith = ReplaceWith("distance", "me.piruin.geok.distance"))
val Iterable<LatLng>.length: Double
    get() {
        var distance = 0.0
        val iterator = iterator()
        if (!iterator.hasNext())
            return distance

        var current = iterator.next()
        while (iterator.hasNext()) {
            val next = iterator.next()
            distance += current.distanceTo(next)
            current = next
        }
        return distance
    }

val Iterable<LatLng>.distance: Double
    get() {
        return foldIndexed(0.0) { index, distance, current ->
            distance + current.distanceTo(elementAtOrNull(index + 1) ?: current)
        }
    }

val Iterable<LatLng>.centroid: LatLng
    get() {
        var latlngs = this
        if (isClosed) {
            latlngs = when (this) {
                is Collection -> open()
                else -> toList().open()
            }
        }
        val lat = latlngs.map { it.latitude }.average()
        val lng = latlngs.map { it.longitude }.average()
        return LatLng(lat, lng)
    }

fun Collection<LatLng>.safeSortedClockwise(): List<LatLng> {
    val list = if (!isClosed) this else open()
    return (list.sortedClockwise()).close()
}

fun Iterable<LatLng>.sortedClockwise(): List<LatLng> {
    require(!isClosed) { "LatLng must not be closed to perform sorting, Try call `open()` before" }
    val center = centroid
    return this.sortedBy { atan2(it.longitude - center.longitude, it.latitude - center.latitude) }
}

fun Iterable<LatLng>.sortedCounterClockwise(): List<LatLng> {
    require(!isClosed) { "LatLng must not be closed to perform sorting, Try call `open()` before" }
    val center = centroid
    return this.sortedBy { atan2(it.latitude - center.latitude, it.longitude - center.longitude) }
}

fun Collection<LatLng>.area(earthRadius: Double = Datum.WSG48.equatorialRad): Double {
    if (size < 3) {
        return 0.0
    }
    val diameter = earthRadius * 2
    val circumference = diameter * Math.PI
    val ySegment = ArrayList<Double>()
    val xSegment = ArrayList<Double>()

    // calculateArea segment x and y in degrees for each point
    val ref = first()
    for (i in 1 until size) {
        val element = elementAt(i)
        ySegment.add((element.latitude - ref.latitude) * circumference / 360.0)
        xSegment.add((element.longitude - ref.longitude) * circumference * cos(element.latitude.toRadians()) / 360.0)
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
    return abs(triangleArea.sum())
}

/**
 * from https://www.swtestacademy.com/intersection-convex-polygons-algorithm/ `GetIntersectionPoint()`
 */
infix fun Pair<LatLng, LatLng>.intersectionPointWith(line2: Pair<LatLng, LatLng>): LatLng? {
    val line1 = this
    val a1 = line1.second.latitude - line1.first.latitude
    val b1 = line1.first.longitude - line1.second.longitude
    val c1 = a1 * line1.first.longitude + b1 * line1.first.latitude

    val a2 = line2.second.latitude - line2.first.latitude
    val b2 = line2.first.longitude - line2.second.longitude
    val c2 = a2 * line2.first.longitude + b2 * line2.first.latitude

    val det = a1 * b2 - a2 * b1
    if (det.equalsTo(0.0)) {
        println("$line1 x $line2 => null")
        return null
    }
    val x = (b1 * c1 - b1 * c2) / det
    val y = (a1 * c2 - a2 * c1) / det
    val online1 = (
        (
            min(line1.first.longitude, line1.second.longitude) < x || min(
                line1.first.longitude,
                line1.second.longitude
            ).equalsTo(x)
            ) &&
            (
                max(line1.first.longitude, line1.second.longitude) > x || max(
                    line1.first.longitude,
                    line1.second.longitude
                ).equalsTo(x)
                ) &&
            (
                min(line1.first.latitude, line1.second.latitude) < y || min(
                    line1.first.latitude,
                    line1.second.latitude
                ).equalsTo(y)
                ) &&
            (
                max(line1.first.latitude, line1.second.latitude) > y || max(
                    line1.first.latitude,
                    line1.second.latitude
                ).equalsTo(y)
                )
        )
    val online2 = (
        (
            min(line2.first.longitude, line2.second.longitude) < x || min(
                line2.first.longitude,
                line2.second.longitude
            ).equalsTo(x)
            ) &&
            (
                max(line2.first.longitude, line2.second.longitude) > x || max(
                    line2.first.longitude,
                    line2.second.longitude
                ).equalsTo(x)
                ) &&
            (
                min(line2.first.latitude, line2.second.latitude) < y || min(
                    line2.first.latitude,
                    line2.second.latitude
                ).equalsTo(y)
                ) &&
            (
                max(line2.first.latitude, line2.second.latitude) > y || max(
                    line2.first.latitude,
                    line2.second.latitude
                ).equalsTo(y)
                )
        )
    return if (online1 && online2) {
        println("$line1 x $line2 => ${LatLng(x to y)}")
        LatLng(x to y)
    } else {
        println("$line1 x $line2 => null")
        null
    }
}

infix fun Pair<LatLng, LatLng>.intersectionPointsWith(poly: List<LatLng>): List<LatLng> {
    val thisLine = this
    val result = mutableListOf<LatLng>()
    poly.forEachLine { line ->
        print("------> inner line | ")
        thisLine.intersectionPointWith(line)?.let { result.safeAdd(it) }
    }
    return result
}

fun Collection<LatLng>.forEachLine(action: (Pair<LatLng, LatLng>) -> Unit) {
    check(size > 1)
    forEachIndexed { index, latLng ->
        elementAtOrNull(index + 1)?.let {
            if (it != latLng) action(latLng to it)
        }
    }
}

infix fun Collection<LatLng>.intersectionPointsWith(other: List<LatLng>): List<LatLng>? {
    val poly1 = this.close()
    val poly2 = other.close()
    val clippedCorners = mutableListOf<LatLng>()

    poly1.forEach {
        if (it insideOf poly2)
            clippedCorners.safeAdd(it)
    }
    poly2.forEach {
        if (it insideOf poly1)
            clippedCorners.safeAdd(it)
    }
    poly1.forEachLine { line ->
        println("poly1 $line")
        clippedCorners.safeAdd(line intersectionPointsWith poly2)
    }
    if (clippedCorners.isEmpty())
        return null
    return clippedCorners.sortedClockwise()
}

private fun MutableCollection<LatLng>.safeAdd(vararg points: LatLng) { safeAdd(points.toList()) }
private fun MutableCollection<LatLng>.safeAdd(points: List<LatLng>) { points.forEach { if (!contains(it)) add(it) } }
