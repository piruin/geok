package me.piruin.geok.geometry

import me.piruin.geok.BBox

data class MultiPoint(val geometries: List<Point>) : MultiGeometry {

    constructor(vararg point: Point) : this(point.toList())

    override val type: String = javaClass.simpleName
    override val bbox: BBox = BBox.from(geometries.map { it.coordinates })
}
