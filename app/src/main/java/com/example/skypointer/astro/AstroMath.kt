package com.example.skypointer.astro

import androidx.compose.ui.graphics.colorspace.Illuminant
import java.time.Instant
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

data class AzAlt(val azimuthDeg: Double, val altitudeDeg: Double)

object AstroMath {

    /// Normalize a degree between [0, 360)
    fun normalizeDegree(x: Double): Double {
        require(x.isFinite()) { "Angle must be a finite number, got $x" }
        val r = x % 360.0
        return if (r < 0) r + 360.0 else r
    }

    // Converts RA in degrees
    // 24hrs = 360 degrees  ->  HA = hours * 15
    fun hoursToDeg(h: Double): Double {
        require(h.isFinite()) { "Hour must be a finite number, got $h" }
        require(h >= 0.0 && h < 24.0) { "RA hours must be in [0, 24), got $h" }
        return h * 15.0
    }

    // Converts time (in h/m/s) in degrees
    fun raHmsToDeg(h: Int, m: Int, s: Double): Double {
        require(m >= 0 && m < 60) { "Minutes must be in 0..59, got $m" }
        require(s >= 0.0 && s < 60.0) { "Seconds must be in [0, 60), got $s" }
        return hoursToDeg(h + m / 60.0 + s / 3600.0)
    }

    // Calculate julian date for UTC
    fun julianDate(utc: Instant) : Double = utc.toEpochMilli()/86_400_000.0 + 2_440_587.5

    // Calculate Greenwich in degrees
    fun gmstDeg(jd: Double): Double {
        // 1900-01-01 <= date < 2100-01-01
        require(jd >= 2_415_020.5 && jd < 2_488_069.5) { "Julian date out of supported range, got $jd" }
        return normalizeDegree(280.46061837 + 360.98564736629 * (jd - 2_451_545.0))
    }

    fun lstDeg(jd: Double, longitudeDeg: Double): Double {
        require(longitudeDeg >= -180.0 && longitudeDeg <= 180.0) { "Longitude must be in [-180, 180], got $longitudeDeg" }
        return normalizeDegree(gmstDeg(jd) + longitudeDeg)
    }


    // Converts Equatorial Degrees (RA/Dec) to Local Degrees
    fun raDecToAzAlt(
        raDeg: Double,
        decDeg: Double,
        latDeg: Double,
        lonDeg: Double,
        utc: Instant
    ): AzAlt {
        require(raDeg >= 0.0 && raDeg < 360.0) { "RA must be in [0, 360), got $raDeg" }
        require(decDeg >= -90.0 && decDeg <= 90.0) { "Dec must be in [-90, 90], got $decDeg" }
        require(latDeg >= -90.0 && latDeg <= 90.0) { "Latitude must be in [-90, 90], got $latDeg" }

        val jd = julianDate(utc)
        val haRad = Math.toRadians(normalizeDegree(lstDeg(jd, lonDeg) - raDeg))
        val dec = Math.toRadians(decDeg)
        val lat = Math.toRadians(latDeg)

        val sinAlt = sin(dec) * sin(lat) + cos(dec) * cos(lat) * cos(haRad)
        val alt = asin(sinAlt.coerceIn(-1.0, 1.0))

        val az = atan2(
            -cos(dec) * sin(haRad),
            sin(dec) * cos(lat) - cos(dec) * sin(lat) * cos(haRad)
        )

        return AzAlt(normalizeDegree(Math.toDegrees(az)), Math.toDegrees(alt))
    }


}