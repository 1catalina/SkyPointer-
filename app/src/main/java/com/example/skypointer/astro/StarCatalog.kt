package com.example.skypointer.astro

object StarCatalog {
    val stars = listOf(
        CelestialObject("Sirius", AstroMath.raHmsToDeg(6, 45, 8.9), -16.7161, -1.46),
        CelestialObject("Arcturus", AstroMath.raHmsToDeg(14, 15, 39.7), 19.1825, -0.05),
        CelestialObject("Vega", AstroMath.raHmsToDeg(18, 36, 56.3), 38.7836, 0.03),
        CelestialObject("Capella", AstroMath.raHmsToDeg(5, 16, 41.4), 45.9981, 0.08),
        CelestialObject("Rigel", AstroMath.raHmsToDeg(5, 14, 32.3), -8.2017, 0.13),
        CelestialObject("Betelgeuse", AstroMath.raHmsToDeg(5, 55, 10.3), 7.4069, 0.42),
        CelestialObject("Deneb", AstroMath.raHmsToDeg(20, 41, 25.9), 45.2803, 1.25),
        CelestialObject("Polaris", AstroMath.raHmsToDeg(2, 31, 49.1), 89.2642, 1.98)
    )
}