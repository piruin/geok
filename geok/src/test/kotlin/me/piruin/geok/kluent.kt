package me.piruin.geok

import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame


infix fun Any?.`should be equal to`(expected: Any?) {
    assertEquals(expected, this)
}

infix fun Any?.`should not be equal to`(expected: Any?) {
    assertNotEquals(expected, this)
}

infix fun Any?.`should be`(expected: Any?) {
    assertSame(expected, this)
}

infix fun Any?.`should not be`(expected: Any?) {
    assertNotSame(expected, this)
}

infix fun Double.`should be equal to`(expected: Double?) {
    this.equalsTo(expected)
}

infix fun Double.`should not be equal to`(expected: Double?) {
    !this.equalsTo(expected)
}
