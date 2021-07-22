package me.piruin.geok

import kotlin.test.Test

class IntersectionLineTest {

    @Test
    fun test1() {
        doTest(
            LatLng(-5 to 0) to LatLng(5 to 0), // T1
            LatLng(0 to -5) to LatLng(0 to 5),
            LatLng(0 to 0) to LatLng(0 to 0)
        )
    }

    @Test
    fun test2() {
        doTest(
            LatLng(0 to 0) to LatLng(10 to 10), // T2
            LatLng(2 to 2) to LatLng(16 to 4),
            LatLng(2 to 2) to LatLng(2 to 2)
        )
    }

    @Test
    fun test3() {
        doTest(
            LatLng(-2 to 2) to LatLng(-2 to -2), // T3
            LatLng(-2 to 0) to LatLng(0 to 0),
            LatLng(-2 to 0) to LatLng(-2 to 0)
        )
    }

    @Test
    fun test4() {
        doTest(
            LatLng(0 to 4) to LatLng(4 to 4), // T4
            LatLng(4 to 0) to LatLng(4 to 8),
            LatLng(4 to 4) to LatLng(4 to 4)
        )
    }

    @Test
    fun test5() {
        doTest(
            LatLng(0 to 0) to LatLng(10 to 10), // T5
            LatLng(2 to 2) to LatLng(6 to 6),
            LatLng(2 to 2) to LatLng(6 to 6)
        )
    }

    @Test
    fun test6() {
        doTest(
            LatLng(8 to 10) to LatLng(16 to -2), // T6
            LatLng(8 to 10) to LatLng(16 to -2),
            LatLng(8 to 10) to LatLng(16 to -2)
        )
    }

    @Test
    fun test7() {
        doTest(
            LatLng(4 to 4) to LatLng(12 to 12), // F1
            LatLng(6 to 8) to LatLng(8 to 10),
            null
        )
    }

    @Test
    fun test8() {
        doTest(
            LatLng(-2 to -2) to LatLng(4 to 4), // F2
            LatLng(6 to 6) to LatLng(12 to 12),
            null
        )
    }

    @Test
    fun test9() {
        doTest(
            LatLng(0 to 0) to LatLng(2 to 2), // F3
            LatLng(1 to 4) to LatLng(4 to 0),
            null
        )
    }

    @Test
    fun test10() {
        doTest(
            LatLng(2 to 2) to LatLng(8 to 2), // F4
            LatLng(4 to 4) to LatLng(6 to 4),
            null
        )
    }

    @Test
    fun test11() {
        doTest(
            LatLng(0 to 8) to LatLng(10 to 0), // F5
            LatLng(4 to 2) to LatLng(4 to 4),
            null
        )
    }

    @Test
    fun test12() {
        doTest(
            LatLng(0 to 0) to LatLng(10 to 0),
            LatLng(5 to -5) to LatLng(3 to 5),
            LatLng(4 to 0) to LatLng(4 to 0)
        )
    }

    @Test
    fun test13() {
        doTest(
            LatLng(0 to 0) to LatLng(0 to 10),
            LatLng(-1 to 5) to LatLng(5 to 4),
            LatLng(0 to 4.833333333333333) to LatLng(0 to 4.833333333333333)
        )
    }

    @Test
    fun test14() {
        doTest(
            LatLng(100.86491525173187 to 12.664525324943511) to LatLng(100.86367607116699 to 12.664525324943511),
            LatLng(100.86390137672424 to 12.666775893853327) to LatLng(100.86381554603577 to 12.66403857138034),
            LatLng(100.86383080853912 to 12.664525324943511) to LatLng(100.86383080853912 to 12.664525324943511)
        )
    }

    private fun doTest(
        line1: Pair<LatLng, LatLng>,
        line2: Pair<LatLng, LatLng>,
        exprectedLine: Pair<LatLng, LatLng>?
    ) {
        line1.segmentIntersectionWith(line2) `should be equal to` exprectedLine
    }
}
