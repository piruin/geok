package me.piruin.geok

import java.util.*

class Polygon(val boundary: MutableList<LatLng>,
              val holes: MutableList<Polygon> = arrayListOf()) {

  constructor(vararg latlngs: LatLng): this(latlngs.toMutableList())

  fun contain(coord: LatLng): Boolean {
    if (boundary.contains(coord)) {
      return true
    }
    return holes.any { it.contain(coord) }
  }

  fun area(earthRadius: Double = Datum.WSG48.equatorialRad): Double {
    if (boundary.size < 3) { return 0.0 }

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

  val isClosed: Boolean
    get() = boundary[0] == boundary.last()

}
