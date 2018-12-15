package me.piruin.geok.geometry

import me.piruin.geok.BBox

class MultiLineString(geometries: List<LineString>) : MultiGeometry<LineString>(geometries) {

    constructor(vararg lineString: LineString) : this(lineString.toList())

    val bbox: BBox = BBox.from(geometries.flatMap { it.coordinates })
}
