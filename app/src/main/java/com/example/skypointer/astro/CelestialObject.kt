package com.example.skypointer.astro

data class CelestialObject(
    val name: String,
    val raDeg: Double,
    val decDeg: Double,
    val magnitude: Double
) {
    init {
        require(raDeg >= 0.0 && raDeg < 360.0) { "RA must be in [0, 360), got $raDeg for $name" }
        require(decDeg >= -90.0 && decDeg <= 90.0) { "Dec must be in [-90, 90], got $decDeg for $name" }
    }
}