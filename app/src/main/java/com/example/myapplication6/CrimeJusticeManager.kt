package com.example.myapplication6

import java.io.Serializable

/**
 * Country Sim v3.0 - Crime & Justice System
 * Handles law enforcement, crime rates, and judicial processes
 */

// Crime types
enum class CrimeType {
    PETTY_CRIME,
    THEFT,
    ASSAULT,
    ROBBERY,
    BURGLARY,
    DRUG_TRAFFICKING,
    ORGANIZED_CRIME,
    WHITE_COLLAR_CRIME,
    CYBERCRIME,
    TERRORISM,
    MURDER,
    KIDNAPPING,
    FRAUD,
    CORRUPTION,
    HUMAN_TRAFFICKING
}

// Crime incident
data class CrimeIncident(
    val id: Int,
    val type: CrimeType,
    val severity: Int, // 1-100
    val region: Int,
    val solved: Boolean,
    val casualties: Int,
    val economicLoss: Double,
    val turnReported: Int
) : Serializable

// Law enforcement agency
data class PoliceAgency(
    val id: Int,
    val name: String,
    val type: PoliceAgencyType,
    var budget: Double,
    var personnel: Int,
    var equipment: Int,
    var successRate: Double,
    var corruptionLevel: Int,
    var activeOperations: Int = 0
) : Serializable

enum class PoliceAgencyType {
    LOCAL_POLICE,
    STATE_POLICE,
    FEDERAL_INVESTIGATION,
    SPECIAL_FORCES,
    ANTI_CORRUPTION,
    CYBER_CRIME_UNIT,
    DRUG_ENFORCEMENT,
    COUNTER_TERRORISM
}

// Justice case
data class JusticeCase(
    val id: Int,
    val defendant: String,
    val charges: List<CrimeType>,
    val evidence: Int, // 0-100
    val publicAttention: Int, // 0-100
    val isOngoing: Boolean,
    val turnFiled: Int
) : Serializable

// Crime statistics
data class CrimeStatistics(
    var totalCrimes: Int = 0,
    var solvedCrimes: Int = 0,
    var unsolvedCrimes: Int = 0,
    var crimeRate: Double = 50.0, // per 100k population
    var murderRate: Double = 5.0,
    var incarcerationRate: Double = 500.0, // per 100k
    var recidivismRate: Double = 30.0,
    var averageSentence: Double = 5.0, // years
    var prisonPopulation: Int = 0,
    var policeBudget: Double = 0.0,
    var judicialBudget: Double = 0.0
) : Serializable

// Punishment types
enum class PunishmentType {
    FINE,
    COMMUNITY_SERVICE,
    PROBATION,
    HOUSE_ARREST,
    PRISON,
    LIFE_IMPRISONMENT,
    DEATH_PENALTY,
    REHABILITATION,
    DEPORTATION
}

// Object for crime and justice management
object CrimeJusticeManager : Serializable {

    val crimeIncidents = mutableListOf<CrimeIncident>()
    val policeAgencies = mutableListOf<PoliceAgency>()
    val activeCases = mutableListOf<JusticeCase>()
    val statistics = CrimeStatistics()

    var crimeRate: Double = 50.0
    var clearanceRate: Double = 50.0
    var publicTrustInPolice: Double = 50.0
    var corruptionPerception: Double = 50.0
    var prisonOvercrowding: Double = 0.0
    var deathPenaltyLegal: Boolean = false
    var threeStrikesLaw: Boolean = false

    fun initializeCrimeJustice() {
        crimeIncidents.clear()
        policeAgencies.clear()
        activeCases.clear()

        // Create starting police agencies
        policeAgencies.add(PoliceAgency(1, "Capital Police Department", PoliceAgencyType.LOCAL_POLICE,
            50000000.0, 5000, 70, 65.0, 15))
        policeAgencies.add(PoliceAgency(2, "Federal Investigation Bureau", PoliceAgencyType.FEDERAL_INVESTIGATION,
            100000000.0, 10000, 85, 80.0, 10))
        policeAgencies.add(PoliceAgency(3, "Drug Enforcement Administration", PoliceAgencyType.DRUG_ENFORCEMENT,
            40000000.0, 3000, 75, 70.0, 20))
        policeAgencies.add(PoliceAgency(4, "Counter-Terrorism Unit", PoliceAgencyType.COUNTER_TERRORISM,
            60000000.0, 2000, 90, 75.0, 5))
        policeAgencies.add(PoliceAgency(5, "Cyber Crime Division", PoliceAgencyType.CYBER_CRIME_UNIT,
            30000000.0, 1500, 80, 60.0, 10))

        updateStatistics()
    }

    fun generateCrime(country: Country): CrimeIncident? {
        // Base crime chance
        var crimeChance = crimeRate / 100.0

        // Modify based on country stats
        if (country.happiness < 40) crimeChance *= 1.5
        if (country.stability < 40) crimeChance *= 1.3
        if (country.education < 40) crimeChance *= 1.2
        if (country.infrastructure < 40) crimeChance *= 1.1

        // Police effectiveness reduces crime
        crimeChance *= (1.0 - (clearanceRate / 200.0))

        if (Math.random() < crimeChance * 0.3) { // 30% max chance per turn
            val crimeTypes = CrimeType.values()
            val type = crimeTypes.random()

            val severity = when (type) {
                CrimeType.PETTY_CRIME -> (10..30).random()
                CrimeType.THEFT, CrimeType.BURGLARY -> (30..50).random()
                CrimeType.ASSAULT, CrimeType.ROBBERY -> (50..70).random()
                CrimeType.DRUG_TRAFFICKING, CrimeType.ORGANIZED_CRIME -> (60..80).random()
                CrimeType.MURDER, CrimeType.TERRORISM -> (80..100).random()
                else -> (20..60).random()
            }

            val casualties = when {
                severity > 80 -> (1..10).random()
                severity > 60 -> (0..3).random()
                else -> 0
            }

            val economicLoss = when (severity) {
                in 0..30 -> (10000.0..100000.0).random()
                in 31..60 -> (100000.0..1000000.0).random()
                in 61..80 -> (1000000.0..10000000.0).random()
                else -> (10000000.0..100000000.0).random()
            }

            val region = if (GameWorld.getUnlockedRegions().isNotEmpty()) {
                GameWorld.getUnlockedRegions().random().id
            } else 0

            val incident = CrimeIncident(
                id = crimeIncidents.size + 1,
                type = type,
                severity = severity,
                region = region,
                solved = false,
                casualties = casualties,
                economicLoss = economicLoss,
                turnReported = country.turn
            )

            crimeIncidents.add(incident)

            // Apply effects
            country.happiness = (country.happiness - (severity / 50)).coerceIn(0, 100)
            country.stability = (country.stability - (severity / 100)).coerceIn(0, 100)
            country.treasury -= economicLoss * 0.1 // 10% economic impact

            // Try to solve the crime
            if (Math.random() < (clearanceRate / 100.0)) {
                incident.solved = true
                statistics.solvedCrimes++
            } else {
                statistics.unsolvedCrimes++
            }

            statistics.totalCrimes++
            updateStatistics()

            return incident
        }

        return null
    }

    fun investigateCrime(crimeId: Int, agencyId: Int, country: Country): String {
        val crime = crimeIncidents.find { it.id == crimeId }
        val agency = policeAgencies.find { it.id == agencyId }

        if (crime == null || crime.solved) return "Crime not found or already solved"
        if (agency == null) return "Agency not found"

        val cost = (100000.0..1000000.0).random()
        if (country.treasury < cost) return "Insufficient funds for investigation"

        country.treasury -= cost
        agency.activeOperations++

        val successChance = agency.successRate / 100.0 * (crime.severity / 100.0)

        if (Math.random() < successChance) {
            crime.solved = true
            agency.activeOperations--
            statistics.solvedCrimes++
            statistics.unsolvedCrimes--

            country.stability = (country.stability + 2).coerceIn(0, 100)
            publicTrustInPolice = (publicTrustInPolice + 3).coerceIn(0, 100)

            return "Crime solved! ${agency.name} successfully investigated the case."
        }

        agency.activeOperations--
        return "Investigation failed. ${agency.name} could not solve the case."
    }

    fun prosecuteCase(case: JusticeCase, country: Country): String {
        if (!case.isOngoing) return "Case is not ongoing"

        val convictionChance = case.evidence / 100.0

        if (Math.random() < convictionChance) {
            // Convicted
            val punishment = determinePunishment(case.charges, country)
            applyPunishment(punishment, country)

            activeCases.remove(case)
            statistics.prisonPopulation += when (punishment) {
                PunishmentType.PRISON -> (5..20).random()
                PunishmentType.LIFE_IMPRISONMENT -> 1
                else -> 0
            }

            country.stability = (country.stability + 3).coerceIn(0, 100)
            publicTrustInPolice = (publicTrustInPolice + 5).coerceIn(0, 100)

            return "Guilty! Defendant sentenced to ${punishment}."
        } else {
            // Acquitted
            activeCases.remove(case)

            country.stability = (country.stability - 2).coerceIn(0, 100)
            publicTrustInPolice = (publicTrustInPolice - 3).coerceIn(0, 100)

            return "Not guilty. Defendant acquitted due to insufficient evidence."
        }
    }

    private fun determinePunishment(charges: List<CrimeType>, country: Country): PunishmentType {
        val maxSeverity = charges.maxOfOrNull { charge ->
            when (charge) {
                CrimeType.PETTY_CRIME -> 10
                CrimeType.THEFT, CrimeType.BURGLARY -> 30
                CrimeType.ASSAULT, CrimeType.ROBBERY -> 50
                CrimeType.DRUG_TRAFFICKING, CrimeType.ORGANIZED_CRIME -> 70
                CrimeType.MURDER, CrimeType.TERRORISM -> 100
                else -> 40
            }
        } ?: 50

        return when {
            maxSeverity >= 90 -> if (deathPenaltyLegal) PunishmentType.DEATH_PENALTY else PunishmentType.LIFE_IMPRISONMENT
            maxSeverity >= 70 -> PunishmentType.PRISON
            maxSeverity >= 50 -> PunishmentType.PRISON
            maxSeverity >= 30 -> PunishmentType.PROBATION
            else -> PunishmentType.FINE
        }
    }

    private fun applyPunishment(punishment: PunishmentType, country: Country) {
        when (punishment) {
            PunishmentType.FINE -> country.treasury += (10000.0..100000.0).random()
            PunishmentType.PRISON -> {
                statistics.prisonPopulation += (5..20).random()
                country.treasury -= 50000.0 // Annual prison cost
            }
            PunishmentType.LIFE_IMPRISONMENT -> {
                statistics.prisonPopulation += 1
                country.treasury -= 100000.0
            }
            PunishmentType.DEATH_PENALTY -> {
                country.internationalRelations = (country.internationalRelations - 5).coerceIn(0, 100)
                country.stability = (country.stability + 5).coerceIn(0, 100)
            }
            PunishmentType.REHABILITATION -> {
                country.treasury -= 20000.0
                country.education = (country.education + 1).coerceIn(0, 100)
            }
            else -> {}
        }
    }

    fun setPoliceBudget(agencyId: Int, budget: Double, country: Country): Boolean {
        val agency = policeAgencies.find { it.id == agencyId }
        if (agency == null) return false

        val budgetChange = budget - agency.budget
        if (country.treasury < budgetChange) return false

        country.treasury -= budgetChange
        agency.budget = budget

        // Update success rate based on budget
        agency.successRate = (50.0 + (budget / 1000000.0)).coerceIn(50.0, 95.0)

        updateStatistics()
        return true
    }

    fun recruitPolicePersonnel(agencyId: Int, count: Int, cost: Double, country: Country): Boolean {
        val agency = policeAgencies.find { it.id == agencyId }
        if (agency == null || country.treasury < cost) return false

        country.treasury -= cost
        agency.personnel += count

        // Update success rate
        agency.successRate = (agency.successRate + (count / 100.0)).coerceIn(50.0, 95.0)

        return true
    }

    fun updateCrimeRate(country: Country) {
        // Calculate new crime rate based on various factors
        var newCrimeRate = 50.0

        // Economic factors
        if (country.gdp < 1000000000) newCrimeRate += 20.0
        if (country.treasury < 0) newCrimeRate += 10.0

        // Social factors
        if (country.happiness < 40) newCrimeRate += 15.0
        if (country.education < 40) newCrimeRate += 10.0
        if (country.stability < 40) newCrimeRate += 15.0

        // Law enforcement factors
        newCrimeRate -= (clearanceRate / 4.0)
        newCrimeRate -= (publicTrustInPolice / 5.0)

        // Police funding
        val totalPoliceBudget = policeAgencies.sumOf { it.budget }
        newCrimeRate -= (totalPoliceBudget / 10000000.0)

        crimeRate = newCrimeRate.coerceIn(10.0, 100.0)
        clearanceRate = policeAgencies.map { it.successRate }.average().coerceIn(30.0, 95.0)
    }

    private fun updateStatistics() {
        statistics.crimeRate = crimeRate
        statistics.clearanceRate = clearanceRate
        statistics.policeBudget = policeAgencies.sumOf { it.budget }
        statistics.prisonPopulation = statistics.prisonPopulation
        statistics.incarcerationRate = (statistics.prisonPopulation.toDouble() / 100000.0) * 100000
    }

    fun getCrimeReport(): String {
        val report = StringBuilder()
        report.append("=== CRIME & JUSTICE REPORT ===\n\n")

        report.append("Crime Rate: ${crimeRate.toInt()}/100\n")
        report.append("Clearance Rate: ${clearanceRate.toInt()}%\n")
        report.append("Public Trust in Police: ${publicTrustInPolice.toInt()}%\n")
        report.append("Corruption Perception: ${corruptionPerception.toInt()}%\n")
        report.append("Prison Population: ${statistics.prisonPopulation}\n\n")

        report.append("Police Agencies (${policeAgencies.size}):\n")
        policeAgencies.forEach { agency ->
            report.append("- ${agency.name} (${agency.type})\n")
            report.append("  Budget: $${String.format("%,d", (agency.budget / 1000000).toLong())}M | Personnel: ${agency.personnel}\n")
            report.append("  Success Rate: ${agency.successRate.toInt()}% | Corruption: ${agency.corruptionLevel}%\n\n")
        }

        if (activeCases.isNotEmpty()) {
            report.append("Active Cases (${activeCases.size}):\n")
            activeCases.forEach { case ->
                report.append("- ${case.defendant}: ${case.charges.size} charges\n")
                report.append("  Evidence: ${case.evidence}% | Public Attention: ${case.publicAttention}%\n\n")
            }
        }

        report.append("=== STATISTICS ===\n")
        report.append("Total Crimes: ${statistics.totalCrimes}\n")
        report.append("Solved: ${statistics.solvedCrimes} | Unsolved: ${statistics.unsolvedCrimes}\n")
        report.append("Murder Rate: ${statistics.murderRate} per 100k\n")

        return report.toString()
    }
}
