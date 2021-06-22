package me.piruin.geok

import me.piruin.geok.geometry.Polygon
import org.amshove.kluent.`should be equal to`
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class IntersectionPolygonTest(val other: Polygon, val result: Polygon?) {

    val main: Polygon = Polygon(
        100.86390137672424 to 12.666775893853327,
        100.86381554603577 to 12.66403857138034,
        100.86470067501068 to 12.664028103551571,
        100.86470067501068 to 12.666791595427096,
        100.86390137672424 to 12.666775893853327,
    )

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            arrayOf(
                Polygon(
                    100.86330592632294 to 12.666812530857289,
                    100.86330592632294 to 12.66575528948397,
                    100.86316108703612 to 12.665509297050555,
                    100.86315035820007 to 12.665326111041706,
                    100.86327910423279 to 12.665038247047587,
                    100.86326837539673 to 12.664059507036592,
                    100.86385846138 to 12.664043805294572,
                    100.8639121055603 to 12.666786361569274,
                    100.86330592632294 to 12.666812530857289,
                ),
                Polygon(
                    100.86385846138 to 12.664043805294572,
                    100.86381574578868 to 12.664044941924203,
                    100.86390137672424 to 12.666775893853327,
                    100.86391190485793 to 12.666776100670068,
                    100.86385846138 to 12.664043805294572,
                )
            ),
            arrayOf(
                Polygon(
                    100.8640730381012 to 12.666545603993596,
                    100.8640730381012 to 12.66635718486273,
                    100.86460947990416 to 12.666351950996,
                    100.86462020874023 to 12.666503733087668,
                    100.8640730381012 to 12.666545603993596,
                ),
                Polygon(
                    100.8640730381012 to 12.666545603993596,
                    100.8640730381012 to 12.66635718486273,
                    100.86460947990416 to 12.666351950996,
                    100.86462020874023 to 12.666503733087668,
                    100.8640730381012 to 12.666545603993596,
                )
            ),
            arrayOf(
                Polygon(
                    100.86367607116699 to 12.664525324943511,
                    100.86368143558502 to 12.664138015732222,
                    100.86489379405974 to 12.664096144430898,
                    100.86491525173187 to 12.664525324943511,
                    100.86367607116699 to 12.664525324943511,
                ),
                Polygon(
                    100.8638185157352 to 12.664133281386057,
                    100.86383080853912 to 12.664525324943511,
                    100.86470067501068 to 12.664525324943511,
                    100.86470067501068 to 12.664102814195711,
                    100.8638185157352 to 12.664133281386057,
                )
            )
        )
    }

    @Test
    fun intersectPolygon() {
        main.intersectionWith(other) `should be equal to` result
    }
}