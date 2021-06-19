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

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToLong

infix fun Double.and(second: Double) = Pair(this, second)

infix fun Double.between(values: Pair<Double, Double>): Boolean {
    return values.first <= this && this <= values.second
}

fun Double.toRadians() = Math.toRadians(this)

val Double.fractional
    get() = this - this.toInt()

val Double.wholeNum
    get() = (this - this.fractional).toInt()

fun Double.round(digitLength: Int): Double {
    val pow = 10.0.pow(digitLength)
    return (this * pow).roundToLong() / pow
}

fun Double.equalsTo(other: Double, delta: Double = 0.000000001): Boolean = abs(this - other) <= delta
