package me.piruin.geok.geometry

import me.piruin.geok.LatLng

data class Point(val coordinates: LatLng) : Geometry {

    constructor(latitude: Double, longitude: Double) : this(LatLng(latitude, longitude))
    constructor(pair: Pair<Double, Double>) : this(LatLng(pair.second, pair.first))

    override val type: String = javaClass.simpleName
}
