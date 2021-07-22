package me.piruin.geok

data class BBox(
    val left: Double,
    val bottom: Double,
    val right: Double,
    val top: Double
) {

    init {
        require(left <= right) { "left[$left] must less than or eq right[$right] " }
        require(bottom <= top) { "bottom[$bottom] must less than or eq top[$top]" }
    }

    companion object {

        fun combine(bbox: List<BBox>): BBox {
            val first = bbox[0]
            val point = mutableListOf(first.left, first.bottom, first.right, first.top)
            bbox.forEach {
                if (it.left < point[0])
                    point[0] = it.left
                if (it.bottom < point[1])
                    point[1] = it.bottom
                if (it.right > point[2])
                    point[2] = it.right
                if (it.top > point[3])
                    point[3] = it.top
            }
            return BBox(point[0], point[1], point[2], point[3])
        }

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

        fun from(line: Pair<LatLng, LatLng>) = from(listOf(line.first, line.second))
    }

    fun contains(coordinate: LatLng): Boolean {
        val x = coordinate.x in left..right
        val y = coordinate.y in bottom..top
        return x && y
    }

    fun contains(coordinates: Pair<Number, Number>): Boolean = contains(LatLng(coordinates.first to coordinates.second))

    infix fun intersectWith(other: BBox): Boolean {
        return left <= other.right &&
            right >= other.left &&
            bottom <= other.top &&
            top >= other.bottom
    }
}

private fun LatLng.toBboxPoint() = mutableListOf(longitude, latitude, longitude, latitude)
