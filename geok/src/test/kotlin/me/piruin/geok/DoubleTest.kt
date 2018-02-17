package me.piruin.geok

import junit.framework.Assert
import junit.framework.Assert.assertEquals
import org.amshove.kluent.`should equal`
import org.junit.Test

class DoubleTest {

    @Test
    fun roundDigit() {
        1816560.792879214.round(1) `should equal` 1816560.8
    }

    @Test
    fun wholeNumber() {
        102.841838.wholeNum `should equal` 102
    }

    @Test
    fun fragtional() {
        16.423976.fractional.shouldEqual(0.423976)
        102.84183.fractional.shouldEqual(0.841838)
    }

    @Test
    fun round() {
        Math.round(102.841838) `should equal` 103
    }

    @Test
    fun floor() {
        Math.floor(102.841838) `should equal` 102.0
    }

    @Test
    fun div() {
        3/4 `should equal` 0
        3.0/4.0 `should equal` 0.75
    }

    fun Double.shouldEqual(expected: Double, delta: Double = 0.00001): Double = this.apply { assertEquals(this, expected, delta) }

}
