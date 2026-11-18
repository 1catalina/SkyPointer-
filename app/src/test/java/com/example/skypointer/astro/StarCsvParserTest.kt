package com.example.skypointer.astro

import org.junit.Assert.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import org.junit.Test

class StarCsvParserTest {
    private fun load() =
        javaClass.classLoader!!.getResourceAsStream("stars_test.csv")!!

    @Test fun parsesAllRows() {
        assertEquals(5, StarCsvParser.parse(load()).size)
    }

    @Test fun findsSiriusByName() {
        val catalog = StarCatalog(StarCsvParser.parse(load()))
        val s = catalog.findByName("sirius")
        assertNotNull(s)
        assertEquals(-1.44, s!!.magnitude, 0.01)
    }

    @Test
    fun blankNameGetsFallback() {
        val star = StarCsvParser.parse(load()).first { it.catalogId == 4 }
        assertEquals("HYG 4", star.name)
    }

    @Test
    fun blankColorIndexIsNull() {
        val star = StarCsvParser.parse(load()).first { it.catalogId == 4 }
        assertNull(star.colorIndex)
    }

    @Test fun filtersByMagnitude() {
        val catalog = StarCatalog(StarCsvParser.parse(load()))
        assertEquals(4, catalog.brighterThan(5.0).size)
    }
}