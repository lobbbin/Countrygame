package com.example.myapplication6

import java.io.Serializable

/**
 * Country Sim v3.0 - Crisis & Emergency Management System
 * Handles natural disasters, emergencies, and crisis response
 */

// Crisis types
enum class CrisisType {
    NATURAL_DISASTER,
    ECONOMIC_CRISIS,
    HEALTH_EMERGENCY,
    POLITICAL_CRISIS,
    MILITARY_CONFLICT,
    TERRORIST_ATTACK,
    CYBER_ATTACK,
    ENVIRONMENTAL_DISASTER,
    SOCIAL_UNREST,
    INFRASTRUCTURE_FAILURE,
    FOOD_SHORTAGE,
    ENERGY_CRISIS,
    REFUGEE_CRISIS,
    PANDEMIC,
    CIVIL_WAR
}

// Crisis severity
enum class CrisisSeverity {
    MINOR,      // Minimal impact, easy to handle
    MODERATE,   // Noticeable impact, manageable
    MAJOR,      // Significant impact, challenging
    SEVERE,     // Major impact, very challenging
    CATASTROPHIC // Existential threat, extremely difficult
}

// Crisis data
data class Crisis(
    val id: Int,
    val name: String,
    val description: String,
    val type: CrisisType,
    var severity: CrisisSeverity,
    val region: Int,
    var turnsActive: Int,
    val maxTurns: Int,
    val effects: Map<String, Double>,
    val responseOptions: List<CrisisResponse>,
    var isResolved: Boolean = false,
    var responseChosen: Int = -1,
    var casualties: Int = 0,
    var economicDamage: Double = 0.0,
    var containmentLevel: Int = 0
) : Serializable

// Crisis response option
data class CrisisResponse(
    val id: Int,
    val title: String,
    val description: String,
    val cost: Double,
    val effectiveness: Double,
    val risk: Double,
    val requirements: Map<String, Int>,
    val consequences: Map<String, Double>
) : Serializable

// Emergency services status
data class EmergencyServices(
    var policeFunding: Double = 50000000.0,
    var fireFunding: Double = 30000000.0,
    var emsFunding: Double = 40000000.0,
    var femaFunding: Double = 20000000.0,
    var policePersonnel: Int = 50000,
    var firePersonnel: Int = 30000,
    var emsPersonnel: Int = 40000,
    var policeEquipment: Int = 50,
    var fireEquipment: Int = 50,
    var emsEquipment: Int = 50,
    var responseTime: Double = 5.0,
    var successRate: Double = 85.0
) : Serializable

// Disaster preparedness
data class DisasterPreparedness(
    var earlyWarningSystem: Int = 50,
    var evacuationPlans: Int = 50,
    var emergencyShelters: Int = 50,
    var stockpiles: Int = 50,
    var trainingLevel: Int = 50,
    var publicAwareness: Int = 50,
    var infrastructureResilience: Int = 50,
    var communicationSystems: Int = 50
) : Serializable

// Object for crisis management
object CrisisManager : Serializable {
    
    val activeCrises = mutableListOf<Crisis>()
    val resolvedCrises = mutableListOf<Crisis>()
    val emergencyServices = EmergencyServices()
    val preparedness = DisasterPreparedness()
    
    var crisisRisk: Double = 10.0
    var nationalEmergencyLevel: Int = 0 // 0-5
    var totalCasualties: Int = 0
    var totalEconomicDamage: Double = 0.0
    var crisesPrevented: Int = 0
    var crisesMismanaged: Int = 0
    
    fun initializeCrisisSystem() {
        activeCrises.clear()
        resolvedCrises.clear()
        crisisRisk = 10.0
        nationalEmergencyLevel = 0
    }
    
    fun checkForNewCrisis(country: Country): Crisis? {
        // Base crisis chance
        var crisisChance = crisisRisk
        
        // Modify based on country stats
        if (country.stability < 40) crisisChance += 15.0
        if (country.happiness < 40) crisisChance += 10.0
        if (country.military < 30) crisisChance += 10.0
        if (country.infrastructure < 40) crisisChance += 10.0
        if (country.healthcare < 40) crisisChance += 10.0
        if (country.environment < 30) crisisChance += 15.0
        if (country.treasury < 0) crisisChance += 10.0
        
        // Modify based on preparedness
        crisisChance -= (preparedness.getAverage() * 0.3)
        
        // Modify based on world tension
        crisisChance += (GameWorld.worldTension * 0.1)
        
        // Check if crisis occurs
        if (Math.random() * 100 < crisisChance) {
            val crisis = generateCrisis(country)
            activeCrises.add(crisis)
            updateEmergencyLevel()
            return crisis
        }
        
        return null
    }
    
    private fun generateCrisis(country: Country): Crisis {
        val crisisTemplates = getCrisisTemplates()
        val template = crisisTemplates.random()

        // Determine severity based on country stats
        val severity = determineSeverity(country)

        // Calculate casualties and damage
        val baseCasualties = when (severity) {
            CrisisSeverity.MINOR -> (10..100).random()
            CrisisSeverity.MODERATE -> (100..1000).random()
            CrisisSeverity.MAJOR -> (1000..10000).random()
            CrisisSeverity.SEVERE -> (10000..100000).random()
            CrisisSeverity.CATASTROPHIC -> (100000..1000000).random()
        }

        val baseDamage = when (severity) {
            CrisisSeverity.MINOR -> 1000000.0 + Math.random() * 9000000.0
            CrisisSeverity.MODERATE -> 10000000.0 + Math.random() * 90000000.0
            CrisisSeverity.MAJOR -> 100000000.0 + Math.random() * 400000000.0
            CrisisSeverity.SEVERE -> 500000000.0 + Math.random() * 1500000000.0
            CrisisSeverity.CATASTROPHIC -> 2000000000.0 + Math.random() * 8000000000.0
        }

        // Determine affected region
        val unlockedRegions = GameWorld.getUnlockedRegions()
        val region = if (unlockedRegions.isNotEmpty()) {
            unlockedRegions.random().id
        } else {
            0
        }
        
        return Crisis(
            id = activeCrises.size + resolvedCrises.size + 1,
            name = template.name,
            description = template.description,
            type = template.type,
            severity = severity,
            region = region,
            turnsActive = 0,
            maxTurns = when (severity) {
                CrisisSeverity.MINOR -> (2..4).random()
                CrisisSeverity.MODERATE -> (4..8).random()
                CrisisSeverity.MAJOR -> (8..12).random()
                CrisisSeverity.SEVERE -> (12..20).random()
                CrisisSeverity.CATASTROPHIC -> (20..50).random()
            },
            effects = calculateEffects(template, severity, country),
            responseOptions = template.responseOptions,
            casualties = baseCasualties,
            economicDamage = baseDamage
        )
    }
    
    private fun determineSeverity(country: Country): CrisisSeverity {
        var severityScore = 0
        
        if (country.stability < 30) severityScore += 2
        if (country.happiness < 30) severityScore += 1
        if (country.infrastructure < 30) severityScore += 2
        if (country.healthcare < 30) severityScore += 1
        if (preparedness.getAverage() < 30) severityScore += 2
        
        return when {
            severityScore >= 6 -> CrisisSeverity.CATASTROPHIC
            severityScore >= 5 -> CrisisSeverity.SEVERE
            severityScore >= 3 -> CrisisSeverity.MAJOR
            severityScore >= 1 -> CrisisSeverity.MODERATE
            else -> CrisisSeverity.MINOR
        }
    }
    
    private fun calculateEffects(template: CrisisTemplate, severity: CrisisSeverity, country: Country): Map<String, Double> {
        val severityMultiplier = when (severity) {
            CrisisSeverity.MINOR -> 0.5
            CrisisSeverity.MODERATE -> 1.0
            CrisisSeverity.MAJOR -> 2.0
            CrisisSeverity.SEVERE -> 4.0
            CrisisSeverity.CATASTROPHIC -> 8.0
        }
        
        return template.baseEffects.mapValues { (_, value) ->
            value * severityMultiplier
        }
    }
    
    data class CrisisTemplate(
        val name: String,
        val description: String,
        val type: CrisisType,
        val baseEffects: Map<String, Double>,
        val responseOptions: List<CrisisResponse>
    )
    
    private fun getCrisisTemplates(): List<CrisisTemplate> {
        return listOf(
            // Natural Disasters
            CrisisTemplate(
                name = "Earthquake",
                description = "A major earthquake has struck a populated area!",
                type = CrisisType.NATURAL_DISASTER,
                baseEffects = mapOf("stability" to -15.0, "happiness" to -20.0, "infrastructure" to -25.0, "gdp" to -50000000.0),
                responseOptions = listOf(
                    CrisisResponse(1, "Emergency Response", "Deploy all emergency services immediately", 
                        20000000.0, 0.8, 0.1, mapOf("emergencyServices" to 60),
                        mapOf("happiness" to 10.0, "stability" to 5.0)),
                    CrisisResponse(2, "International Aid", "Request international disaster relief",
                        5000000.0, 0.6, 0.2, mapOf("internationalRelations" to 50),
                        mapOf("internationalRelations" to -5.0, "happiness" to 5.0)),
                    CrisisResponse(3, "Military Deployment", "Use military for disaster relief",
                        15000000.0, 0.9, 0.3, mapOf("military" to 50),
                        mapOf("stability" to 10.0, "military" to -5.0))
                )
            ),
            CrisisTemplate(
                name = "Hurricane",
                description = "A massive hurricane is approaching the coast!",
                type = CrisisType.NATURAL_DISASTER,
                baseEffects = mapOf("infrastructure" to -20.0, "environment" to -15.0, "gdp" to -40000000.0, "happiness" to -15.0),
                responseOptions = listOf(
                    CrisisResponse(1, "Mass Evacuation", "Evacuate all coastal areas",
                        30000000.0, 0.85, 0.15, mapOf<String, Int>("infrastructure" to 50),
                        mapOf("happiness" to 5.0, "stability" to 5.0)),
                    CrisisResponse(2, "Emergency Shelters", "Open emergency shelters",
                        10000000.0, 0.6, 0.1, mapOf<String, Int>("preparedness" to 40),
                        mapOf("happiness" to 10.0)),
                    CrisisResponse(3, "Do Nothing", "Hope for the best",
                        0.0, 0.2, 0.8, mapOf<String, Int>(),
                        mapOf("happiness" to -20.0, "stability" to -15.0))
                )
            ),
            CrisisTemplate(
                name = "Wildfire",
                description = "Uncontrolled wildfires are spreading!",
                type = CrisisType.ENVIRONMENTAL_DISASTER,
                baseEffects = mapOf("environment" to -30.0, "infrastructure" to -10.0, "happiness" to -10.0),
                responseOptions = listOf(
                    CrisisResponse(1, "Aerial Firefighting", "Deploy firefighting aircraft",
                        25000000.0, 0.8, 0.1, mapOf<String, Int>(),
                        mapOf("environment" to 15.0, "infrastructure" to 5.0)),
                    CrisisResponse(2, "Ground Crews", "Deploy firefighter teams",
                        10000000.0, 0.6, 0.2, mapOf<String, Int>("emergencyServices" to 50),
                        mapOf("environment" to 10.0)),
                    CrisisResponse(3, "Controlled Burn", "Create firebreaks with controlled burns",
                        5000000.0, 0.7, 0.3, mapOf<String, Int>(),
                        mapOf("environment" to -5.0, "infrastructure" to 5.0))
                )
            ),

            // Economic Crises
            CrisisTemplate(
                name = "Stock Market Crash",
                description = "The stock market has crashed! Panic selling ensues.",
                type = CrisisType.ECONOMIC_CRISIS,
                baseEffects = mapOf("gdp" to -100000000.0, "treasury" to -30000000.0, "happiness" to -25.0, "stability" to -15.0),
                responseOptions = listOf(
                    CrisisResponse(1, "Market Intervention", "Buy stocks to stabilize market",
                        100000000.0, 0.75, 0.2, mapOf<String, Int>("treasury" to 100000000),
                        mapOf("gdp" to 50000000.0, "happiness" to 10.0)),
                    CrisisResponse(2, "Bank Bailout", "Bail out failing banks",
                        75000000.0, 0.65, 0.3, mapOf<String, Int>("treasury" to 50000000),
                        mapOf("stability" to 10.0, "happiness" to -10.0)),
                    CrisisResponse(3, "Let Market Correct", "Allow natural market correction",
                        0.0, 0.4, 0.5, mapOf<String, Int>(),
                        mapOf("gdp" to -50000000.0, "happiness" to -15.0))
                )
            ),
            CrisisTemplate(
                name = "Banking Crisis",
                description = "Major banks are failing! Bank runs are occurring.",
                type = CrisisType.ECONOMIC_CRISIS,
                baseEffects = mapOf("treasury" to -50000000.0, "gdp" to -80000000.0, "stability" to -25.0),
                responseOptions = listOf(
                    CrisisResponse(1, "Bank Holiday", "Temporarily close all banks",
                        10000000.0, 0.7, 0.3, mapOf<String, Int>("stability" to 40),
                        mapOf("stability" to 15.0, "happiness" to -10.0)),
                    CrisisResponse(2, "Deposit Guarantee", "Guarantee all deposits",
                        50000000.0, 0.8, 0.15, mapOf<String, Int>("treasury" to 50000000),
                        mapOf("stability" to 20.0, "happiness" to 5.0)),
                    CrisisResponse(3, "Nationalize Banks", "Take control of failing banks",
                        100000000.0, 0.85, 0.4, mapOf<String, Int>(),
                        mapOf("stability" to 10.0, "gdp" to 30000000.0, "internationalRelations" to -10.0))
                )
            ),

            // Health Emergencies
            CrisisTemplate(
                name = "Pandemic Outbreak",
                description = "A new infectious disease is spreading rapidly!",
                type = CrisisType.PANDEMIC,
                baseEffects = mapOf("healthcare" to -30.0, "gdp" to -150000000.0, "happiness" to -30.0, "population" to -50000.0),
                responseOptions = listOf(
                    CrisisResponse(1, "Lockdown", "Implement strict lockdown measures",
                        50000000.0, 0.8, 0.2, mapOf<String, Int>("healthcare" to 50),
                        mapOf("healthcare" to 20.0, "gdp" to -50000000.0, "happiness" to -10.0)),
                    CrisisResponse(2, "Targeted Measures", "Targeted restrictions in hotspots",
                        20000000.0, 0.5, 0.3, mapOf<String, Int>(),
                        mapOf("healthcare" to 10.0, "gdp" to -30000000.0)),
                    CrisisResponse(3, "Herd Immunity", "Allow natural immunity to develop",
                        5000000.0, 0.3, 0.7, mapOf<String, Int>(),
                        mapOf("population" to -100000.0, "healthcare" to -50.0, "happiness" to -40.0))
                )
            ),
            CrisisTemplate(
                name = "Hospital Overload",
                description = "Hospitals are overwhelmed with patients!",
                type = CrisisType.HEALTH_EMERGENCY,
                baseEffects = mapOf("healthcare" to -40.0, "happiness" to -25.0, "stability" to -15.0),
                responseOptions = listOf(
                    CrisisResponse(1, "Emergency Hospitals", "Build field hospitals",
                        40000000.0, 0.85, 0.1, mapOf<String, Int>("treasury" to 30000000),
                        mapOf("healthcare" to 30.0, "happiness" to 10.0)),
                    CrisisResponse(2, "Medical Staff Surge", "Call in all medical reserves",
                        15000000.0, 0.6, 0.15, mapOf<String, Int>("healthcare" to 40),
                        mapOf("healthcare" to 20.0)),
                    CrisisResponse(3, "Ration Care", "Prioritize critical cases only",
                        5000000.0, 0.4, 0.5, mapOf<String, Int>(),
                        mapOf("happiness" to -30.0, "stability" to -20.0, "healthcare" to 10.0))
                )
            ),

            // Political Crises
            CrisisTemplate(
                name = "Government Collapse",
                description = "The ruling coalition has collapsed!",
                type = CrisisType.POLITICAL_CRISIS,
                baseEffects = mapOf("stability" to -40.0, "happiness" to -20.0, "internationalRelations" to -15.0),
                responseOptions = listOf(
                    CrisisResponse(1, "Snap Elections", "Call immediate elections",
                        20000000.0, 0.7, 0.2, mapOf<String, Int>("stability" to 30),
                        mapOf("stability" to 20.0, "happiness" to 10.0)),
                    CrisisResponse(2, "Coalition Building", "Form new coalition government",
                        10000000.0, 0.6, 0.3, mapOf<String, Int>(),
                        mapOf("stability" to 15.0)),
                    CrisisResponse(3, "Emergency Powers", "Rule by emergency decree",
                        5000000.0, 0.5, 0.6, mapOf<String, Int>(),
                        mapOf("stability" to 25.0, "happiness" to -25.0, "internationalRelations" to -20.0))
                )
            ),
            CrisisTemplate(
                name = "Corruption Scandal",
                description = "High-level corruption has been exposed!",
                type = CrisisType.POLITICAL_CRISIS,
                baseEffects = mapOf("stability" to -20.0, "happiness" to -30.0, "internationalRelations" to -10.0),
                responseOptions = listOf(
                    CrisisResponse(1, "Full Investigation", "Launch independent investigation",
                        10000000.0, 0.8, 0.15, mapOf<String, Int>("stability" to 40),
                        mapOf("stability" to 15.0, "happiness" to 15.0)),
                    CrisisResponse(2, "Resignations", "Force resignations of officials",
                        5000000.0, 0.6, 0.2, mapOf<String, Int>(),
                        mapOf("happiness" to 20.0, "stability" to -5.0)),
                    CrisisResponse(3, "Cover Up", "Attempt to suppress the scandal",
                        15000000.0, 0.3, 0.7, mapOf<String, Int>(),
                        mapOf("happiness" to -40.0, "stability" to -30.0))
                )
            ),

            // Military Conflicts
            CrisisTemplate(
                name = "Border Skirmish",
                description = "Armed conflict has erupted at the border!",
                type = CrisisType.MILITARY_CONFLICT,
                baseEffects = mapOf("military" to -15.0, "stability" to -20.0, "internationalRelations" to -25.0, "treasury" to -30000000.0),
                responseOptions = listOf(
                    CrisisResponse(1, "Military Escalation", "Respond with full military force",
                        50000000.0, 0.7, 0.4, mapOf<String, Int>("military" to 60),
                        mapOf("military" to -10.0, "stability" to 10.0, "internationalRelations" to -20.0)),
                    CrisisResponse(2, "Defensive Posture", "Hold positions defensively",
                        20000000.0, 0.6, 0.2, mapOf<String, Int>("military" to 50),
                        mapOf("stability" to 5.0, "internationalRelations" to -10.0)),
                    CrisisResponse(3, "Ceasefire", "Immediately seek ceasefire",
                        5000000.0, 0.5, 0.3, mapOf<String, Int>(),
                        mapOf("internationalRelations" to 10.0, "stability" to -15.0, "military" to -10.0))
                )
            ),

            // Terrorist Attacks
            CrisisTemplate(
                name = "Terrorist Attack",
                description = "A major terrorist attack has occurred!",
                type = CrisisType.TERRORIST_ATTACK,
                baseEffects = mapOf("stability" to -35.0, "happiness" to -40.0, "population" to -5000.0),
                responseOptions = listOf(
                    CrisisResponse(1, "Security Crackdown", "Implement strict security measures",
                        40000000.0, 0.75, 0.25, mapOf<String, Int>("military" to 50, "stability" to 40),
                        mapOf("stability" to 20.0, "happiness" to -10.0, "internationalRelations" to -5.0)),
                    CrisisResponse(2, "Intelligence Operation", "Launch covert intelligence operation",
                        20000000.0, 0.6, 0.2, mapOf<String, Int>("intelligence" to 50),
                        mapOf("stability" to 15.0)),
                    CrisisResponse(3, "Negotiation", "Attempt to negotiate with perpetrators",
                        10000000.0, 0.4, 0.5, mapOf<String, Int>(),
                        mapOf("stability" to -20.0, "happiness" to -30.0, "internationalRelations" to -15.0))
                )
            ),

            // Cyber Attacks
            CrisisTemplate(
                name = "Cyber Warfare Attack",
                description = "Critical infrastructure has been hacked!",
                type = CrisisType.CYBER_ATTACK,
                baseEffects = mapOf("infrastructure" to -25.0, "gdp" to -50000000.0, "stability" to -20.0),
                responseOptions = listOf(
                    CrisisResponse(1, "Cyber Counterattack", "Launch cyber counteroffensive",
                        30000000.0, 0.7, 0.3, mapOf<String, Int>("technology" to 60),
                        mapOf("infrastructure" to 15.0, "stability" to 10.0)),
                    CrisisResponse(2, "System Isolation", "Disconnect critical systems",
                        10000000.0, 0.6, 0.15, mapOf<String, Int>(),
                        mapOf("infrastructure" to 10.0, "gdp" to -20000000.0)),
                    CrisisResponse(3, "Public-Private Response", "Coordinate with tech companies",
                        15000000.0, 0.65, 0.2, mapOf<String, Int>("technology" to 50),
                        mapOf("infrastructure" to 12.0, "gdp" to 10000000.0))
                )
            ),

            // Environmental Disasters
            CrisisTemplate(
                name = "Nuclear Accident",
                description = "A nuclear facility has had a major accident!",
                type = CrisisType.ENVIRONMENTAL_DISASTER,
                baseEffects = mapOf("environment" to -50.0, "healthcare" to -40.0, "happiness" to -50.0, "population" to -20000.0),
                responseOptions = listOf(
                    CrisisResponse(1, "Full Evacuation", "Evacuate entire region",
                        100000000.0, 0.8, 0.1, mapOf<String, Int>("treasury" to 100000000),
                        mapOf("healthcare" to 30.0, "happiness" to 20.0)),
                    CrisisResponse(2, "Containment", "Attempt to contain the radiation",
                        50000000.0, 0.5, 0.4, mapOf<String, Int>("technology" to 70),
                        mapOf("environment" to 20.0, "healthcare" to 15.0)),
                    CrisisResponse(3, "International Help", "Request international nuclear response",
                        20000000.0, 0.7, 0.2, mapOf<String, Int>(),
                        mapOf("environment" to 25.0, "internationalRelations" to -10.0))
                )
            ),

            // Social Unrest
            CrisisTemplate(
                name = "Mass Protests",
                description = "Massive protests are sweeping the nation!",
                type = CrisisType.SOCIAL_UNREST,
                baseEffects = mapOf("stability" to -30.0, "happiness" to -25.0, "gdp" to -30000000.0),
                responseOptions = listOf(
                    CrisisResponse(1, "Concede to Demands", "Accept protester demands",
                        30000000.0, 0.8, 0.1, mapOf<String, Int>("happiness" to 40),
                        mapOf("happiness" to 25.0, "stability" to 15.0)),
                    CrisisResponse(2, "Police Response", "Deploy police to disperse crowds",
                        15000000.0, 0.5, 0.4, mapOf<String, Int>("police" to 50),
                        mapOf("stability" to 10.0, "happiness" to -20.0, "internationalRelations" to -10.0)),
                    CrisisResponse(3, "Dialogue", "Open dialogue with protest leaders",
                        5000000.0, 0.65, 0.15, mapOf<String, Int>(),
                        mapOf("stability" to 15.0, "happiness" to 10.0))
                )
            ),

            // Infrastructure Failures
            CrisisTemplate(
                name = "Power Grid Failure",
                description = "The national power grid has failed!",
                type = CrisisType.INFRASTRUCTURE_FAILURE,
                baseEffects = mapOf("infrastructure" to -35.0, "gdp" to -80000000.0, "happiness" to -30.0),
                responseOptions = listOf(
                    CrisisResponse(1, "Emergency Power", "Deploy emergency generators",
                        40000000.0, 0.75, 0.15, mapOf<String, Int>("infrastructure" to 50),
                        mapOf("infrastructure" to 20.0, "gdp" to 30000000.0)),
                    CrisisResponse(2, "Regional Grids", "Activate regional backup grids",
                        20000000.0, 0.6, 0.2, mapOf<String, Int>("infrastructure" to 40),
                        mapOf("infrastructure" to 15.0)),
                    CrisisResponse(3, "Rolling Blackouts", "Implement controlled blackouts",
                        5000000.0, 0.4, 0.3, mapOf<String, Int>(),
                        mapOf("gdp" to -40000000.0, "happiness" to -20.0, "infrastructure" to 10.0))
                )
            ),

            // Food Shortage
            CrisisTemplate(
                name = "Famine",
                description = "Widespread food shortages are occurring!",
                type = CrisisType.FOOD_SHORTAGE,
                baseEffects = mapOf("happiness" to -50.0, "healthcare" to -30.0, "population" to -100000.0, "stability" to -40.0),
                responseOptions = listOf(
                    CrisisResponse(1, "Food Imports", "Import emergency food supplies",
                        75000000.0, 0.85, 0.1, mapOf<String, Int>("treasury" to 50000000),
                        mapOf("happiness" to 30.0, "healthcare" to 20.0, "population" to 50000)),
                    CrisisResponse(2, "Rationing", "Implement strict food rationing",
                        10000000.0, 0.6, 0.2, mapOf<String, Int>(),
                        mapOf("happiness" to -20.0, "stability" to 10.0, "population" to -50000)),
                    CrisisResponse(3, "International Aid", "Request food aid from UN",
                        5000000.0, 0.7, 0.3, mapOf<String, Int>(),
                        mapOf("happiness" to 20.0, "internationalRelations" to -15.0))
                )
            ),
            
            // Energy Crisis
            CrisisTemplate(
                name = "Energy Crisis",
                description = "Critical energy shortages are affecting the nation!",
                type = CrisisType.ENERGY_CRISIS,
                baseEffects = mapOf("gdp" to -100000000.0, "infrastructure" to -20.0, "happiness" to -35.0),
                responseOptions = listOf(
                    CrisisResponse(1, "Emergency Imports", "Import emergency energy supplies",
                        100000000.0, 0.8, 0.15, mapOf<String, Int>("treasury" to 80000000),
                        mapOf("gdp" to 50000000.0, "infrastructure" to 15.0)),
                    CrisisResponse(2, "Rationing", "Implement energy rationing",
                        10000000.0, 0.5, 0.25, mapOf<String, Int>(),
                        mapOf("gdp" to -50000000.0, "happiness" to -15.0)),
                    CrisisResponse(3, "Strategic Reserve", "Release strategic petroleum reserve",
                        30000000.0, 0.7, 0.2, mapOf<String, Int>("infrastructure" to 50),
                        mapOf("gdp" to 30000000.0, "infrastructure" to 10.0))
                )
            ),

            // Refugee Crisis
            CrisisTemplate(
                name = "Refugee Crisis",
                description = "Massive influx of refugees at the border!",
                type = CrisisType.REFUGEE_CRISIS,
                baseEffects = mapOf("treasury" to -50000000.0, "stability" to -20.0, "happiness" to -15.0),
                responseOptions = listOf(
                    CrisisResponse(1, "Open Borders", "Accept all refugees",
                        100000000.0, 0.7, 0.3, mapOf<String, Int>("treasury" to 100000000),
                        mapOf("population" to 500000.0, "internationalRelations" to 20.0, "happiness" to -10.0)),
                    CrisisResponse(2, "Processing Centers", "Set up processing centers",
                        50000000.0, 0.6, 0.2, mapOf<String, Int>("infrastructure" to 50),
                        mapOf("internationalRelations" to 5.0, "stability" to -5.0)),
                    CrisisResponse(3, "Closed Borders", "Seal the borders",
                        20000000.0, 0.5, 0.5, mapOf<String, Int>("military" to 60),
                        mapOf("internationalRelations" to -30.0, "happiness" to -25.0, "stability" to 10.0))
                )
            ),

            // Civil War
            CrisisTemplate(
                name = "Civil War",
                description = "Armed rebellion has escalated to civil war!",
                type = CrisisType.CIVIL_WAR,
                baseEffects = mapOf("stability" to -60.0, "happiness" to -50.0, "gdp" to -200000000.0, "population" to -200000.0),
                responseOptions = listOf(
                    CrisisResponse(1, "Military Suppression", "Crush the rebellion militarily",
                        200000000.0, 0.6, 0.5, mapOf<String, Int>("military" to 70),
                        mapOf("stability" to 30.0, "happiness" to -30.0, "internationalRelations" to -25.0, "population" to -100000.0)),
                    CrisisResponse(2, "Negotiated Settlement", "Negotiate peace agreement",
                        50000000.0, 0.5, 0.3, mapOf<String, Int>("stability" to 40),
                        mapOf("stability" to 20.0, "happiness" to 10.0, "military" to -20.0)),
                    CrisisResponse(3, "Federalization", "Grant autonomy to regions",
                        30000000.0, 0.45, 0.4, mapOf<String, Int>(),
                        mapOf("stability" to 25.0, "happiness" to -10.0, "infrastructure" to -10.0))
                )
            )
        )
    }
    
    fun respondToCrisis(crisisIndex: Int, responseIndex: Int, country: Country): String {
        if (crisisIndex !in activeCrises.indices) return "Invalid crisis"
        
        val crisis = activeCrises[crisisIndex]
        if (responseIndex !in crisis.responseOptions.indices) return "Invalid response"
        
        val response = crisis.responseOptions[responseIndex]
        
        // Check requirements
        if (!checkResponseRequirements(response, country)) {
            return "Requirements not met!"
        }
        
        // Check cost
        if (country.treasury < response.cost) {
            return "Insufficient funds!"
        }
        
        // Apply cost
        country.treasury -= response.cost
        
        // Calculate effectiveness with modifiers
        var effectiveness = response.effectiveness
        
        // Emergency services bonus
        effectiveness += (emergencyServices.successRate - 85.0) / 100.0
        
        // Preparedness bonus
        effectiveness += (preparedness.getAverage() - 50.0) / 200.0
        
        // Roll for success
        val successRoll = Math.random()
        val isSuccess = successRoll < effectiveness
        
        // Apply consequences
        var message = if (isSuccess) "✓ Response Successful!\n" else "✗ Response Failed!\n"
        message += "${response.title}\n\n"
        
        if (isSuccess) {
            // Apply positive consequences
            response.consequences.forEach { (stat, value) ->
                if (value > 0) applyCrisisConsequence(stat, value, crisis, country)
            }
            crisis.containmentLevel = (crisis.containmentLevel + 30).coerceAtMost(100)
            message += "The crisis is being contained!"
        } else {
            // Apply negative consequences
            response.consequences.forEach { (stat, value) ->
                if (value < 0) applyCrisisConsequence(stat, value, crisis, country)
            }
            crisis.containmentLevel = (crisis.containmentLevel - 20).coerceAtLeast(0)
            message += "The situation is worsening!"
        }
        
        crisis.responseChosen = responseIndex
        
        return message
    }
    
    private fun checkResponseRequirements(response: CrisisResponse, country: Country): Boolean {
        response.requirements.forEach { (req, value) ->
            when (req) {
                "emergencyServices" -> if (emergencyServices.successRate < value) return false
                "internationalRelations" -> if (country.internationalRelations < value) return false
                "military" -> if (country.military < value) return false
                "treasury" -> if (country.treasury < value) return false
                "stability" -> if (country.stability < value) return false
                "healthcare" -> if (country.healthcare < value) return false
                "infrastructure" -> if (country.infrastructure < value) return false
                "technology" -> if (country.education < value) return false
                "preparedness" -> if (preparedness.getAverage() < value) return false
                "police" -> if (emergencyServices.policePersonnel < value * 1000) return false
            }
        }
        return true
    }
    
    private fun applyCrisisConsequence(stat: String, value: Double, crisis: Crisis, country: Country) {
        when (stat) {
            "stability" -> country.stability = (country.stability + value.toInt()).coerceIn(0, 100)
            "happiness" -> country.happiness = (country.happiness + value.toInt()).coerceIn(0, 100)
            "gdp" -> country.gdp = (country.gdp + value).coerceAtLeast(0.0)
            "treasury" -> country.treasury += value
            "infrastructure" -> country.infrastructure = (country.infrastructure + value.toInt()).coerceIn(0, 100)
            "healthcare" -> country.healthcare = (country.healthcare + value.toInt()).coerceIn(0, 100)
            "military" -> country.military = (country.military + value.toInt()).coerceIn(0, 100)
            "internationalRelations" -> country.internationalRelations = (country.internationalRelations + value.toInt()).coerceIn(0, 100)
            "environment" -> country.environment = (country.environment + value.toInt()).coerceIn(0, 100)
            "population" -> country.population = (country.population + value.toLong()).coerceAtLeast(0)
            "education" -> country.education = (country.education + value.toInt()).coerceIn(0, 100)
        }
    }
    
    fun processCrisisTurn(country: Country) {
        val crisesToRemove = mutableListOf<Crisis>()
        
        activeCrises.forEach { crisis ->
            crisis.turnsActive++
            
            // Apply ongoing effects
            crisis.effects.forEach { (stat, value) ->
                when (stat) {
                    "stability" -> country.stability = (country.stability + value.toInt()).coerceIn(0, 100)
                    "happiness" -> country.happiness = (country.happiness + value.toInt()).coerceIn(0, 100)
                    "gdp" -> country.gdp = (country.gdp + value).coerceAtLeast(0.0)
                    "infrastructure" -> country.infrastructure = (country.infrastructure + value.toInt()).coerceIn(0, 100)
                }
            }
            
            // Check if crisis is resolved
            if (crisis.containmentLevel >= 100 || crisis.turnsActive >= crisis.maxTurns) {
                crisis.isResolved = true
                crisesToRemove.add(crisis)
                resolvedCrises.add(crisis)
                
                if (crisis.containmentLevel >= 100) {
                    // Successfully resolved
                    country.happiness = (country.happiness + 5).coerceIn(0, 100)
                    country.stability = (country.stability + 5).coerceIn(0, 100)
                    crisesPrevented++
                } else {
                    // Crisis ran its course
                    country.happiness = (country.happiness - 5).coerceIn(0, 100)
                    crisesMismanaged++
                }
            }
            
            // Check for crisis escalation
            if (crisis.containmentLevel <= 0 && crisis.turnsActive < crisis.maxTurns / 2) {
                // Crisis is escalating
                crisis.severity = when (crisis.severity) {
                    CrisisSeverity.MINOR -> CrisisSeverity.MODERATE
                    CrisisSeverity.MODERATE -> CrisisSeverity.MAJOR
                    CrisisSeverity.MAJOR -> CrisisSeverity.SEVERE
                    CrisisSeverity.SEVERE -> CrisisSeverity.CATASTROPHIC
                    CrisisSeverity.CATASTROPHIC -> CrisisSeverity.CATASTROPHIC
                }
            }
        }
        
        activeCrises.removeAll(crisesToRemove)
        updateEmergencyLevel()
    }
    
    private fun updateEmergencyLevel() {
        nationalEmergencyLevel = when {
            activeCrises.isEmpty() -> 0
            activeCrises.size == 1 -> 1
            activeCrises.size == 2 -> 2
            activeCrises.size == 3 -> 3
            activeCrises.size == 4 -> 4
            else -> 5
        }
        
        // Upgrade if any catastrophic crisis
        if (activeCrises.any { it.severity == CrisisSeverity.CATASTROPHIC }) {
            nationalEmergencyLevel = nationalEmergencyLevel.coerceAtLeast(4)
        }
    }
    
    fun getPreparednessScore(): Double {
        return preparedness.getAverage()
    }
    
    private fun DisasterPreparedness.getAverage(): Double {
        return (earlyWarningSystem + evacuationPlans + emergencyShelters + stockpiles + 
                trainingLevel + publicAwareness + infrastructureResilience + communicationSystems) / 8.0
    }
    
    fun upgradePreparedness(stat: String, amount: Int, cost: Double, country: Country): Boolean {
        if (country.treasury < cost) return false
        
        country.treasury -= cost
        
        when (stat) {
            "earlyWarning" -> preparedness.earlyWarningSystem = (preparedness.earlyWarningSystem + amount).coerceIn(0, 100)
            "evacuation" -> preparedness.evacuationPlans = (preparedness.evacuationPlans + amount).coerceIn(0, 100)
            "shelters" -> preparedness.emergencyShelters = (preparedness.emergencyShelters + amount).coerceIn(0, 100)
            "stockpiles" -> preparedness.stockpiles = (preparedness.stockpiles + amount).coerceIn(0, 100)
            "training" -> preparedness.trainingLevel = (preparedness.trainingLevel + amount).coerceIn(0, 100)
            "awareness" -> preparedness.publicAwareness = (preparedness.publicAwareness + amount).coerceIn(0, 100)
            "resilience" -> preparedness.infrastructureResilience = (preparedness.infrastructureResilience + amount).coerceIn(0, 100)
            "communication" -> preparedness.communicationSystems = (preparedness.communicationSystems + amount).coerceIn(0, 100)
        }
        
        // Reduce crisis risk based on preparedness
        crisisRisk = (50.0 - preparedness.getAverage() * 0.4).coerceIn(5.0, 50.0)
        
        return true
    }
    
    fun upgradeEmergencyServices(stat: String, amount: Int, cost: Double, country: Country): Boolean {
        if (country.treasury < cost) return false
        
        country.treasury -= cost
        
        when (stat) {
            "policeFunding" -> emergencyServices.policeFunding += amount.toDouble()
            "fireFunding" -> emergencyServices.fireFunding += amount.toDouble()
            "emsFunding" -> emergencyServices.emsFunding += amount.toDouble()
            "femaFunding" -> emergencyServices.femaFunding += amount.toDouble()
            "policePersonnel" -> emergencyServices.policePersonnel += amount
            "firePersonnel" -> emergencyServices.firePersonnel += amount
            "emsPersonnel" -> emergencyServices.emsPersonnel += amount
        }
        
        // Recalculate success rate
        val avgFunding = (emergencyServices.policeFunding + emergencyServices.fireFunding + 
                         emergencyServices.emsFunding + emergencyServices.femaFunding) / 4.0
        val baseRate = 70.0 + (avgFunding / 1000000.0)
        emergencyServices.successRate = baseRate.coerceIn(50.0, 98.0)
        
        // Recalculate response time
        val totalPersonnel = emergencyServices.policePersonnel + emergencyServices.firePersonnel + emergencyServices.emsPersonnel
        emergencyServices.responseTime = (10.0 - (totalPersonnel / 50000.0)).coerceIn(1.0, 10.0)
        
        return true
    }
    
    fun getCrisisReport(): String {
        val report = StringBuilder()
        report.append("=== CRISIS REPORT ===\n\n")
        
        report.append("National Emergency Level: $nationalEmergencyLevel/5\n")
        report.append("Crisis Risk: ${crisisRisk.toInt()}%\n")
        report.append("Preparedness Score: ${getPreparednessScore().toInt()}%\n\n")
        
        if (activeCrises.isEmpty()) {
            report.append("No active crises.\n")
        } else {
            report.append("Active Crises (${activeCrises.size}):\n\n")
            activeCrises.forEach { crisis ->
                report.append("⚠️ ${crisis.name}\n")
                report.append("   Severity: ${crisis.severity}\n")
                report.append("   Region: ${GameWorld.getRegionById(crisis.region)?.name ?: "Unknown"}\n")
                report.append("   Duration: ${crisis.turnsActive}/${crisis.maxTurns} turns\n")
                report.append("   Containment: ${crisis.containmentLevel}%\n")
                report.append("   Casualties: ${crisis.casualties}\n")
                report.append("   Economic Damage: $${String.format("%,d", crisis.economicDamage.toLong())}\n\n")
            }
        }
        
        report.append("\n=== STATISTICS ===\n")
        report.append("Total Casualties: $totalCasualties\n")
        report.append("Total Economic Damage: $${String.format("%,d", totalEconomicDamage.toLong())}\n")
        report.append("Crises Prevented: $crisesPrevented\n")
        report.append("Crises Mismanaged: $crisesMismanaged\n")
        
        return report.toString()
    }
}
