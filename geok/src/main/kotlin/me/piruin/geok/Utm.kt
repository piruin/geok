package me.piruin.geok

data class Utm(val zone: Int, val hemisphere: Char, val easting: Double, val northing: Double) {

    override fun toString(): String {
        return "$zone $hemisphere $easting, $northing"
    }
}
