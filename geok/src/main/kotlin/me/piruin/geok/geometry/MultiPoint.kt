package me.piruin.geok.geometry

import me.piruin.geok.BBox
import me.piruin.geok.LatLng
import me.piruin.geok.centroid

data class MultiPoint(val points: List<Point>) : MultiGeometry {

    constructor(vararg point: Point) : this(point.toList())

    override val type: String = javaClass.simpleName
    override val bbox: BBox = BBox.from(points.map { it.coordinates })

    /**
     * @return centroid coordination of this Polygon
     */
    val centroid: LatLng
        get() = points.map { it.coordinates }.centroid
}
