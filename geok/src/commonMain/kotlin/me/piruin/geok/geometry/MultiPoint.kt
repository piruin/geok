package me.piruin.geok.geometry

import me.piruin.geok.BBox
import me.piruin.geok.LatLng
import me.piruin.geok.centroid

data class MultiPoint(override val coordinates: List<Point>) : GeometryCollection<Point>, MultiGeometry {

    constructor(vararg point: Point) : this(point.toList())

    override val type: String = this::class.simpleName!!
    override val bbox: BBox = BBox.from(coordinates.map { it.coordinates })

    val points: List<Point>
        get() = coordinates

    /**
     * @return centroid coordination of this Polygon
     */
    val centroid: LatLng
        get() = coordinates.map { it.coordinates }.centroid
}
