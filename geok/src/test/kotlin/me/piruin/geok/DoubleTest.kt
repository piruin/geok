/*
 * Copyright (c) 2018 Piruin Panichphol
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package me.piruin.geok

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
