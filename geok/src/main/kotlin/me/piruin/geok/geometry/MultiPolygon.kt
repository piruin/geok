package me.piruin.geok.geometry

import me.piruin.geok.BBox

data class MultiPolygon(val polygons: List<Polygon>) : MultiGeometry {

    constructor(vararg polygons: Polygon) : this(polygons.toList())

    override val type: String = javaClass.simpleName
    override val bbox: BBox = BBox.combine(polygons.map { it.bbox })
}
