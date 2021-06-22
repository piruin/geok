package me.piruin.geok.geometry

import me.piruin.geok.BBox

@Deprecated("Replace with GeometryCollection", ReplaceWith("GeometryCollection<*>"))
interface MultiGeometry : Geometry {

    val bbox: BBox
}
