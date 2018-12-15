package me.piruin.geok.geometry

import me.piruin.geok.BBox

class MultiPoint(geometries: List<Point>) : MultiGeometry<Point>(geometries) {

    constructor(vararg point: Point) : this(point.toList())

    val bbox: BBox = BBox.from(geometries.map { it.coordinates })
}
