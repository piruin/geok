package me.piruin.geok

data class LatLng(val lat: Double,
                  val lng: Double,
                  val elevation: Double = Double.NaN) {

    init {
        assert(lat between (-90.0 and 90.0)) { "latitude should between -90.0 and 90 [$lat]" }
        assert(lng between (-180.0 and 180.0)) { "longitude should between -180.0 and 180 [$lng]" }
    }

    override fun toString(): String {
        return "$lat, $lng${if (elevation != Double.NaN) ", $elevation" else ""}"
    }

    /**
     * This method port from Professor Steven Dutch's JavaScript "Convert Between Geographic and UTM Coordinates"
     *
     * @see <a href="http://www.uwgb.edu/dutchs/UsefulData/ConvertUTMNoOZ.HTM">Convert Between Geographic and UTM Coordinates</a>
     * @see <a href="http://www.uwgb.edu/dutchs/UsefulData/UTMFormulas.HTM">Converting UTM to Latitude and Longitude (Or Vice Versa)</a>
     *
     */
    fun toUtm(datum: Datum = Datum.WSG48): Utm {

        println(datum)

        assert(lat between (-80.0 to 84.0)) { "latitude $lat is outside utm grid" }

        val a = datum.equatorialRad
        println("a = $a")
        val f = 1.0 / datum.flat //polar flattening.
        println("f = $f")
        val b = datum.polarRad
        println("b = $b")


        val e = Math.sqrt(1 - (b / a) * (b / a))//eccentricity
        println("e= $e")

        val k0 = 0.9996 //scale on central meridian
        val latd = lat
        val lngd = lng

        val drad = Math.PI / 180

        val phi = latd * drad//Convert latitude to radians
        println("phi=$phi")
        val lng = lngd * drad//Convert longitude to radians
        val utmZone = 1 + Math.floor((lngd + 180) / 6)//calculate utm zone
        var latz = 0//Latitude zone: A-B S of -80, C-W -80 to +72, X 72-84, Y,Z N of 84
        if (latd > -80 && latd < 72) {
            latz = (Math.floor((latd + 80) / 8) + 2).toInt()
        }
        if (latd > 72 && latd < 84) {
            latz = 21
        }
        if (latd > 84) {
            latz = 23
        }

        val zcm = 3 + 6 * (utmZone - 1) - 180//Central meridian of zone
        println("zcm = " + zcm)
        //Calculate Intermediate Terms
        val e0 = e / Math.sqrt(1 - e * e)//Called e prime in reference
        val esq = (1 - (b / a) * (b / a))//e squared for use in expansions
        val e0sq = e * e / (1 - e * e)// e0 squared - always even powers
        println("$esq   "+e0sq)
        val N = a / Math.sqrt(1 - Math.pow(e * Math.sin(phi), 2.0))
        println(1-Math.pow(e*Math.sin(phi),2.0))
        println("N=  "+N)
        val T = Math.pow(Math.tan(phi), 2.0)
        println("T=  "+T)
        val C = e0sq * Math.pow(Math.cos(phi), 2.0)
        println("C=  "+C)
        val A = (lngd - zcm) * drad * Math.cos(phi)
        println("A=  "+A)
        //Calculate M
        var M = phi * (1.0 - esq * (1.0 / 4.0 + esq * (3.0 / 64.0 + 5 * esq / 256)))
        //println((3 / 64 + 5 * esq / 256))
        //println(((3 / 64) + (5.0 * esq / 256.0))) //modify
        //println((1 / 4 + esq * (3 / 64 + 5 * esq / 256)))
        //println((1.0 / 4.0 + esq * (3.0 / 64.0 + 5.0 * esq / 256.0)))
        //println((1 - esq * (1 / 4 + esq * (3 / 64 + 5 * esq / 256))))
        //println((1 - esq * (1.0 / 4.0 + esq * (3.0 / 64.0 + 5.0 * esq / 256.0))))
        println("m1 = $M")
        M = M - Math.sin(2.0 * phi) * (esq * (3.0 / 8.0 + esq * (3.0 / 32.0 + 45 * esq / 1024)))
        println("m2 = $M")
        M = M + Math.sin(4.0 * phi) * (esq * esq * (15.0 / 256.0 + esq * 45.0 / 1024))
        println("m3 = $M")
        M = M - Math.sin(6.0 * phi) * (esq * esq * esq * (35.0 / 3072))
        println("m4 = $M")
        M = M * a//Arc length along standard meridian
        println("m = $M")
        val M0 = 0//M0 is M for some origin latitude other than zero. Not needed for standard UTM
        //println("M    ="+M)
        //Calculate UTM Values
        var x = k0 * N * A * (1 + A * A * ((1 - T + C) / 6 + A * A * (5 - 18 * T + T * T + 72 * C - 58 * e0sq) / 120))//Easting relative to CM
        x = x + 500000//Easting standard
        var y = k0 * (M - M0 + N * Math.tan(phi)
          * (A * A * (1.0 / 2.0 + A * A * ((5.0 - T + (9.0 * C) + (4.0 * C * C)) / 24.0 + A * A * (61.0 - (58.0 * T) + (T * T) + (600.0 * C) - (330.0 * e0sq)) / 720))))//Northing from equator
        val yg = y + 10000000//yg = y global, from S. Pole
        if (lat < 0) {
            y += 10000000.0
        }

        return Utm(utmZone.toInt(), if (phi > 0) 'N' else 'S', x.round(1), y.round(1))
    }

}

