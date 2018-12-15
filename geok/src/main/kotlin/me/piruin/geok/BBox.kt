package me.piruin.geok

import me.piruin.geok.geometry.Polygon

class BBox(
        var left: Double,
        var bottom: Double,
        var right: Double,
        var top: Double) {

    companion object {

        fun fromLatLngs(latLngs: List<LatLng>): BBox {
            var point = mutableListOf(0.0, 0.0, 0.0, 0.0)
            latLngs.forEach {
                if (it.longitude < point[0])
                    point[0] = it.longitude
                if (it.latitude < point[1])
                    point[1] = it.latitude
                if (it.longitude > point[2])
                    point[2] = it.longitude
                if (it.latitude > point[3])
                    point[3] = it.latitude
            }
            return BBox(point[0], point[1], point[2], point[3])
        }

        fun fromPolygon(polygon: Polygon) = fromLatLngs(polygon.boundary)
    }
}
