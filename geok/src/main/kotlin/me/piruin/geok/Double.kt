package me.piruin.geok

infix fun Double.and(second: Double) = Pair(this, second)

infix fun Double.between(values: Pair<Double, Double>): Boolean {
    return values.first <= this && this <= values.second
}

val Double.fractional
    get() = this - this.toInt()

val Double.wholeNum
    get() = (this - this.fractional).toInt()

fun Double.round(digitLength: Int): Double {
    val pow = 10.0.times(digitLength)
    return Math.round(this * pow) / pow
}
