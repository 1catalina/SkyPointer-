package com.example.skypointer.astro

class StarCatalog(val stars: List<CelestialObject>) {

    fun findByName(name: String) =
        stars.firstOrNull { it.name.equals(name, ignoreCase = true) }

    fun brighterThan(mag: Double) = stars.filter { it.magnitude <= mag }

    companion object {
        suspend fun load(context: android.content.Context): StarCatalog =
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                context.assets.open("stars.csv").use { StarCatalog(StarCsvParser.parse(it)) }
            }
    }
}
