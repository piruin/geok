package me.piruin.geok.geometry

import me.piruin.geok.Datum
import me.piruin.geok.LatLng
import me.piruin.geok.toRadians

class Polygon(val boundary: MutableList<LatLng>,
              val holes: MutableList<Polygon> = arrayListOf()) : Geometry {

    override val type: String = "Polygon"

    constructor(vararg latlngs: LatLng) : this(latlngs.toMutableList())

    constructor(vararg xyPair: Pair<Double, Double>) :
      this(xyPair.map { LatLng(it.second, it.first) }.toMutableList())

    val isClosed: Boolean
        get() = boundary[0] == boundary.last()

    fun contain(coordinate: LatLng): Boolean {
        if (boundary.contains(coordinate)) {
            return true
        }
        return holes.any { it.contain(coordinate) }
    }

    fun area(earthRadius: Double = Datum.WSG48.equatorialRad): Double {
        if (boundary.size < 3) {
            return 0.0
        }

        val diameter = earthRadius * 2
        val circumference = diameter * Math.PI
        val ySegment = ArrayList<Double>()
        val xSegment = ArrayList<Double>()

        // calculateArea segment x and y in degrees for each point
        val latitudeRef = boundary[0].latitude
        val longitudeRef = boundary[0].longitude
        for (i in 1 until boundary.size) {
            val latitude = boundary[i].latitude
            val longitude = boundary[i].longitude
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
        var areasSum = Math.abs(triangleArea.sum())
        holes.forEach { areasSum -= it.area() }
        return areasSum
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
