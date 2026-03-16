package com.example.myapplication6

import java.io.Serializable

/**
 * Manages the overall game world state, including regions, factions, and world events
 */
object GameWorld : Serializable {

    private val regions = mutableListOf<Region>()
    private val factions = mutableListOf<Faction>()
    private val worldEvents = mutableListOf<WorldEvent>()
    private val unlockedRegions = mutableSetOf<Int>()
    val diplomaticRelations = mutableMapOf<Int, Int>()  // Nation ID to relation score (public for access)

    var currentThreatLevel: ThreatLevel = ThreatLevel.LOW
    var worldTension: Int = 30  // 0-100
    var globalEconomyState: EconomyState = EconomyState.STABLE
    var activeCrises: Int = 0

    fun initializeWorld() {
        regions.clear()
        factions.clear()
        worldEvents.clear()
        unlockedRegions.clear()
        diplomaticRelations.clear()

        // Initialize regions
        regions.add(
            Region(
                id = 0,
                name = "Capital Region",
                description = "The heart of the nation, home to the government and major institutions.",
                population = 5000000,
                development = 70,
                stability = 60,
                loyalty = 65,
                resources = listOf("Government", "Finance", "Technology"),
                isUnlocked = true
            )
        )

        regions.add(
            Region(
                id = 1,
                name = "Industrial Heartland",
                description = "Manufacturing hub with factories and heavy industry.",
                population = 8000000,
                development = 65,
                stability = 50,
                loyalty = 55,
                resources = listOf("Manufacturing", "Steel", "Automobiles"),
                isUnlocked = false
            )
        )

        regions.add(
            Region(
                id = 2,
                name = "Agricultural Plains",
                description = "Fertile farmland producing the nation's food supply.",
                population = 4000000,
                development = 40,
                stability = 70,
                loyalty = 75,
                resources = listOf("Wheat", "Corn", "Livestock"),
                isUnlocked = false
            )
        )

        regions.add(
            Region(
                id = 3,
                name = "Coastal Provinces",
                description = "Strategic ports and tourist destinations along the coast.",
                population = 6000000,
                development = 60,
                stability = 55,
                loyalty = 50,
                resources = listOf("Shipping", "Tourism", "Fishing"),
                isUnlocked = false
            )
        )

        regions.add(
            Region(
                id = 4,
                name = "Northern Territories",
                description = "Resource-rich but sparsely populated frontier.",
                population = 1000000,
                development = 25,
                stability = 40,
                loyalty = 45,
                resources = listOf("Oil", "Gas", "Minerals"),
                isUnlocked = false
            )
        )

        regions.add(
            Region(
                id = 5,
                name = "Border Disputed Zone",
                description = "Contested territory with neighboring nations.",
                population = 500000,
                development = 20,
                stability = 20,
                loyalty = 30,
                resources = listOf("Strategic Position"),
                isUnlocked = false
            )
        )

        // Unlock starting region
        unlockedRegions.add(0)

        // Initialize factions
        initializeFactions()

        // Initialize diplomatic relations with other nations
        initializeDiplomacy()
    }

    private fun initializeFactions() {
        factions.add(
            Faction(
                id = 0,
                name = "Ruling Party",
                description = "The current governing coalition.",
                power = 60,
                ideology = FactionIdeology.CENTRIST,
                leader = "Margaret Chen",
                goals = listOf("Maintain stability", "Economic growth"),
                isLegal = true
            )
        )

        factions.add(
            Faction(
                id = 1,
                name = "Progressive Alliance",
                description = "Left-wing coalition pushing for social reforms.",
                power = 35,
                ideology = FactionIdeology.PROGRESSIVE,
                leader = "Maya Patel",
                goals = listOf("Social justice", "Environmental protection"),
                isLegal = true
            )
        )

        factions.add(
            Faction(
                id = 2,
                name = "National Front",
                description = "Right-wing nationalists emphasizing tradition and security.",
                power = 30,
                ideology = FactionIdeology.NATIONALIST,
                leader = "General Marcus Stone (sympathizer)",
                goals = listOf("Strong defense", "National sovereignty"),
                isLegal = true
            )
        )

        factions.add(
            Faction(
                id = 3,
                name = "Business Coalition",
                description = "Corporate interests and industry lobbyists.",
                power = 45,
                ideology = FactionIdeology.CAPITALIST,
                leader = "Robert Blackwood",
                goals = listOf("Deregulation", "Tax cuts"),
                isLegal = true
            )
        )

        factions.add(
            Faction(
                id = 4,
                name = "Freedom Front",
                description = "Armed opposition group seeking regime change.",
                power = 20,
                ideology = FactionIdeology.REVOLUTIONARY,
                leader = "Commander Alex Volkov",
                goals = listOf("Overthrow government", "New constitution"),
                isLegal = false
            )
        )

        factions.add(
            Faction(
                id = 5,
                name = "Green Movement",
                description = "Environmental activists and conservationists.",
                power = 25,
                ideology = FactionIdeology.ENVIRONMENTALIST,
                leader = "Dr. Elena Vasquez",
                goals = listOf("Carbon neutrality", "Conservation"),
                isLegal = true
            )
        )

        factions.add(
            Faction(
                id = 6,
                name = "Labor Union Federation",
                description = "Coalition of trade unions representing workers.",
                power = 40,
                ideology = FactionIdeology.SOCIALIST,
                leader = "James Connolly",
                goals = listOf("Worker rights", "Fair wages"),
                isLegal = true
            )
        )

        factions.add(
            Faction(
                id = 7,
                name = "Marino Syndicate",
                description = "Organized crime network.",
                power = 15,
                ideology = FactionIdeology.CRIMINAL,
                leader = "Vincent 'The Ghost' Marino",
                goals = listOf("Expand operations", "Avoid law enforcement"),
                isLegal = false
            )
        )
    }

    private fun initializeDiplomacy() {
        // Relations with other nations (0-100, higher is better)
        diplomaticRelations[0] = 60  // Allied Nation A
        diplomaticRelations[1] = 45  // Neutral Nation B
        diplomaticRelations[2] = 30  // Rival Nation C
        diplomaticRelations[3] = 75  // Trade Partner D
        diplomaticRelations[4] = 20  // Hostile Nation E
        diplomaticRelations[5] = 50  // Emerging Nation F
    }

    fun getAllRegions(): List<Region> = regions.toList()

    fun getUnlockedRegions(): List<Region> = regions.filter { isRegionUnlocked(it.id) }

    fun isRegionUnlocked(id: Int): Boolean = unlockedRegions.contains(id)

    fun unlockRegion(id: Int) {
        unlockedRegions.add(id)
        val region = regions.find { it.id == id }
        region?.isUnlocked = true
    }

    fun getRegionById(id: Int): Region? = regions.find { it.id == id }

    fun getAllFactions(): List<Faction> = factions.toList()

    fun getFactionById(id: Int): Faction? = factions.find { it.id == id }

    fun getFactionsByIdeology(ideology: FactionIdeology): List<Faction> {
        return factions.filter { it.ideology == ideology }
    }

    fun getLegalFactions(): List<Faction> = factions.filter { it.isLegal }

    fun getIllegalFactions(): List<Faction> = factions.filter { !it.isLegal }

    fun getDiplomaticRelation(nationId: Int): Int = diplomaticRelations[nationId] ?: 50

    fun setDiplomaticRelation(nationId: Int, value: Int) {
        diplomaticRelations[nationId] = value.coerceIn(0, 100)
    }

    fun getNationName(nationId: Int): String {
        return when (nationId) {
            0 -> "Allied Republic"
            1 -> "Neutral Federation"
            2 -> "Rival Empire"
            3 -> "Trade Confederation"
            4 -> "Hostile State"
            5 -> "Emerging Democracy"
            else -> "Unknown Nation"
        }
    }

    fun updateWorldState(country: Country) {
        // Update threat level based on various factors
        currentThreatLevel = calculateThreatLevel(country)

        // Update world tension
        updateWorldTension(country)

        // Update global economy
        updateGlobalEconomy()

        // Process regional changes
        updateRegions(country)

        // Update faction power
        updateFactions(country)

        // Check for world events
        checkWorldEvents(country)
    }

    private fun calculateThreatLevel(country: Country): ThreatLevel {
        var threatScore = 0

        // Military threats
        if (country.military < 30) threatScore += 2
        if (country.stability < 30) threatScore += 2
        if (country.internationalRelations < 30) threatScore += 1

        // Economic threats
        if (country.treasury < 0) threatScore += 1
        if (country.gdp < 500000000) threatScore += 1

        // Social threats
        if (country.happiness < 30) threatScore += 1
        if (activeCrises > 2) threatScore += 2

        return when {
            threatScore >= 6 -> ThreatLevel.CRITICAL
            threatScore >= 4 -> ThreatLevel.HIGH
            threatScore >= 2 -> ThreatLevel.MEDIUM
            else -> ThreatLevel.LOW
        }
    }

    private fun updateWorldTension(country: Country) {
        // Base tension change
        var tensionChange = 0

        // International relations affect tension
        val avgRelations = diplomaticRelations.values.average()
        if (avgRelations < 40) tensionChange += 2
        if (avgRelations > 70) tensionChange -= 1

        // Military buildup
        if (country.military > 70) tensionChange += 1
        if (country.military < 30) tensionChange += 1  // Perceived weakness

        // Active conflicts
        val illegalFactions = getIllegalFactions()
        if (illegalFactions.any { it.power > 30 }) tensionChange += 2

        worldTension = (worldTension + tensionChange).coerceIn(0, 100)
    }

    private fun updateGlobalEconomy() {
        // Simple economy state machine
        globalEconomyState = when {
            worldTension > 70 -> EconomyState.RECESSION
            worldTension > 50 -> EconomyState.SLOWING
            worldTension < 30 -> EconomyState.BOOMING
            else -> EconomyState.STABLE
        }
    }

    private fun updateRegions(country: Country) {
        regions.forEach { region ->
            if (region.isUnlocked) {
                // Development changes
                if (country.infrastructure > 60) region.development += 1
                if (country.infrastructure < 40) region.development -= 1

                // Stability changes
                if (country.stability > 60) region.stability += 1
                if (country.stability < 40) region.stability -= 2

                // Loyalty changes based on happiness and regional investment
                if (country.happiness > 60) region.loyalty += 1
                if (country.happiness < 40) region.loyalty -= 1

                // Clamp values
                region.development = region.development.coerceIn(0, 100)
                region.stability = region.stability.coerceIn(0, 100)
                region.loyalty = region.loyalty.coerceIn(0, 100)
            }
        }
    }

    private fun updateFactions(country: Country) {
        factions.forEach { faction ->
            // Faction power changes based on government performance
            when (faction.ideology) {
                FactionIdeology.CENTRIST -> {
                    if (country.stability > 60 && country.happiness > 60) faction.power += 1
                    else faction.power -= 1
                }
                FactionIdeology.PROGRESSIVE -> {
                    if (country.environment < 50 || country.happiness < 50) faction.power += 2
                    else faction.power -= 1
                }
                FactionIdeology.NATIONALIST -> {
                    if (country.military < 50 || country.internationalRelations < 40) faction.power += 2
                    else faction.power -= 1
                }
                FactionIdeology.CAPITALIST -> {
                    if (country.gdp > 1500000000 && country.treasury > 100000000) faction.power += 1
                    else faction.power -= 1
                }
                FactionIdeology.REVOLUTIONARY -> {
                    if (country.stability < 40 && country.happiness < 40) faction.power += 3
                    else faction.power -= 2
                }
                FactionIdeology.ENVIRONMENTALIST -> {
                    if (country.environment < 50) faction.power += 2
                    else faction.power -= 1
                }
                FactionIdeology.SOCIALIST -> {
                    if (country.happiness < 50 || getFactionById(6)?.power ?: 0 < 30) faction.power += 1
                    else faction.power -= 1
                }
                FactionIdeology.CRIMINAL -> {
                    if (country.stability < 40) faction.power += 2
                    else faction.power -= 2
                }
            }

            // Clamp power
            faction.power = faction.power.coerceIn(0, 100)
        }
    }

    private fun checkWorldEvents(country: Country) {
        // Random world events that affect gameplay
        if (Math.random() < 0.1) {  // 10% chance per turn
            generateWorldEvent(country)
        }
    }

    private fun generateWorldEvent(country: Country) {
        val events = listOf(
            WorldEvent(
                id = 1,
                title = "Global Market Crash",
                description = "Stock markets worldwide are plummeting amid economic uncertainty.",
                effects = mapOf("gdp" to -50000000, "treasury" to -10000000),
                duration = 3,
                isActive = true
            ),
            WorldEvent(
                id = 2,
                title = "International Summit Success",
                description = "Your diplomacy efforts have led to a successful international agreement.",
                effects = mapOf("internationalRelations" to 15, "stability" to 5),
                duration = 1,
                isActive = true
            ),
            WorldEvent(
                id = 3,
                title = "Regional Conflict Erupts",
                description = "Neighboring countries have engaged in military conflict.",
                effects = mapOf("military" to 10, "internationalRelations" to -5),
                duration = 5,
                isActive = true
            ),
            WorldEvent(
                id = 4,
                title = "Technological Breakthrough",
                description = "A global scientific collaboration has achieved a major breakthrough.",
                effects = mapOf("education" to 10, "gdp" to 30000000),
                duration = 2,
                isActive = true
            ),
            WorldEvent(
                id = 5,
                title = "Pandemic Alert",
                description = "WHO issues warning about a new infectious disease.",
                effects = mapOf("healthcare" to 10, "gdp" to -30000000),
                duration = 4,
                isActive = true
            )
        )

        val event = events.random()
        worldEvents.add(event)
        activeCrises++
    }

    fun processActiveEvents(country: Country) {
        val eventsToRemove = mutableListOf<WorldEvent>()

        worldEvents.forEach { event ->
            if (event.isActive) {
                // Apply effects (only once, on first turn)
                if (event.duration == event.isActiveTurns) {
                    event.effects.forEach { (stat, value) ->
                        when (stat) {
                            "treasury" -> country.treasury += value.toDouble()
                            "gdp" -> country.gdp = (country.gdp + value.toDouble()).coerceAtLeast(0.0)
                            "stability" -> country.stability = (country.stability + value).coerceIn(0, 100)
                            "happiness" -> country.happiness = (country.happiness + value).coerceIn(0, 100)
                            "military" -> country.military = (country.military + value).coerceIn(0, 100)
                            "internationalRelations" -> country.internationalRelations = (country.internationalRelations + value).coerceIn(0, 100)
                            "education" -> country.education = (country.education + value).coerceIn(0, 100)
                            "healthcare" -> country.healthcare = (country.healthcare + value).coerceIn(0, 100)
                            "infrastructure" -> country.infrastructure = (country.infrastructure + value).coerceIn(0, 100)
                            "environment" -> country.environment = (country.environment + value).coerceIn(0, 100)
                        }
                    }
                }

                event.isActiveTurns--
                if (event.isActiveTurns <= 0) {
                    event.isActive = false
                    eventsToRemove.add(event)
                    activeCrises = (activeCrises - 1).coerceAtLeast(0)
                }
            }
        }

        worldEvents.removeAll(eventsToRemove)
    }

    fun getActiveEvents(): List<WorldEvent> = worldEvents.filter { it.isActive }

    fun getRegionBonus(region: Region): Map<String, Double> {
        return when (region.id) {
            0 -> mapOf("tax" to 1.2, "production" to 1.1)  // Capital bonus
            1 -> mapOf("production" to 1.3, "gdp" to 1.15)  // Industrial bonus
            2 -> mapOf("food" to 1.5, "population_growth" to 1.1)  // Agricultural bonus
            3 -> mapOf("trade" to 1.3, "tourism" to 1.2)  // Coastal bonus
            4 -> mapOf("resource" to 1.4, "energy" to 1.2)  // Resource bonus
            5 -> mapOf("strategic" to 1.2)  // Strategic position
            else -> mapOf()
        }
    }

    fun canUnlockRegion(region: Region, country: Country): Boolean {
        return when (region.id) {
            1 -> country.infrastructure >= 50 && country.gdp >= 1200000000
            2 -> country.stability >= 60 && country.population >= 2000000
            3 -> country.infrastructure >= 40 && country.treasury >= 50000000
            4 -> country.military >= 50 && country.stability >= 50
            5 -> country.military >= 60 && country.internationalRelations >= 40
            else -> false
        }
    }

    fun getThreatLevelDescription(): String {
        return when (currentThreatLevel) {
            ThreatLevel.LOW -> "No significant threats detected. Nation is secure."
            ThreatLevel.MEDIUM -> "Some concerns require attention. Stay vigilant."
            ThreatLevel.HIGH -> "Serious threats emerging. Immediate action recommended."
            ThreatLevel.CRITICAL -> "National security at risk. Emergency measures may be needed."
        }
    }

    fun getWorldStatusReport(country: Country): String {
        val report = StringBuilder()
        report.append("=== WORLD STATUS REPORT ===\n\n")

        report.append("Threat Level: ${currentThreatLevel}\n")
        report.append("${getThreatLevelDescription()}\n\n")

        report.append("World Tension: $worldTension%\n")
        report.append("Global Economy: ${globalEconomyState}\n\n")

        report.append("Active Crises: $activeCrises\n")
        getActiveEvents().forEach { event ->
            report.append("- ${event.title}\n")
        }
        if (getActiveEvents().isEmpty()) {
            report.append("- None\n")
        }
        report.append("\n")

        report.append("Regional Status:\n")
        getUnlockedRegions().forEach { region ->
            report.append("- ${region.name}: Stability ${region.stability}%, Loyalty ${region.loyalty}%\n")
        }
        report.append("\n")

        report.append("Faction Balance:\n")
        val sortedFactions = factions.sortedByDescending { it.power }.take(5)
        sortedFactions.forEach { faction ->
            report.append("- ${faction.name}: ${faction.power}% power\n")
        }

        return report.toString()
    }
}

enum class ThreatLevel {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

enum class EconomyState {
    BOOMING,
    STABLE,
    SLOWING,
    RECESSION
}

enum class FactionIdeology {
    CENTRIST,
    PROGRESSIVE,
    NATIONALIST,
    CAPITALIST,
    REVOLUTIONARY,
    ENVIRONMENTALIST,
    SOCIALIST,
    CRIMINAL
}

data class Region(
    val id: Int,
    val name: String,
    val description: String,
    var population: Int,
    var development: Int,
    var stability: Int,
    var loyalty: Int,
    val resources: List<String>,
    var isUnlocked: Boolean = false
) : Serializable

data class Faction(
    val id: Int,
    val name: String,
    val description: String,
    var power: Int,
    val ideology: FactionIdeology,
    val leader: String,
    val goals: List<String>,
    val isLegal: Boolean
) : Serializable

data class WorldEvent(
    val id: Int,
    val title: String,
    val description: String,
    val effects: Map<String, Int>,
    val duration: Int,
    var isActive: Boolean,
    var isActiveTurns: Int = duration
) : Serializable
