package me.piruin.geok.geometry

import me.piruin.geok.BBox

interface MultiGeometry : Geometry {
    val bbox: BBox
}
