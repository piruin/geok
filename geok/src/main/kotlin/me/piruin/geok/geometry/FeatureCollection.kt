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

package me.piruin.geok.geometry

import me.piruin.geok.BBox
import me.piruin.geok.LatLng
import java.lang.IllegalStateException

data class FeatureCollection<T>(
        val features: List<Feature<T>>
) {
    constructor(vararg features: Feature<T>) : this(features.toList())

    val type = "FeatureCollection"
    val bbox: BBox?

    init {
        require(features.isNotEmpty()) { "Feature Collection should not empty" }

        val latLng = features.toLatLngs()
        bbox = if (latLng.size > 1) BBox.from(latLng) else null
    }

    private fun List<Feature<T>>.toLatLngs(): List<LatLng> {
        return flatMap {
            when (it.geometry) {
                is Polygon -> it.geometry.boundary
                is Point -> listOf(it.geometry.coordinates)
                else -> throw IllegalStateException("Not support ${it.type}")
            }
        }
    }
}
