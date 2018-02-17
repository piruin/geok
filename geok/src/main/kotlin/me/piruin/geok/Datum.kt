package me.piruin.geok

class Datum(val equatorialRad: Double, val flat: Double) {
    companion object {
        val WSG48 = Datum(6378137.0, 298.257223563)
    }
}
