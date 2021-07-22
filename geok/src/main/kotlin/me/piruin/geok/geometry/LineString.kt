package me.piruin.geok.geometry

import me.piruin.geok.BBox
import me.piruin.geok.LatLng
import me.piruin.geok.centroid
import me.piruin.geok.distance

data class LineString(val coordinates: List<LatLng>) : Geometry {

    constructor(vararg xyPair: Pair<Double, Double>) : this(xyPair.map { LatLng(it.second, it.first) })

    override val type: String = this::class.simpleName!!
    val bbox: BBox = BBox.from(coordinates)

    init {
        require(coordinates.size > 1) { "LineString coordinates size should more than 1" }
    }

    @Deprecated("Replace with distance", replaceWith = ReplaceWith("distance"))
    val length: Double
        get() = coordinates.distance

    /**
     * @return meter of distance from point to point in this LineString
     */
    val distance: Double
        get() = coordinates.distance

    /**
     * @return centroid coordination of this Polygon
     */
    val centroid: LatLng
        get() = coordinates.centroid
}
