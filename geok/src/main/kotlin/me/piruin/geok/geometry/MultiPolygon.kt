package me.piruin.geok.geometry

import me.piruin.geok.BBox

class MultiPolygon(geometries: List<Polygon>) : MultiGeometry<Polygon>(geometries) {

    constructor(vararg polygons: Polygon) : this(polygons.toList())

    val bbox: BBox? = BBox.combine(geometries.mapNotNull { it.bbox })
}
