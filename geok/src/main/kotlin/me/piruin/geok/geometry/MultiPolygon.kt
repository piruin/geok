package me.piruin.geok.geometry

import me.piruin.geok.BBox
import me.piruin.geok.Datum
import me.piruin.geok.LatLng
import me.piruin.geok.centroid

data class MultiPolygon(override val coordinates: List<Polygon>) : GeometryCollection<Polygon>, MultiGeometry {

    constructor(vararg polygons: Polygon) : this(polygons.toList())

    override val type: String = javaClass.simpleName
    override val bbox: BBox = BBox.combine(coordinates.map { it.bbox })

    val polygons: List<Polygon>
        get() = coordinates

    /**
     * @return sum of area of polygons in square meter (m^2)
     */
    fun area(earthRadius: Double = Datum.WSG84.equatorialRad): Double {
        return coordinates.fold(0.0) { area, polygon -> area + polygon.area(earthRadius) }
    }

    /**
     * @return centroid coordination of this Polygon
     */
    val centroid: LatLng
        get() = coordinates.map { centroid }.centroid
}
