package me.piruin.geok

data class Datum(val equatorialRad: Double, val polarRad: Double, val flat: Double) {

    override fun toString(): String {
        return "Equatorial Radius (meters) = $equatorialRad, Polar Radius (meters) = $polarRad, Flattening = ${1.0 / flat} 1/Flattening = $flat"
    }

    companion object {
        val WSG48 = Datum(6378137.0, 6356752.314247833, 298.257223563)
    }
}
