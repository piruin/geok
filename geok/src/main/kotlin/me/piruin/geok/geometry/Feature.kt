package me.piruin.geok.geometry

class Feature<T>(var geometry: Geometry, var properties: T) {
    val type = "Feature"
}
