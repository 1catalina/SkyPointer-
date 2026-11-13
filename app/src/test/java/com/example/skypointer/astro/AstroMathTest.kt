package com.example.skypointer.astro

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.Instant

class AstroMathTest {

    private val time = Instant.parse("2026-06-15T21:00:00Z")
    private val j2000 = 2_451_545.0

    // normalizeDegree
    @Test
    fun normalizeDegreeTest() {
        /// x = Nan
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.normalizeDegree(Double.NaN)
        }

        /// x = + infinity
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.normalizeDegree(Double.POSITIVE_INFINITY)
        }

        /// x = - infinity
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.normalizeDegree(Double.NEGATIVE_INFINITY)
        }

        /// r<0
        assertEquals(350.0, AstroMath.normalizeDegree(-10.0), 1e-9)

        ///r>0
        assertEquals(10.0, AstroMath.normalizeDegree(370.0), 1e-9)
    }

    // hoursToDeg
    @Test
    fun convertsHoursToDegreesTest() {

        /// Nan case
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.hoursToDeg(Double.NaN)
        }

        /// +inf
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.hoursToDeg(Double.POSITIVE_INFINITY)
        }

        /// - inf
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.hoursToDeg(Double.NEGATIVE_INFINITY)
        }

        /// h<0
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.hoursToDeg(-10.0)
        }

        /// h=0
        assertEquals(0.0, AstroMath.hoursToDeg(0.0), 1e-9)

        /// h=24 hrs
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.hoursToDeg(24.0)
        }

        /// h in interval
        assertEquals(180.0, AstroMath.hoursToDeg(12.0), 1e-9)
    }


    // raHmsToDeg
    @Test
    fun convertsHmsToDegreesTest() {

        /// h<0
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.raHmsToDeg(-1, 0, 0.0)
        }
        /// h=24hrs
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.raHmsToDeg(24, 0, 0.0)
        }

        /// m<0
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.raHmsToDeg(1, -1, 0.0)
        }

        /// m=60
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.raHmsToDeg(1, 60, 0.0)
        }

        /// s<0
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.raHmsToDeg(1, 0, -1.0)
        }

        /// s= 60
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.raHmsToDeg(1, 0, 60.0)
        }

        /// s = Nan
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.raHmsToDeg(1, 0, Double.NaN)
        }

        /// hms zero
        assertEquals(0.0, AstroMath.raHmsToDeg(0, 0, 0.0), 1e-9)

        /// in interval
        assertEquals(307.625, AstroMath.raHmsToDeg(20, 30, 30.0), 1e-9)
    }

    // julianDate
    @Test
    fun computesJulianDateTest() {
        /// J=2000
        assertEquals(2_451_545.0, AstroMath.julianDate(Instant.parse("2000-01-01T12:00:00Z")), 1e-9)

        /// unix epoch
        assertEquals(2_440_587.5, AstroMath.julianDate(Instant.parse("1970-01-01T00:00:00Z")), 1e-9)

    }

    // gmstDeg
    @Test
    fun computesGmstForJdNaN() {
        ///jd = Nan
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.gmstDeg(Double.NaN)
        }

        /// below range
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.gmstDeg(2_415_020.0)
        }

        /// at range start
        assertEquals(100.18338831141591, AstroMath.gmstDeg(2_415_020.5), 1e-6)

        /// in range
        assertEquals(280.46061837, AstroMath.gmstDeg(j2000), 1e-9)

        /// at range end
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.gmstDeg(2_488_069.5)
        }
    }

    // lstDeg
    @Test
    fun computesLstForLongitudeNaN() {
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.lstDeg(j2000, Double.NaN)
        }
    }
    @Test
    fun computesLstForLongitudeInRange() {
        /// below range
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.lstDeg(j2000, -181.0)
        }

        /// above range
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.lstDeg(j2000, 181.0)
        }

        /// in range
        assertEquals(20.46061837, AstroMath.lstDeg(j2000, 100.0), 1e-9)

    }
    @Test
    fun computesLstForLongitudeAtLimits() {
        ///W limit
        assertEquals(100.46061837, AstroMath.lstDeg(j2000, -180.0), 1e-9)

        ///E limit
        assertEquals(100.46061837, AstroMath.lstDeg(j2000, 180.0), 1e-9)
    }


    // raDecToAzAlt
    @Test
    fun computesAzAltForRaNaN() {
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.raDecToAzAlt(Double.NaN, 0.0, 45.0, 25.0, time)
        }
    }
    @Test
    fun computesAzAltForRaNegative() {
        /// Ra < 0
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.raDecToAzAlt(-1.0, 0.0, 45.0, 25.0, time)
        }

        ///Ra
    }
    @Test
    fun computesAzAltForRaThreeHundredSixty() {
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.raDecToAzAlt(360.0, 0.0, 45.0, 25.0, time)
        }
    }
    @Test
    fun computesAzAltForDecNaN() {
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.raDecToAzAlt(0.0, Double.NaN, 45.0, 25.0, time)
        }
    }
    @Test
    fun computesAzAltForDecOutside() {
        /// dec below range
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.raDecToAzAlt(0.0, -91.0, 45.0, 25.0, time)
        }

        /// dec above range
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.raDecToAzAlt(0.0, 91.0, 45.0, 25.0, time)
        }
    }

    @Test
    fun computesAzAltForLatNaN() {
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.raDecToAzAlt(0.0, 0.0, Double.NaN, 25.0, time)
        }
    }
    @Test
    fun computesAzAltForLatBelowRange() {

        /// Lat below range
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.raDecToAzAlt(0.0, 0.0, -91.0, 25.0, time)
        }

        /// Lat above range
        assertThrows(IllegalArgumentException::class.java) {
            AstroMath.raDecToAzAlt(0.0, 0.0, 91.0, 25.0, time)
        }
    }

    @Test
    fun computesAzAltForDecAtLimits() {
        // at Dec = 90 altitude equals latitude
        assertEquals(45.0, AstroMath.raDecToAzAlt(0.0, 90.0, 45.0, 25.0, time).altitudeDeg, 1e-9)

        /// S limit
        assertEquals(-45.0, AstroMath.raDecToAzAlt(0.0, -90.0, 45.0, 25.0, time).altitudeDeg, 1e-9)
    }

    @Test
    fun computesAzAltForLatAtNorthLimit() {
        // at lat = 90 altitude equals declination
        assertEquals(30.0, AstroMath.raDecToAzAlt(0.0, 30.0, 90.0, 25.0, time).altitudeDeg, 1e-9)

        /// S limit
        assertEquals(-30.0, AstroMath.raDecToAzAlt(0.0, 30.0, -90.0, 25.0, time).altitudeDeg, 1e-9)

    }

    @Test
    fun computesAzAltForObjectAtZenith() {
        // asin is imprecise near 90, hence 1e-4
        val ra = AstroMath.lstDeg(AstroMath.julianDate(time), 25.0)
        assertEquals(90.0, AstroMath.raDecToAzAlt(ra, 45.0, 45.0, 25.0, time).altitudeDeg, 1e-4)
    }
    @Test
    fun computesAzAltForEquatorObjectAtMeridian() {
        val ra = AstroMath.lstDeg(AstroMath.julianDate(time), 25.0)
        val result = AstroMath.raDecToAzAlt(ra, 0.0, 45.0, 25.0, time)
        assertEquals(45.0, result.altitudeDeg, 1e-9)
        assertEquals(180.0, result.azimuthDeg, 1e-9)
    }

//    @Test
//    fun computesAzAltForStarAgainstStellarium() {
//        val time = Instant.parse("2026-11-13T18:00:00Z") // UTC time
//        val result = AstroMath.raDecToAzAlt(
//            AstroMath.raHmsToDeg(18, 36, 56.0), 38.78, // RA/Dec (J2000) from Stellarium
//            45.0, 25.0, time                          // latitude, longitude
//        )
//        assertEquals(180.0, result.azimuthDeg, 0.5)   // Az from Stellarium
//        assertEquals(40.0, result.altitudeDeg, 0.5)   // Alt from Stellarium
//    }
}