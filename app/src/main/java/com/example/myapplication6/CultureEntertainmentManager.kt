package com.example.myapplication6

import java.io.Serializable

/**
 * Country Sim v3.0 - Culture & Entertainment System
 * Handles arts, sports, media, and cultural development
 */

// Cultural institution types
enum class CulturalInstitutionType {
    MUSEUM,
    ART_GALLERY,
    THEATER,
    CONCERT_HALL,
    LIBRARY,
    CULTURAL_CENTER,
    HISTORICAL_SITE,
    CINEMA,
    STADIUM,
    AMUSEMENT_PARK
}

// Cultural institution
data class CulturalInstitution(
    val id: Int,
    val name: String,
    val type: CulturalInstitutionType,
    var quality: Int, // 0-100
    var visitors: Int,
    var revenue: Double,
    val cost: Double,
    val region: Int,
    var isFunded: Boolean = true,
    val yearEstablished: Int
) : Serializable

// Entertainment industry
data class EntertainmentIndustry(
    val id: Int,
    val name: String,
    val sector: EntertainmentSector,
    val marketShare: Double,
    val revenue: Double,
    val employees: Int,
    val growthRate: Double
) : Serializable

enum class EntertainmentSector {
    FILM,
    MUSIC,
    GAMING,
    SPORTS,
    STREAMING,
    PUBLISHING,
    RADIO,
    LIVE_EVENTS
}

// Cultural event
data class CulturalEvent(
    val id: Int,
    val name: String,
    val type: EventType,
    val cost: Double,
    val duration: Int,
    val expectedAttendance: Int,
    val prestigeGain: Int,
    var isOngoing: Boolean = false,
    var isComplete: Boolean = false,
    var isSuccess: Boolean = false,
    val turnStarted: Int = 0
) : Serializable

enum class EventType {
    ART_EXHIBITION,
    MUSIC_FESTIVAL,
    SPORTS_TOURNAMENT,
    FILM_FESTIVAL,
    CULTURAL_FAIR,
    INTERNATIONAL_COMPETITION,
    CONCERT_SERIES,
    THEATER_PRODUCTION
}

// Culture statistics
data class CultureStatistics(
    var totalInstitutions: Int = 0,
    var totalVisitors: Int = 0,
    var culturalBudget: Double = 0.0,
    var softPower: Double = 50.0,
    var culturalOutput: Double = 50.0,
    var tourismAppeal: Double = 50.0,
    var nationalPride: Double = 50.0,
    var diversityIndex: Double = 50.0
) : Serializable

// Object for culture and entertainment management
object CultureEntertainmentManager : Serializable {

    val institutions = mutableListOf<CulturalInstitution>()
    val entertainmentIndustries = mutableListOf<EntertainmentIndustry>()
    val activeEvents = mutableListOf<CulturalEvent>()
    val statistics = CultureStatistics()

    var cultureBudget: Double = 100000000.0
    var censorshipLevel: Int = 20
    var culturalDiversity: Double = 50.0
    var heritagePreservation: Double = 50.0

    fun initializeCulture() {
        institutions.clear()
        entertainmentIndustries.clear()
        activeEvents.clear()

        // Create starting institutions
        institutions.add(CulturalInstitution(1, "National Museum of History", CulturalInstitutionType.MUSEUM,
            80, 500000, 5000000.0, 8000000.0, 0, true, 1950))
        institutions.add(CulturalInstitution(2, "Capital Art Gallery", CulturalInstitutionType.ART_GALLERY,
            70, 200000, 2000000.0, 3000000.0, 0, true, 1975))
        institutions.add(CulturalInstitution(3, "Grand Theater", CulturalInstitutionType.THEATER,
            85, 300000, 8000000.0, 6000000.0, 0, true, 1920))
        institutions.add(CulturalInstitution(4, "National Stadium", CulturalInstitutionType.STADIUM,
            75, 1000000, 15000000.0, 10000000.0, 0, true, 1980))
        institutions.add(CulturalInstitution(5, "Central Library", CulturalInstitutionType.LIBRARY,
            90, 800000, 1000000.0, 4000000.0, 0, true, 1900))

        // Create entertainment industries
        entertainmentIndustries.add(EntertainmentIndustry(1, "Film Studios Inc.", EntertainmentSector.FILM,
            25.0, 50000000.0, 5000, 5.0))
        entertainmentIndustries.add(EntertainmentIndustry(2, "Music Records Ltd.", EntertainmentSector.MUSIC,
            20.0, 30000000.0, 2000, 8.0))
        entertainmentIndustries.add(EntertainmentIndustry(3, "Game Dev Studios", EntertainmentSector.GAMING,
            15.0, 40000000.0, 3000, 12.0))
        entertainmentIndustries.add(EntertainmentIndustry(4, "Sports League", EntertainmentSector.SPORTS,
            30.0, 80000000.0, 10000, 6.0))

        updateStatistics()
    }

    fun buildInstitution(name: String, type: CulturalInstitutionType, cost: Double, region: Int, country: Country): Boolean {
        if (country.treasury < cost) return false

        country.treasury -= cost

        val institution = CulturalInstitution(
            id = institutions.size + 1,
            name = name,
            type = type,
            quality = (60..90).random(),
            visitors = 0,
            revenue = 0.0,
            cost = cost * 0.1, // 10% annual maintenance
            region = region,
            isFunded = true,
            yearEstablished = country.year
        )

        institutions.add(institution)

        country.happiness = (country.happiness + 5).coerceIn(0, 100)
        country.education = (country.education + 2).coerceIn(0, 100)

        updateStatistics()
        return true
    }

    fun hostCulturalEvent(event: CulturalEvent, country: Country): String {
        if (country.treasury < event.cost) {
            return "Insufficient funds!"
        }

        country.treasury -= event.cost
        event.isOngoing = true
        event.turnStarted = country.turn
        activeEvents.add(event)

        return "Cultural event '${event.name}' started!"
    }

    fun processCulturalTurn(country: Country): List<String> {
        val results = mutableListOf<String>()
        val eventsToRemove = mutableListOf<CulturalEvent>()

        activeEvents.forEach { event ->
            if (event.isOngoing) {
                val turnsElapsed = country.turn - event.turnStarted

                if (turnsElapsed >= event.duration) {
                    event.isOngoing = false
                    event.isComplete = true

                    // Calculate success based on quality and funding
                    val successChance = 0.5 + (event.prestigeGain / 200.0)
                    event.isSuccess = Math.random() < successChance

                    if (event.isSuccess) {
                        country.happiness = (country.happiness + 5).coerceIn(0, 100)
                        country.internationalRelations = (country.internationalRelations + event.prestigeGain / 10).coerceIn(0, 100)
                        statistics.softPower = (statistics.softPower + event.prestigeGain / 20.0).coerceIn(0.0, 100.0)
                        results.add("✓ ${event.name}: SUCCESS - Boosted national prestige!")
                    } else {
                        country.happiness = (country.happiness - 2).coerceIn(0, 100)
                        results.add("✗ ${event.name}: FAILED - Did not meet expectations")
                    }

                    eventsToRemove.add(event)
                }
            }
        }

        // Process institutions
        institutions.forEach { institution ->
            if (institution.isFunded) {
                // Generate visitors based on quality
                institution.visitors = (institution.quality * 1000 * (1.0 + country.happiness / 200.0)).toInt()
                institution.revenue = institution.visitors * 10.0 // $10 per visitor

                // Apply effects
                country.happiness = (country.happiness + 0.1).coerceIn(0, 100)
                country.education = (country.education + 0.05).coerceIn(0, 100)
            }
        }

        // Process entertainment industries
        entertainmentIndustries.forEach { industry ->
            industry.revenue = industry.revenue * (1.0 + industry.growthRate / 100.0)
            industry.employees = (industry.employees * (1.0 + industry.growthRate / 200.0)).toInt()

            // Tax revenue
            country.treasury += industry.revenue * 0.2 // 20% tax
        }

        activeEvents.removeAll(eventsToRemove)
        updateStatistics()

        return results
    }

    fun fundInstitution(institutionId: Int, amount: Double, country: Country): Boolean {
        val institution = institutions.find { it.id == institutionId }
        if (institution == null || country.treasury < amount) return false

        country.treasury -= amount
        institution.quality = (institution.quality + (amount / 1000000.0).toInt()).coerceIn(0, 100)
        institution.isFunded = true

        return true
    }

    fun setCulturalPolicy(policy: CulturalPolicy, country: Country): String {
        return when (policy) {
            CulturalPolicy.INCREASE_FUNDING -> {
                cultureBudget *= 1.5
                country.treasury -= cultureBudget * 0.5
                country.happiness = (country.happiness + 5).coerceIn(0, 100)
                "Cultural funding increased!"
            }
            CulturalPolicy.DECREASE_FUNDING -> {
                cultureBudget *= 0.7
                country.treasury += cultureBudget * 0.3
                country.happiness = (country.happiness - 3).coerceIn(0, 100)
                "Cultural funding decreased!"
            }
            CulturalPolicy.PROMOTE_DIVERSITY -> {
                culturalDiversity = (culturalDiversity + 15.0).coerceIn(0.0, 100.0)
                country.happiness = (country.happiness + 5).coerceIn(0, 100)
                country.internationalRelations = (country.internationalRelations + 5).coerceIn(0, 100)
                "Cultural diversity promoted!"
            }
            CulturalPolicy.PRESERVE_HERITAGE -> {
                heritagePreservation = (heritagePreservation + 20.0).coerceIn(0.0, 100.0)
                country.happiness = (country.happiness + 3).coerceIn(0, 100)
                "Heritage preservation strengthened!"
            }
            CulturalPolicy.INCREASE_CENSORSHIP -> {
                censorshipLevel = (censorshipLevel + 20).coerceIn(0, 100)
                country.stability = (country.stability + 5).coerceIn(0, 100)
                country.happiness = (country.happiness - 8).coerceIn(0, 100)
                country.internationalRelations = (country.internationalRelations - 10).coerceIn(0, 100)
                "Cultural censorship increased!"
            }
            CulturalPolicy.DECREASE_CENSORSHIP -> {
                censorshipLevel = (censorshipLevel - 20).coerceIn(0, 100)
                country.stability = (country.stability - 3).coerceIn(0, 100)
                country.happiness = (country.happiness + 8).coerceIn(0, 100)
                country.internationalRelations = (country.internationalRelations + 5).coerceIn(0, 100)
                "Cultural censorship decreased!"
            }
        }
    }

    enum class CulturalPolicy {
        INCREASE_FUNDING,
        DECREASE_FUNDING,
        PROMOTE_DIVERSITY,
        PRESERVE_HERITAGE,
        INCREASE_CENSORSHIP,
        DECREASE_CENSORSHIP
    }

    private fun updateStatistics() {
        statistics.totalInstitutions = institutions.size
        statistics.totalVisitors = institutions.sumOf { it.visitors }
        statistics.culturalBudget = cultureBudget
        statistics.tourismAppeal = (institutions.map { it.quality }.average() + culturalDiversity) / 2.0
        statistics.nationalPride = (heritagePreservation + institutions.map { it.quality }.average()) / 2.0
        statistics.diversityIndex = culturalDiversity
    }

    fun getCultureReport(): String {
        val report = StringBuilder()
        report.append("=== CULTURE & ENTERTAINMENT REPORT ===\n\n")

        report.append("Cultural Budget: $${String.format("%,d", (cultureBudget / 1000000).toLong())}M\n")
        report.append("Soft Power: ${statistics.softPower.toInt()}/100\n")
        report.append("Cultural Diversity: ${culturalDiversity.toInt()}%\n")
        report.append("Heritage Preservation: ${heritagePreservation.toInt()}%\n")
        report.append("Censorship Level: $censorshipLevel%\n\n")

        report.append("Cultural Institutions (${institutions.size}):\n")
        institutions.forEach { inst ->
            val status = if (inst.isFunded) "✓ Funded" else "⚠ Underfunded"
            report.append("- ${inst.name} ($status)\n")
            report.append("  Type: ${inst.type} | Quality: ${inst.quality}%\n")
            report.append("  Visitors: ${inst.visitors / 1000}K | Revenue: $${String.format("%,d", (inst.revenue / 1000).toLong())}K\n\n")
        }

        report.append("Entertainment Industries (${entertainmentIndustries.size}):\n")
        entertainmentIndustries.forEach { ind ->
            report.append("- ${ind.name} (${ind.sector})\n")
            report.append("  Market Share: ${ind.marketShare}% | Revenue: $${String.format("%,d", (ind.revenue / 1000000).toLong())}M\n")
            report.append("  Employees: ${ind.employees} | Growth: ${ind.growthRate}%\n\n")
        }

        if (activeEvents.isNotEmpty()) {
            report.append("Active Events:\n")
            activeEvents.forEach { event ->
                report.append("- ${event.name}: ${event.type}\n")
            }
        }

        report.append("\n=== STATISTICS ===\n")
        report.append("Total Visitors: ${statistics.totalVisitors / 1000}K\n")
        report.append("Tourism Appeal: ${statistics.tourismAppeal.toInt()}%\n")
        report.append("National Pride: ${statistics.nationalPride.toInt()}%\n")

        return report.toString()
    }

    fun getAvailableEvents(): List<CulturalEvent> {
        return listOf(
            CulturalEvent(1, "International Art Exhibition", EventType.ART_EXHIBITION,
                10000000.0, 4, 100000, 15),
            CulturalEvent(2, "Summer Music Festival", EventType.MUSIC_FESTIVAL,
                15000000.0, 3, 200000, 20),
            CulturalEvent(3, "National Sports Championship", EventType.SPORTS_TOURNAMENT,
                25000000.0, 5, 500000, 25),
            CulturalEvent(4, "International Film Festival", EventType.FILM_FESTIVAL,
                12000000.0, 4, 80000, 18),
            CulturalEvent(5, "Cultural Heritage Fair", EventType.CULTURAL_FAIR,
                8000000.0, 3, 150000, 12),
            CulturalEvent(6, "World Cup Bid", EventType.INTERNATIONAL_COMPETITION,
                50000000.0, 10, 1000000, 40),
            CulturalEvent(7, "Symphony Concert Series", EventType.CONCERT_SERIES,
                5000000.0, 2, 50000, 10),
            CulturalEvent(8, "National Theater Production", EventType.THEATER_PRODUCTION,
                7000000.0, 3, 40000, 14)
        )
    }
}
