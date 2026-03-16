package com.example.myapplication6

import java.io.Serializable

/**
 * Country Sim v3.0 - Intelligence & Espionage System
 */

// Intelligence agency types
enum class AgencyType {
    DOMESTIC_INTELLIGENCE,
    FOREIGN_INTELLIGENCE,
    MILITARY_INTELLIGENCE,
    CYBER_INTELLIGENCE,
    COUNTER_INTELLIGENCE,
    SPECIAL_OPERATIONS
}

// Intelligence operation
data class IntelOperation(
    val id: Int,
    val name: String,
    val description: String,
    val type: OperationType,
    val target: String,
    val difficulty: Int,
    val cost: Double,
    val duration: Int,
    val successChance: Double,
    val rewards: Map<String, Double>,
    val failureConsequences: Map<String, Double>,
    var progress: Int = 0,
    var isComplete: Boolean = false,
    var isSuccess: Boolean = false
) : Serializable

enum class OperationType {
    SURVEILLANCE,
    ASSASSINATION,
    COUP,
    SABOTAGE,
    CYBER_ATTACK,
    PROPAGANDA,
    RECRUITMENT,
    INTERCEPTION,
    EXTRACTION,
    INFILTRATION
}

// Intelligence asset
data class IntelAsset(
    val id: Int,
    val codename: String,
    val type: AssetType,
    val location: String,
    val loyalty: Int,
    val skill: Int,
    val cover: String,
    val access: Int,
    var isActive: Boolean = true,
    var isCompromised: Boolean = false
) : Serializable

enum class AssetType {
    AGENT,
    INFORMANT,
    HACKER,
    DIPLOMAT,
    DEFECTOR,
    DOUBLE_AGENT
}

// Intelligence report
data class IntelReport(
    val id: Int,
    val title: String,
    val content: String,
    val reliability: Int,
    val urgency: Int,
    val source: String,
    val timestamp: Long
) : Serializable

// Intelligence agency
data class IntelligenceAgency(
    val id: Int,
    val name: String,
    val type: AgencyType,
    val budget: Double,
    val personnel: Int,
    val capability: Int,
    val director: String,
    val headquarters: String,
    var activeOperations: Int = 0,
    val maxOperations: Int = 5
) : Serializable

// Object for intelligence management
object IntelligenceManager : Serializable {
    
    val agencies = mutableListOf<IntelligenceAgency>()
    val activeOperations = mutableListOf<IntelOperation>()
    val completedOperations = mutableListOf<IntelOperation>()
    val assets = mutableListOf<IntelAsset>()
    val intelReports = mutableListOf<IntelReport>()
    
    var globalIntelLevel: Int = 50
    var counterIntelLevel: Int = 50
    var cyberCapability: Int = 50
    var internationalIntelSharing: Int = 30
    var scandals: Int = 0
    var successfulOps: Int = 0
    var failedOps: Int = 0
    
    fun initializeIntelligence() {
        agencies.clear()
        activeOperations.clear()
        assets.clear()
        intelReports.clear()
        
        // Create starting agencies
        agencies.add(IntelligenceAgency(1, "National Security Agency", AgencyType.DOMESTIC_INTELLIGENCE, 
            50000000.0, 10000, 60, "Director Smith", "Capital City"))
        agencies.add(IntelligenceAgency(2, "Foreign Intelligence Service", AgencyType.FOREIGN_INTELLIGENCE,
            75000000.0, 15000, 65, "Director Johnson", "Capital City"))
        agencies.add(IntelligenceAgency(3, "Cyber Command", AgencyType.CYBER_INTELLIGENCE,
            40000000.0, 5000, 55, "Director Chen", "Capital City"))
        
        // Create starting assets
        assets.add(IntelAsset(1, "Eagle", AssetType.AGENT, "Foreign Nation A", 80, 75, "Diplomat", 60))
        assets.add(IntelAsset(2, "Shadow", AssetType.HACKER, "Foreign Nation B", 70, 85, "IT Contractor", 70))
        assets.add(IntelAsset(3, "Mole", AssetType.DOUBLE_AGENT, "Foreign Nation C", 50, 65, "Government Official", 80))
    }
    
    fun startOperation(operation: IntelOperation, country: Country): String {
        // Check cost
        if (country.treasury < operation.cost) {
            return "Insufficient funds!"
        }
        
        // Check agency capacity
        val agency = agencies.find { it.type == getAgencyTypeForOperation(operation.type) }
        if (agency == null || agency.activeOperations >= agency.maxOperations) {
            return "No agency available!"
        }
        
        // Deduct cost
        country.treasury -= operation.cost
        agency.activeOperations++
        
        // Add to active operations
        activeOperations.add(operation)
        
        return "Operation '${operation.name}' initiated!"
    }
    
    private fun getAgencyTypeForOperation(type: OperationType): AgencyType {
        return when (type) {
            OperationType.SURVEILLANCE, OperationType.RECRUITMENT, OperationType.INTERCEPTION -> AgencyType.DOMESTIC_INTELLIGENCE
            OperationType.ASSASSINATION, OperationType.COUP, OperationType.EXTRACTION, OperationType.INFILTRATION -> AgencyType.FOREIGN_INTELLIGENCE
            OperationType.SABOTAGE, OperationType.PROPAGANDA -> AgencyType.MILITARY_INTELLIGENCE
            OperationType.CYBER_ATTACK -> AgencyType.CYBER_INTELLIGENCE
        }
    }
    
    fun processOperations(country: Country): List<String> {
        val results = mutableListOf<String>()
        val opsToRemove = mutableListOf<IntelOperation>()
        
        activeOperations.forEach { op ->
            op.progress++
            
            if (op.progress >= op.duration) {
                // Operation complete
                op.isComplete = true
                
                // Roll for success
                val roll = Math.random() * 100
                op.isSuccess = roll < (op.successChance * (globalIntelLevel / 100.0))
                
                if (op.isSuccess) {
                    // Apply rewards
                    op.rewards.forEach { (stat, value) ->
                        applyIntelReward(stat, value, country)
                    }
                    successfulOps++
                    results.add("✓ ${op.name}: SUCCESS")
                } else {
                    // Apply failure consequences
                    op.failureConsequences.forEach { (stat, value) ->
                        applyIntelConsequence(stat, value, country)
                    }
                    failedOps++
                    results.add("✗ ${op.name}: FAILED")
                    
                    // Chance of asset compromise
                    if (Math.random() < 0.3) {
                        scandals++
                        results.add("⚠ Asset compromised! Scandal erupted.")
                    }
                }
                
                // Free up agency slot
                val agency = agencies.find { it.type == getAgencyTypeForOperation(op.type) }
                agency?.let {
                    it.activeOperations = (it.activeOperations - 1).coerceAtLeast(0)
                }

                opsToRemove.add(op)
                completedOperations.add(op)
            }
        }
        
        activeOperations.removeAll(opsToRemove)
        return results
    }
    
    private fun applyIntelReward(stat: String, value: Double, country: Country) {
        when (stat) {
            "internationalRelations" -> country.internationalRelations = (country.internationalRelations + value.toInt()).coerceIn(0, 100)
            "stability" -> country.stability = (country.stability + value.toInt()).coerceIn(0, 100)
            "treasury" -> country.treasury += value
            "military" -> country.military = (country.military + value.toInt()).coerceIn(0, 100)
            "intel" -> globalIntelLevel = (globalIntelLevel + value.toInt()).coerceIn(0, 100)
            "tech" -> country.education = (country.education + value.toInt()).coerceIn(0, 100)
        }
    }
    
    private fun applyIntelConsequence(stat: String, value: Double, country: Country) {
        when (stat) {
            "internationalRelations" -> country.internationalRelations = (country.internationalRelations + value.toInt()).coerceIn(0, 100)
            "stability" -> country.stability = (country.stability + value.toInt()).coerceIn(0, 100)
            "treasury" -> country.treasury += value
            "happiness" -> country.happiness = (country.happiness + value.toInt()).coerceIn(0, 100)
            "scandal" -> scandals++
        }
    }
    
    fun recruitAsset(codename: String, type: AssetType, location: String, cost: Double, country: Country): Boolean {
        if (country.treasury < cost) return false
        
        country.treasury -= cost
        
        val skill = (50..90).random()
        val loyalty = (40..100).random()
        val access = (30..90).random()
        
        assets.add(IntelAsset(assets.size + 1, codename, type, location, loyalty, skill, "Undercover", access))
        
        return true
    }
    
    fun getAvailableOperations(): List<IntelOperation> {
        return listOf(
            IntelOperation(1, "Operation Wiretap", "Surveil communications of target",
                OperationType.SURVEILLANCE, "Foreign Diplomat", 30, 5000000.0, 3, 0.75,
                mapOf("intel" to 10.0, "internationalRelations" to -5.0),
                mapOf("scandal" to 1.0, "internationalRelations" to -15.0)),
            IntelOperation(2, "Operation Cyber Storm", "Hack into enemy systems",
                OperationType.CYBER_ATTACK, "Foreign Government", 60, 15000000.0, 5, 0.6,
                mapOf("tech" to 15.0, "treasury" to 20000000.0, "military" to 10.0),
                mapOf("internationalRelations" to -30.0, "stability" to -10.0)),
            IntelOperation(3, "Operation Puppet Master", "Influence foreign election",
                OperationType.PROPAGANDA, "Foreign Nation", 70, 25000000.0, 8, 0.5,
                mapOf("internationalRelations" to 20.0, "stability" to 10.0),
                mapOf("scandal" to 1.0, "internationalRelations" to -40.0, "happiness" to -15.0)),
            IntelOperation(4, "Operation Deep Cover", "Infiltrate enemy organization",
                OperationType.INFILTRATION, "Terrorist Group", 80, 10000000.0, 10, 0.45,
                mapOf("intel" to 30.0, "stability" to 15.0, "military" to 10.0),
                mapOf("scandal" to 1.0, "stability" to -20.0)),
            IntelOperation(5, "Operation Extraction", "Extract defector from hostile nation",
                OperationType.EXTRACTION, "Foreign Nation", 50, 8000000.0, 4, 0.65,
                mapOf("intel" to 20.0, "internationalRelations" to -10.0),
                mapOf("internationalRelations" to -25.0, "military" to -5.0)),
            IntelOperation(6, "Operation Sabotage", "Disable enemy infrastructure",
                OperationType.SABOTAGE, "Foreign Military Base", 75, 20000000.0, 6, 0.55,
                mapOf("military" to 20.0, "internationalRelations" to -15.0),
                mapOf("internationalRelations" to -40.0, "military" to -15.0, "stability" to -10.0)),
            IntelOperation(7, "Operation Turncoat", "Recruit double agent",
                OperationType.RECRUITMENT, "Foreign Intelligence", 85, 30000000.0, 12, 0.4,
                mapOf("intel" to 40.0, "internationalRelations" to 10.0),
                mapOf("scandal" to 1.0, "internationalRelations" to -50.0)),
            IntelOperation(8, "Operation Blackout", "Disrupt enemy communications",
                OperationType.CYBER_ATTACK, "Foreign Military", 65, 12000000.0, 4, 0.6,
                mapOf("military" to 15.0, "intel" to 10.0),
                mapOf("internationalRelations" to -30.0, "military" to -10.0))
        )
    }
    
    fun getIntelligenceReport(): String {
        val report = StringBuilder()
        report.append("=== INTELLIGENCE REPORT ===\n\n")
        
        report.append("Global Intel Level: $globalIntelLevel/100\n")
        report.append("Counter-Intel: $counterIntelLevel/100\n")
        report.append("Cyber Capability: $cyberCapability/100\n")
        report.append("Intel Sharing: $internationalIntelSharing/100\n\n")
        
        report.append("Agencies (${agencies.size}):\n")
        agencies.forEach { agency ->
            report.append("- ${agency.name}\n")
            report.append("  Budget: $${String.format("%,d", (agency.budget / 1000000).toLong())}M | ")
            report.append("Personnel: ${agency.personnel} | Capability: ${agency.capability}\n")
            report.append("  Operations: ${agency.activeOperations}/${agency.maxOperations}\n\n")
        }
        
        report.append("Active Assets (${assets.count { it.isActive }}):\n")
        assets.filter { it.isActive }.forEach { asset ->
            report.append("- ${asset.codename} (${asset.type})\n")
            report.append("  Location: ${asset.location} | Loyalty: ${asset.loyalty}% | Skill: ${asset.skill}\n")
            report.append("  Access: ${asset.access}%\n\n")
        }
        
        if (activeOperations.isNotEmpty()) {
            report.append("Active Operations (${activeOperations.size}):\n")
            activeOperations.forEach { op ->
                report.append("- ${op.name} (${op.type})\n")
                report.append("  Progress: ${op.progress}/${op.duration} | Success Chance: ${(op.successChance * 100).toInt()}%\n\n")
            }
        }
        
        report.append("=== STATISTICS ===\n")
        report.append("Successful Ops: $successfulOps\n")
        report.append("Failed Ops: $failedOps\n")
        report.append("Scandals: $scandals\n")
        
        return report.toString()
    }
}
