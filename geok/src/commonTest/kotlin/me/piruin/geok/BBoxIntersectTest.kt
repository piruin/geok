package me.piruin.geok

import kotlin.test.Test

class BBoxIntersectTest {

    @Test
    fun test1() {
        doTest(LatLng(0 to 0) to LatLng(5 to 5), LatLng(1 to 1) to LatLng(2 to 2), true)
    }

    @Test
    fun test2() {
        doTest(LatLng(0 to 0) to LatLng(3 to 3), LatLng(1 to -1) to LatLng(2 to 7), true)
    }

    @Test
    fun test3() {
        doTest(LatLng(0 to 0) to LatLng(3 to 3), LatLng(1 to -1) to LatLng(2 to 2), true)
    }

    @Test
    fun test4() {
        doTest(LatLng(0 to 0) to LatLng(3 to 3), LatLng(3 to 3) to LatLng(5 to 5), true)
    }

    @Test
    fun test5() {
        doTest(LatLng(0 to 0) to LatLng(3 to 3), LatLng(4 to 4) to LatLng(5 to 5), false)
    }

    private fun doTest(line1: Pair<LatLng, LatLng>, line2: Pair<LatLng, LatLng>, asExpected: Boolean) {
        line1 bboxIntersectWith line2 shouldBe asExpected
        BBox.from(line1) intersectWith BBox.from(line2) shouldBe asExpected
    }
}
