package me.piruin.geok

import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame

infix fun Any?.shouldBeEqualTo(expected: Any?) {
    assertEquals(expected, this)
}

infix fun Any?.shouldNotBeEqualTo(expected: Any?) {
    assertNotEquals(expected, this)
}

infix fun Any?.shouldBe(expected: Any?) {
    assertSame(expected, this)
}

infix fun Any?.shouldNotBe(expected: Any?) {
    assertNotSame(expected, this)
}

infix fun Double.shouldBeEqualTo(expected: Double?) {
    this.equalsTo(expected)
}

infix fun Double.shouldNotBeEqualTo(expected: Double?) {
    !this.equalsTo(expected)
}
