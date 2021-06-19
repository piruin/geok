package me.piruin.geok.geometry

import me.piruin.geok.BBox
import me.piruin.geok.Datum
import me.piruin.geok.LatLng
import me.piruin.geok.centroid

data class MultiPolygon(val polygons: List<Polygon>) : MultiGeometry {

    constructor(vararg polygons: Polygon) : this(polygons.toList())

    override val type: String = javaClass.simpleName
    override val bbox: BBox = BBox.combine(polygons.map { it.bbox })

    /**
     * @return sum of area of polygons in square meter (m^2)
     */
    fun area(earthRadius: Double = Datum.WSG84.equatorialRad): Double {
        return polygons.fold(0.0) { area, polygon -> area + polygon.area(earthRadius) }
    }

    /**
     * @return centroid coordination of this Polygon
     */
    val centroid: LatLng
        get() = polygons.map { centroid }.centroid
}
