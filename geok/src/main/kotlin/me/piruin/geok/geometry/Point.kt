package me.piruin.geok.geometry

import me.piruin.geok.LatLng

data class Point(val coordinates: LatLng) : Geometry {
    constructor(latitude: Double, longitude: Double) : this(LatLng(latitude, longitude))

    override val type: String = javaClass.simpleName
}
