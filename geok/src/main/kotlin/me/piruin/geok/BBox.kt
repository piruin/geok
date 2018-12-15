package me.piruin.geok

data class BBox(
        var left: Double,
        var bottom: Double,
        var right: Double,
        var top: Double) {

    init {
        require(left <= right) { "left[$left] must less than or eq right[$right] " }
        require(bottom <= top) { "bottom[$bottom] must less than or eq top[$top]" }
    }

    companion object {

        fun from(latLngs: List<LatLng>): BBox {
            val point = latLngs[0].toBboxPoint()
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

        fun from(vararg latLngs: LatLng) = from(latLngs.toList())
    }
}

private fun LatLng.toBboxPoint() = mutableListOf(longitude, latitude, longitude, latitude)
