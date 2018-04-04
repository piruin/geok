package me.piruin.geok.geometry

import me.piruin.geok.LatLng

data class Point(val coordinates: LatLng) : Geometry {
    override val type: String = "Point"

    constructor(latitude: Double, longitude: Double) : this(LatLng(latitude, longitude))
}
