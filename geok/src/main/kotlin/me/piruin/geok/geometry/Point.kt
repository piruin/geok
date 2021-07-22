package me.piruin.geok.geometry

import me.piruin.geok.LatLng

data class Point(val coordinates: LatLng) : Geometry {

    constructor(latitude: Double, longitude: Double) : this(LatLng(latitude, longitude))
    constructor(pair: Pair<Double, Double>) : this(LatLng(pair.second, pair.first))

    override val type: String = this::class.simpleName!!

    /**
     * return Determines whether this Point are inside of given Polygon.
     */
    infix fun insideOf(polygon: Polygon) = coordinates.insideOf(polygon.boundary)

    /**
     * return Determines whether this Point are inside of given MultiPolygon.
     */
    infix fun insideOf(polygons: MultiPolygon) = polygons.polygons.any { insideOf(it) }

    infix fun distanceTo(other: Point) = coordinates distanceTo other.coordinates
}
