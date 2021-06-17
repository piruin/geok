package me.piruin.geok.geometry

import me.piruin.geok.BBox
import me.piruin.geok.LatLng
import me.piruin.geok.length

data class LineString(val coordinates: List<LatLng>) : Geometry {

    constructor(vararg xyPair: Pair<Double, Double>) : this(xyPair.map { LatLng(it.second, it.first) })

    override val type: String = javaClass.simpleName
    val bbox: BBox = BBox.from(coordinates)

    init {
        require(coordinates.size > 1) { "LineString coordinates size should more than 1" }
    }

    val length: Double
        get() = coordinates.length
}
