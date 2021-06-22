package me.piruin.geok.geometry

import me.piruin.geok.BBox

data class MultiLineString(override val coordinates: List<LineString>) : GeometryCollection<LineString>, MultiGeometry {

    constructor(vararg lineString: LineString) : this(lineString.toList())

    override val type: String = javaClass.simpleName
    override val bbox: BBox = BBox.from(coordinates.flatMap { it.coordinates })

    val lineString: List<LineString>
        get() = coordinates
}
