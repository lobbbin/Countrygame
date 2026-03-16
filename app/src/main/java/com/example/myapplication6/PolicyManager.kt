package com.example.myapplication6

import java.io.Serializable

/**
 * Advanced Policy and Laws Management System
 */

// Law types
enum class LawType {
    CONSTITUTIONAL,
    CIVIL,
    CRIMINAL,
    ECONOMIC,
    SOCIAL,
    ENVIRONMENTAL,
    LABOR,
    EDUCATION,
    HEALTHCARE,
    DEFENSE,
    IMMIGRATION,
    TRADE
}

// Law status
enum class LawStatus {
    PROPOSED,
    IN_COMMITTEE,
    IN_PARLIAMENT,
    PASSED,
    VETOED,
    REPEALED,
    ACTIVE,
    EXPIRED
}

// Political party
data class PoliticalParty(
    val id: Int,
    val name: String,
    val ideology: PoliticalIdeology,
    val leader: String,
    var seats: Int,
    val color: String,
    val policies: List<String>,
    var popularity: Double = 20.0,
    val baseVoters: Double = 15.0
) : Serializable

enum class PoliticalIdeology {
    CONSERVATIVE,
    LIBERAL,
    SOCIALIST,
    LIBERTARIAN,
    GREEN,
    NATIONALIST,
    CENTRIST,
    COMMUNIST,
    FASCIST
}

// Government ministry
data class Ministry(
    val id: Int,
    val name: String,
    val minister: NPC,
    val budget: Double,
    var efficiency: Double,
    val projects: MutableList<GovernmentProject>,
    val employees: Int,
    val description: String
) : Serializable

// Government project
data class GovernmentProject(
    val id: Int,
    val name: String,
    val description: String,
    val ministry: Int,
    val cost: Double,
    val duration: Int,
    val benefits: Map<String, Double>,
    var progress: Int = 0,
    var isCompleted: Boolean = false,
    var isCancelled: Boolean = false,
    var funding: Double = 0.0
) : Serializable

// Cabinet member
data class CabinetMember(
    val npc: NPC,
    val position: String,
    val approvalRating: Double,
    val scandals: Int,
    val accomplishments: Int
) : Serializable

// Parliamentary vote
data class ParliamentaryVote(
    val law: Law,
    val votesFor: Int,
    val votesAgainst: Int,
    val abstentions: Int,
    val isPassed: Boolean
) : Serializable

// Law proposal
data class Law(
    val id: Int,
    val name: String,
    val description: String,
    val lawType: LawType,
    val proposer: String,
    val effects: Map<String, Double>,
    val cost: Double,
    val duration: Int,
    var status: LawStatus = LawStatus.PROPOSED,
    val supportRequired: Double = 50.0,
    var controversy: Double = 50.0,
    var lobbySupport: Double = 0.0,
    var lobbyOpposition: Double = 0.0,
    var turnsInCommittee: Int = 0,
    var turnsInParliament: Int = 0
) : Serializable

// Lobby group
data class LobbyGroup(
    val id: Int,
    val name: String,
    val interest: String,
    val budget: Double,
    val influence: Double,
    val alliedParties: List<Int>,
    val opposedParties: List<Int>
) : Serializable

// Public opinion poll
data class OpinionPoll(
    val topic: String,
    val support: Double,
    val oppose: Double,
    val undecided: Double,
    val margin: Double,
    val sampleSize: Int,
    val date: String
) : Serializable

// Approval ratings
data class ApprovalRatings(
    var presidentApproval: Double = 50.0,
    var congressApproval: Double = 40.0,
    var supremeCourtApproval: Double = 55.0,
    var mediaApproval: Double = 45.0,
    var internationalApproval: Double = 50.0
) : Serializable

// Object for policy management
object PolicyManager : Serializable {
    
    val laws = mutableListOf<Law>()
    val parties = mutableListOf<PoliticalParty>()
    val ministries = mutableListOf<Ministry>()
    val cabinet = mutableListOf<CabinetMember>()
    val lobbyGroups = mutableListOf<LobbyGroup>()
    val opinionPolls = mutableListOf<OpinionPoll>()
    val approvalRatings = ApprovalRatings()
    
    var totalSeats: Int = 100
    var rulingCoalition: List<Int> = listOf(0)
    var oppositionParties: List<Int> = listOf(1, 2)
    var nextElectionTurns: Int = 20
    var constitutionalMajority: Int = 67
    var simpleMajority: Int = 51
    
    fun initializePolicySystem() {
        laws.clear()
        parties.clear()
        ministries.clear()
        cabinet.clear()
        lobbyGroups.clear()
        
        initializeParties()
        initializeMinistries()
        initializeLobbyGroups()
        initializeStartingLaws()
    }
    
    private fun initializeParties() {
        parties.add(PoliticalParty(
            id = 0,
            name = "National Unity Party",
            ideology = PoliticalIdeology.CENTRIST,
            leader = "Margaret Chen",
            seats = 35,
            color = "#1976D2",
            policies = listOf("Economic Growth", "Social Stability", "Pragmatic Governance")
        ))
        
        parties.add(PoliticalParty(
            id = 1,
            name = "Progressive Alliance",
            ideology = PoliticalIdeology.SOCIALIST,
            leader = "Maya Patel",
            seats = 25,
            color = "#D32F2F",
            policies = listOf("Social Justice", "Wealth Redistribution", "Universal Healthcare")
        ))
        
        parties.add(PoliticalParty(
            id = 2,
            name = "Freedom & Enterprise",
            ideology = PoliticalIdeology.LIBERAL,
            leader = "Robert Blackwood",
            seats = 20,
            color = "#388E3C",
            policies = listOf("Free Market", "Low Taxes", "Limited Government")
        ))
        
        parties.add(PoliticalParty(
            id = 3,
            name = "Green Future",
            ideology = PoliticalIdeology.GREEN,
            leader = "Dr. Elena Vasquez",
            seats = 10,
            color = "#4CAF50",
            policies = listOf("Climate Action", "Renewable Energy", "Conservation")
        ))
        
        parties.add(PoliticalParty(
            id = 4,
            name = "National Front",
            ideology = PoliticalIdeology.NATIONALIST,
            leader = "General Marcus Stone",
            seats = 10,
            color = "#795548",
            policies = listOf("Strong Defense", "Border Security", "National Sovereignty")
        ))
    }
    
    private fun initializeMinistries() {
        // Get NPCs for minister positions
        val chiefOfStaff = NPCManager.getNPCById(0)
        val general = NPCManager.getNPCById(1)
        val economist = NPCManager.getNPCById(2)
        val diplomat = NPCManager.getNPCById(3)
        val scientist = NPCManager.getNPCById(4)
        
        ministries.add(Ministry(
            id = 0,
            name = "Ministry of Finance",
            minister = economist ?: createDefaultNPC("Finance Minister"),
            budget = 50000000.0,
            efficiency = 70.0,
            projects = mutableListOf(),
            employees = 5000,
            description = "Manages national budget, taxation, and economic policy"
        ))
        
        ministries.add(Ministry(
            id = 1,
            name = "Ministry of Defense",
            minister = general ?: createDefaultNPC("Defense Minister"),
            budget = 80000000.0,
            efficiency = 75.0,
            projects = mutableListOf(),
            employees = 10000,
            description = "Oversees armed forces and national security"
        ))
        
        ministries.add(Ministry(
            id = 2,
            name = "Ministry of Foreign Affairs",
            minister = diplomat ?: createDefaultNPC("Foreign Minister"),
            budget = 30000000.0,
            efficiency = 65.0,
            projects = mutableListOf(),
            employees = 3000,
            description = "Handles international relations and diplomacy"
        ))
        
        ministries.add(Ministry(
            id = 3,
            name = "Ministry of Education",
            minister = scientist ?: createDefaultNPC("Education Minister"),
            budget = 40000000.0,
            efficiency = 60.0,
            projects = mutableListOf(),
            employees = 8000,
            description = "Manages schools, universities, and research institutions"
        ))
        
        ministries.add(Ministry(
            id = 4,
            name = "Ministry of Health",
            minister = createDefaultNPC("Health Minister"),
            budget = 45000000.0,
            efficiency = 68.0,
            projects = mutableListOf(),
            employees = 12000,
            description = "Oversees healthcare system and public health"
        ))
        
        ministries.add(Ministry(
            id = 5,
            name = "Ministry of Infrastructure",
            minister = createDefaultNPC("Infrastructure Minister"),
            budget = 60000000.0,
            efficiency = 62.0,
            projects = mutableListOf(),
            employees = 15000,
            description = "Builds and maintains roads, bridges, and public works"
        ))
        
        ministries.add(Ministry(
            id = 6,
            name = "Ministry of Environment",
            minister = createDefaultNPC("Environment Minister"),
            budget = 20000000.0,
            efficiency = 55.0,
            projects = mutableListOf(),
            employees = 4000,
            description = "Protects natural resources and manages environmental policy"
        ))
        
        ministries.add(Ministry(
            id = 7,
            name = "Ministry of Justice",
            minister = createDefaultNPC("Justice Minister"),
            budget = 35000000.0,
            efficiency = 72.0,
            projects = mutableListOf(),
            employees = 20000,
            description = "Administers courts, prisons, and legal system"
        ))
        
        ministries.add(Ministry(
            id = 8,
            name = "Ministry of Labor",
            minister = createDefaultNPC("Labor Minister"),
            budget = 25000000.0,
            efficiency = 58.0,
            projects = mutableListOf(),
            employees = 6000,
            description = "Protects worker rights and manages employment programs"
        ))
        
        ministries.add(Ministry(
            id = 9,
            name = "Ministry of Science & Technology",
            minister = scientist ?: createDefaultNPC("Science Minister"),
            budget = 35000000.0,
            efficiency = 70.0,
            projects = mutableListOf(),
            employees = 5000,
            description = "Promotes research, development, and innovation"
        ))
    }
    
    private fun createDefaultNPC(name: String): NPC {
        return NPC(
            id = 100 + ministries.size,
            name = name,
            role = NPCRole.ADVISOR,
            description = "Government official",
            relationship = 50,
            influence = 50,
            personality = Personality(
                trait1 = PersonalityTrait.PRAGMATIC,
                trait2 = PersonalityTrait.HONEST,
                motivation = "Serve the nation",
                fear = "Failure",
                values = listOf("Duty", "Service")
            )
        )
    }
    
    private fun initializeLobbyGroups() {
        lobbyGroups.add(LobbyGroup(
            id = 0,
            name = "Chamber of Commerce",
            interest = "Business",
            budget = 50000000.0,
            influence = 70.0,
            alliedParties = listOf(2),
            opposedParties = listOf(1)
        ))
        
        lobbyGroups.add(LobbyGroup(
            id = 1,
            name = "Labor Union Federation",
            interest = "Workers",
            budget = 30000000.0,
            influence = 60.0,
            alliedParties = listOf(1),
            opposedParties = listOf(2)
        ))
        
        lobbyGroups.add(LobbyGroup(
            id = 2,
            name = "Environmental Coalition",
            interest = "Environment",
            budget = 20000000.0,
            influence = 50.0,
            alliedParties = listOf(3),
            opposedParties = listOf(4)
        ))
        
        lobbyGroups.add(LobbyGroup(
            id = 3,
            name = "Defense Contractors Association",
            interest = "Military",
            budget = 40000000.0,
            influence = 65.0,
            alliedParties = listOf(4),
            opposedParties = listOf(1, 3)
        ))
        
        lobbyGroups.add(LobbyGroup(
            id = 4,
            name = "Healthcare Industry Group",
            interest = "Healthcare",
            budget = 45000000.0,
            influence = 72.0,
            alliedParties = listOf(0, 2),
            opposedParties = listOf(1)
        ))
        
        lobbyGroups.add(LobbyGroup(
            id = 5,
            name = "Education Alliance",
            interest = "Education",
            budget = 15000000.0,
            influence = 45.0,
            alliedParties = listOf(0, 1),
            opposedParties = listOf(2)
        ))
    }
    
    private fun initializeStartingLaws() {
        // Starting active laws
        laws.add(Law(
            id = 0,
            name = "Constitution of the Nation",
            description = "The supreme law of the land",
            lawType = LawType.CONSTITUTIONAL,
            proposer = "Founding Fathers",
            effects = emptyMap(),
            cost = 0.0,
            duration = -1,
            status = LawStatus.ACTIVE
        ))
        
        laws.add(Law(
            id = 1,
            name = "Basic Tax Code",
            description = "Standard taxation framework",
            lawType = LawType.ECONOMIC,
            proposer = "Parliament",
            effects = mapOf("treasury" to 50000000.0),
            cost = 0.0,
            duration = -1,
            status = LawStatus.ACTIVE
        ))
        
        laws.add(Law(
            id = 2,
            name = "Labor Standards Act",
            description = "Basic worker protections",
            lawType = LawType.LABOR,
            proposer = "Ministry of Labor",
            effects = mapOf("happiness" to 5.0),
            cost = 0.0,
            duration = -1,
            status = LawStatus.ACTIVE
        ))
    }
    
    fun proposeLaw(law: Law, country: Country): Boolean {
        if (country.treasury < law.cost) {
            return false
        }
        
        country.treasury -= law.cost
        laws.add(law)
        
        // Calculate initial support based on party positions
        calculateLawSupport(law)
        
        return true
    }
    
    fun calculateLawSupport(law: Law) {
        var totalSupport = 0.0
        var totalSeats = 0.0
        
        parties.forEach { party ->
            val partySupport = calculatePartySupport(party, law)
            totalSupport += partySupport * party.seats
            totalSeats += party.seats
        }
        
        // Add lobby influence
        totalSupport += law.lobbySupport - law.lobbyOpposition
        
        law.controversy = 100 - Math.abs(50 - (totalSupport / totalSeats * 100)) * 2
    }
    
    private fun calculatePartySupport(party: PoliticalParty, law: Law): Double {
        // Base support based on ideology alignment
        val ideologySupport = when (party.ideology) {
            PoliticalIdeology.CENTRIST -> 50.0
            PoliticalIdeology.SOCIALIST -> if (law.lawType == LawType.SOCIAL || law.lawType == LawType.LABOR) 70.0 else 30.0
            PoliticalIdeology.LIBERAL -> if (law.lawType == LawType.ECONOMIC || law.lawType == LawType.TRADE) 70.0 else 40.0
            PoliticalIdeology.GREEN -> if (law.lawType == LawType.ENVIRONMENTAL) 80.0 else 30.0
            PoliticalIdeology.NATIONALIST -> if (law.lawType == LawType.DEFENSE || law.lawType == LawType.IMMIGRATION) 75.0 else 35.0
            else -> 45.0
        }
        
        // Adjust based on law effects
        var support = ideologySupport
        
        law.effects.forEach { (stat, value) ->
            when {
                value > 0 -> support += 5.0
                value < 0 -> support -= 5.0
            }
        }
        
        return support.coerceIn(0.0, 100.0)
    }
    
    fun processLawInCommittee(law: Law): Boolean {
        law.turnsInCommittee++
        
        // Lobby groups try to influence
        lobbyGroups.forEach { lobby ->
            if (lobby.alliedParties.any { it in rulingCoalition }) {
                law.lobbySupport += lobby.influence * 0.1
            }
            if (lobby.opposedParties.any { it in rulingCoalition }) {
                law.lobbyOpposition += lobby.influence * 0.1
            }
        }
        
        // Committee vote after 2 turns
        if (law.turnsInCommittee >= 2) {
            val committeeScore = (law.lobbySupport - law.lobbyOpposition + 50) / 100.0
            if (committeeScore >= 0.4) {
                law.status = LawStatus.IN_PARLIAMENT
                return true
            } else {
                law.status = LawStatus.VETOED
                return false
            }
        }
        
        return true
    }
    
    fun processLawInParliament(law: Law): ParliamentaryVote {
        law.turnsInParliament++
        
        var votesFor = 0
        var votesAgainst = 0
        var abstentions = 0
        
        parties.forEach { party ->
            val support = calculatePartySupport(party, law)
            val partyVotes = party.seats
            
            val forVotes = (partyVotes * support / 100.0).toInt()
            val againstVotes = (partyVotes * (100 - support) / 100.0 * 0.8).toInt()
            val abstainVotes = partyVotes - forVotes - againstVotes
            
            votesFor += forVotes
            votesAgainst += againstVotes
            abstentions += abstainVotes
        }
        
        val isPassed = votesFor > votesAgainst
        
        law.status = if (isPassed) LawStatus.PASSED else LawStatus.VETOED
        
        return ParliamentaryVote(law, votesFor, votesAgainst, abstentions, isPassed)
    }
    
    fun enactLaw(law: Law, country: Country) {
        law.status = LawStatus.ACTIVE
        
        // Apply law effects
        law.effects.forEach { (stat, value) ->
            when (stat) {
                "treasury" -> country.treasury += value
                "gdp" -> country.gdp = (country.gdp + value).coerceAtLeast(0.0)
                "happiness" -> country.happiness = (country.happiness + value.toInt()).coerceIn(0, 100)
                "stability" -> country.stability = (country.stability + value.toInt()).coerceIn(0, 100)
                "education" -> country.education = (country.education + value.toInt()).coerceIn(0, 100)
                "healthcare" -> country.healthcare = (country.healthcare + value.toInt()).coerceIn(0, 100)
                "infrastructure" -> country.infrastructure = (country.infrastructure + value.toInt()).coerceIn(0, 100)
                "environment" -> country.environment = (country.environment + value.toInt()).coerceIn(0, 100)
                "military" -> country.military = (country.military + value.toInt()).coerceIn(0, 100)
                "internationalRelations" -> country.internationalRelations = (country.internationalRelations + value.toInt()).coerceIn(0, 100)
            }
        }
    }
    
    fun getAvailableLaws(): List<Law> {
        return listOf(
            Law(10, "Minimum Wage Act", "Establish a national minimum wage", LawType.LABOR,
                "Government", mapOf("happiness" to 10.0, "treasury" to -5000000.0), 0.0, -1),
            Law(11, "Healthcare Reform", "Universal healthcare coverage", LawType.HEALTHCARE,
                "Government", mapOf("healthcare" to 25.0, "happiness" to 15.0, "treasury" to -50000000.0), 100000000.0, -1),
            Law(12, "Education Investment", "Increase education funding", LawType.EDUCATION,
                "Government", mapOf("education" to 20.0, "gdp" to 30000000.0, "treasury" to -30000000.0), 50000000.0, -1),
            Law(13, "Environmental Protection Act", "Stricter environmental regulations", LawType.ENVIRONMENTAL,
                "Government", mapOf("environment" to 25.0, "gdp" to -20000000.0, "treasury" to 10000000.0), 20000000.0, -1),
            Law(14, "Tax Reform", "Comprehensive tax code overhaul", LawType.ECONOMIC,
                "Government", mapOf("treasury" to 20000000.0, "gdp" to 50000000.0, "happiness" to -5.0), 10000000.0, -1),
            Law(15, "Defense Modernization", "Upgrade military equipment", LawType.DEFENSE,
                "Government", mapOf("military" to 20.0, "treasury" to -80000000.0), 100000000.0, -1),
            Law(16, "Immigration Reform", "New immigration policies", LawType.IMMIGRATION,
                "Government", mapOf("population" to 50000.0, "happiness" to -10.0, "gdp" to 20000000.0), 5000000.0, -1),
            Law(17, "Trade Liberalization", "Reduce trade barriers", LawType.TRADE,
                "Government", mapOf("gdp" to 40000000.0, "internationalRelations" to 10.0, "treasury" to -10000000.0), 0.0, -1),
            Law(18, "Criminal Justice Reform", "Reform prison system", LawType.CRIMINAL,
                "Government", mapOf("stability" to -5.0, "happiness" to 10.0, "treasury" to -15000000.0), 30000000.0, -1),
            Law(19, "Civil Rights Act", "Expand civil protections", LawType.CIVIL,
                "Government", mapOf("happiness" to 15.0, "stability" to 5.0), 5000000.0, -1),
            Law(20, "Infrastructure Bill", "Major infrastructure investment", LawType.ECONOMIC,
                "Government", mapOf("infrastructure" to 25.0, "gdp" to 80000000.0, "treasury" to -100000000.0), 150000000.0, -1),
            Law(21, "Social Security Reform", "Pension system overhaul", LawType.SOCIAL,
                "Government", mapOf("happiness" to 10.0, "treasury" to 20000000.0, "stability" to -10.0), 10000000.0, -1),
            Law(22, "Research & Development Act", "Boost scientific research", LawType.EDUCATION,
                "Government", mapOf("education" to 15.0, "gdp" to 40000000.0, "treasury" to -40000000.0), 60000000.0, -1),
            Law(23, "Housing Act", "Affordable housing program", LawType.SOCIAL,
                "Government", mapOf("happiness" to 20.0, "treasury" to -60000000.0, "stability" to 10.0), 80000000.0, -1),
            Law(24, "Energy Independence Act", "Reduce foreign energy dependence", LawType.ECONOMIC,
                "Government", mapOf("environment" to 10.0, "treasury" to -50000000.0, "gdp" to 30000000.0), 100000000.0, -1)
        )
    }
    
    fun startProject(project: GovernmentProject, country: Country): Boolean {
        if (country.treasury < project.cost) {
            return false
        }
        
        val ministry = ministries.find { it.id == project.ministry }
        ministry?.projects?.add(project)
        
        country.treasury -= project.cost
        project.funding = project.cost
        
        return true
    }
    
    fun processMinistryTurn() {
        ministries.forEach { ministry ->
            // Process projects
            ministry.projects.removeAll { project ->
                if (!project.isCompleted && !project.isCancelled) {
                    project.progress++
                    if (project.progress >= project.duration) {
                        project.isCompleted = true
                        false
                    } else {
                        false
                    }
                } else {
                    false
                }
            }
            
            // Ministry efficiency changes
            val efficiencyChange = when {
                ministry.budget > 50000000 -> 2.0
                ministry.budget > 30000000 -> 1.0
                ministry.budget < 10000000 -> -2.0
                else -> 0.0
            }
            
            ministry.efficiency = (ministry.efficiency + efficiencyChange).coerceIn(30.0, 100.0)
        }
    }
    
    fun processElection(country: Country) {
        // Calculate election results based on approval and party popularity
        parties.forEach { party ->
            val rulingBonus = if (party.id in rulingCoalition) approvalRatings.presidentApproval / 10 else 0.0
            val ideologyBonus = when {
                country.happiness > 60 && party.ideology == PoliticalIdeology.CENTRIST -> 5.0
                country.happiness < 40 && party.ideology == PoliticalIdeology.SOCIALIST -> 5.0
                country.gdp > 1500000000 && party.ideology == PoliticalIdeology.LIBERAL -> 5.0
                country.environment < 40 && party.ideology == PoliticalIdeology.GREEN -> 5.0
                country.military < 40 && party.ideology == PoliticalIdeology.NATIONALIST -> 5.0
                else -> 0.0
            }
            
            party.popularity = (party.baseVoters + rulingBonus + ideologyBonus + Math.random() * 20).coerceIn(5.0, 60.0)
        }
        
        // Distribute seats based on popularity
        val totalPopularity = parties.sumOf { it.popularity }
        parties.forEach { party ->
            party.seats = ((party.popularity / totalPopularity) * totalSeats).toInt()
        }
        
        // Ensure total seats
        val seatDiff = totalSeats - parties.sumOf { it.seats }
        if (seatDiff > 0) {
            parties.maxByOrNull { it.popularity }?.seats = parties.maxByOrNull { it.popularity }!!.seats + seatDiff
        }
        
        // Update ruling coalition if needed
        val rulingSeats = rulingCoalition.sumOf { parties.find { p -> p.id == it }?.seats ?: 0 }
        if (rulingSeats < simpleMajority) {
            // Form new coalition
            val sortedParties = parties.sortedByDescending { it.seats }
            rulingCoalition = listOf(sortedParties[0].id)
            oppositionParties = parties.filter { it.id !in rulingCoalition }.map { it.id }
        }
        
        nextElectionTurns = 20
    }
    
    fun getPoliticalSummary(): String {
        val summary = StringBuilder()
        summary.append("=== POLITICAL SUMMARY ===\n\n")
        
        summary.append("Government Approval: ${approvalRatings.presidentApproval.toInt()}%\n")
        summary.append("Congress Approval: ${approvalRatings.congressApproval.toInt()}%\n")
        summary.append("Next Election: Turn $nextElectionTurns\n\n")
        
        summary.append("Parliament Composition:\n")
        parties.sortedByDescending { it.seats }.forEach { party ->
            val percentage = party.seats * 100.0 / totalSeats
            summary.append("${party.name}: ${party.seats} seats ($percentage%)\n")
        }
        
        val rulingSeats = rulingCoalition.sumOf { parties.find { p -> p.id == it }?.seats ?: 0 }
        summary.append("\nRuling Coalition: $rulingSeats / $totalSeats seats\n")
        
        summary.append("\nActive Laws: ${laws.count { it.status == LawStatus.ACTIVE }}\n")
        summary.append("Pending Laws: ${laws.count { it.status == LawStatus.IN_PARLIAMENT || it.status == LawStatus.IN_COMMITTEE }}\n")
        
        return summary.toString()
    }
}
