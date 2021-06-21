package me.piruin.geok

import org.amshove.kluent.`should be`
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class BBoxIntersectTest(val line1: Pair<LatLng, LatLng>, val line2: Pair<LatLng, LatLng>, val asExpected: Boolean) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            arrayOf(LatLng(0 to 0) to LatLng(5 to 5), LatLng(1 to 1) to LatLng(2 to 2), true),
            arrayOf(LatLng(0 to 0) to LatLng(3 to 3), LatLng(1 to -1) to LatLng(2 to 7), true),
            arrayOf(LatLng(0 to 0) to LatLng(3 to 3), LatLng(1 to -1) to LatLng(2 to 2), true),
            arrayOf(LatLng(0 to 0) to LatLng(3 to 3), LatLng(3 to 3) to LatLng(5 to 5), true),
            arrayOf(LatLng(0 to 0) to LatLng(3 to 3), LatLng(4 to 4) to LatLng(5 to 5), false),
        )
    }
    @Test
    fun line() {
        line1 bboxIntersectWith line2 `should be` asExpected
    }

    @Test
    fun bbox() {
        BBox.from(line1) intersectWith BBox.from(line2) `should be` asExpected
    }
}
