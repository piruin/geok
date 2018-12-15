package me.piruin.geok.geometry

abstract class MultiGeometry<T : Geometry>(val geometries: List<T>) {

    constructor(vararg geometry: T) : this(geometry.toList())

    val type: String = javaClass.simpleName
}

