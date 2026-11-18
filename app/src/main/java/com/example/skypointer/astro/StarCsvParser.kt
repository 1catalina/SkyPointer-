package com.example.skypointer.astro

import java.io.InputStream

object StarCsvParser {

    fun parse(input: InputStream): List<CelestialObject> {
        val result = mutableListOf<CelestialObject>()
        input.bufferedReader().useLines { lines ->
            lines.drop(1)                       // skips header
                .filter { it.isNotBlank() }
                .forEach { line -> parseLine(line)?.let(result::add) }
        }
        return result
    }

    private fun parseLine(line: String): CelestialObject? {
        val c = splitCsv(line)
        if (c.size < 8) return null

        val id = c[0].toIntOrNull() ?: return null
        val raHours = c[2].toDoubleOrNull() ?: return null
        val dec = c[3].toDoubleOrNull() ?: return null
        val mag = c[4].toDoubleOrNull() ?: return null

        // hours to degrees, then normalized in [0, 360)
        val raDeg = ((raHours * 15.0) % 360.0 + 360.0) % 360.0

        if (dec < -90.0 || dec > 90.0) return null   // invalid line, skips

        return CelestialObject(
            name = c[1].ifBlank { "HYG $id" },
            raDeg = raDeg,
            decDeg = dec,
            magnitude = mag,
            colorIndex = c[5].toDoubleOrNull(),
            spectralType = c[6].ifBlank { null },
            constellation = c[7].ifBlank { null },
            catalogId = id
        )
    }

    private fun splitCsv(line: String): List<String> {
        val out = mutableListOf<String>()
        val sb = StringBuilder()
        var inQuotes = false
        var i = 0
        while (i < line.length) {
            when (val ch = line[i]) {
                '"' if inQuotes && i + 1 < line.length && line[i + 1] == '"' -> { sb.append('"'); i++ }
                '"' -> inQuotes = !inQuotes
                ',' if !inQuotes -> { out.add(sb.toString()); sb.clear() }
                else -> sb.append(ch)
            }
            i++
        }
        out.add(sb.toString())
        return out
    }
}