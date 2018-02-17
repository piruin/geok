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

    fun toUtm(): Utm {

        assert(lat between (-80.0 to 84.0)) { "latitude $lat outside utm grid" }

        val datum = Datum.WSG48
        val a = datum.equatorialRad
        val f = 1 / datum.flat
        val b = a * (1 - f)//polar axis
        println("b = $b")

        val e = Math.sqrt(1 - (b / a) * (b / a))//eccentricity
        println("e $e")

        val k0 = 0.9996 //scale on central meridian
        val latd = lat
        val lngd = lng

        val drad = Math.PI / 180

        val phi = latd * drad//Convert latitude to radians
        val lng = lngd * drad//Convert longitude to radians
        val utmz = 1 + Math.floor((lngd + 180) / 6)//calculate utm zone
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

        val zcm = 3 + 6 * (utmz - 1) - 180//Central meridian of zone
        //alert(utmz + "   " + zcm)
        //Calculate Intermediate Terms
        val e0 = e / Math.sqrt(1 - e * e)//Called e prime in reference
        val esq = (1 - (b / a) * (b / a))//e squared for use in expansions
        val e0sq = e * e / (1 - e * e)// e0 squared - always even powers
        //alert(esq+"   "+e0sq)
        val N = a / Math.sqrt(1 - Math.pow(e * Math.sin(phi), 2.0))
        //alert(1-Math.pow(e*Math.sin(phi),2))
        //alert("N=  "+N)
        val T = Math.pow(Math.tan(phi), 2.0)
        //alert("T=  "+T)
        val C = e0sq * Math.pow(Math.cos(phi), 2.0)
        //alert("C=  "+C)
        val A = (lngd - zcm) * drad * Math.cos(phi)
        //alert("A=  "+A)
        //Calculate M
        var M = phi * (1 - esq * (1 / 4 + esq * (3 / 64 + 5 * esq / 256)))
        M = M - Math.sin(2 * phi) * (esq * (3 / 8 + esq * (3 / 32 + 45 * esq / 1024)))
        M = M + Math.sin(4 * phi) * (esq * esq * (15 / 256 + esq * 45 / 1024))
        M = M - Math.sin(6 * phi) * (esq * esq * esq * (35 / 3072))
        M = M * a//Arc length along standard meridian
        //alert(a*(1 - esq*(1/4 + esq*(3/64 + 5*esq/256))))
        //alert(a*(esq*(3/8 + esq*(3/32 + 45*esq/1024))))
        //alert(a*(esq*esq*(15/256 + esq*45/1024)))
        //alert(a*esq*esq*esq*(35/3072))
        //alert(M)
        val M0 = 0//M0 is M for some origin latitude other than zero. Not needed for standard UTM
        //alert("M    ="+M)
        //Calculate UTM Values
        var x = k0 * N * A * (1 + A * A * ((1 - T + C) / 6 + A * A * (5 - 18 * T + T * T + 72 * C - 58 * e0sq) / 120))//Easting relative to CM
        x = x + 500000//Easting standard
        var y = k0 * (M - M0 + N * Math.tan(phi) * (A * A * (1 / 2 + A * A * ((5 - T + 9 * C + 4 * C * C) / 24 + A * A * (61 - 58 * T + T * T + 600 * C - 330 * e0sq) / 720))))//Northing from equator
        val yg = y + 10000000//yg = y global, from S. Pole
        if (lat < 0) {
            y += 10000000.0
        }

        return Utm(utmz.toInt(), if (phi > 0) 'N' else 'S', x, y)
    }
}

data class Utm(val zone: Int, val hemispare: Char, val easting: Double, val northing: Double) {

    override fun toString(): String {
        return "$zone $hemispare $easting, $northing"
    }
}

private infix fun Double.and(second: Double) = Pair(this, second)

private infix fun Double.between(values: Pair<Double, Double>): Boolean {
    return values.first <= this && this <= values.second
}

