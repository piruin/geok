package me.piruin.geok.geometry

import me.piruin.geok.BBox

data class MultiPoint(val points: List<Point>) : MultiGeometry {

    constructor(vararg point: Point) : this(point.toList())

    override val type: String = javaClass.simpleName
    override val bbox: BBox = BBox.from(points.map { it.coordinates })
}
