package com.example.myapplication6

import java.io.Serializable

/**
 * Game Advancements and Achievements System v2.0
 * Tracks player progress and unlocks bonuses
 */

// Advancement categories
enum class AdvancementCategory {
    ECONOMIC,
    POLITICAL,
    TECHNOLOGICAL,
    MILITARY,
    SOCIAL,
    DIPLOMATIC,
    REGIONAL,
    SPECIAL
}

// Advancement tier
enum class AdvancementTier {
    BRONZE,    // Easy
    SILVER,    // Medium
    GOLD,      // Hard
    DIAMOND,   // Very Hard
    LEGENDARY  // Extremely Rare
}

// Advancement data
data class Advancement(
    val id: Int,
    val name: String,
    val description: String,
    val category: AdvancementCategory,
    val tier: AdvancementTier,
    val requirement: (Country) -> Boolean,
    val reward: (Country) -> Unit,
    val rewardDescription: String,
    var isUnlocked: Boolean = false,
    var progress: Int = 0,
    val maxProgress: Int = 1
) : Serializable

// Statistics tracker
data class GameStatistics(
    var totalTurns: Int = 0,
    var totalGdpEarned: Double = 0.0,
    var totalTreasurySpent: Double = 0.0,
    var lawsPassed: Int = 0,
    var technologiesResearched: Int = 0,
    var regionsUnlocked: Int = 0,
    var warsWon: Int = 0,
    var diplomaticDealsSigned: Int = 0,
    var populationGrowth: Int = 0,
    var highestGdp: Double = 0.0,
    var highestStability: Int = 0,
    var highestHappiness: Int = 0,
    var consecutiveTurnsPositive: Int = 0,
    var crisesSurvived: Int = 0,
    var electionsWon: Int = 0,
    var factionsControlled: Int = 0,
    var npcRelationshipsMaxed: Int = 0,
    var perfectTurns: Int = 0,
    var totalRevenue: Double = 0.0,
    var totalExpenses: Double = 0.0,
    var researchInvestment: Double = 0.0,
    var militarySpending: Double = 0.0,
    var foreignAidGiven: Double = 0.0,
    var infrastructureBuilt: Int = 0,
    var jobsCreated: Int = 0,
    var companiesFounded: Int = 0,
    var patentsFiled: Int = 0,
    var tradeBalanceTotal: Double = 0.0,
    var debtPaidOff: Double = 0.0,
    var environmentalProjects: Int = 0,
    var socialProgramsLaunched: Int = 0
) : Serializable

// Milestone rewards
data class Milestone(
    val id: Int,
    val name: String,
    val threshold: Int,
    val type: MilestoneType,
    val reward: String,
    var isClaimed: Boolean = false
) : Serializable

enum class MilestoneType {
    TURNS,
    GDP,
    TREASURY,
    POPULATION,
    TECHNOLOGIES,
    LAWS,
    REGIONS
}

// Object for advancement tracking
object AdvancementManager : Serializable {
    
    val advancements = mutableListOf<Advancement>()
    val statistics = GameStatistics()
    val milestones = mutableListOf<Milestone>()
    var totalAdvancementPoints: Int = 0
    
    fun initializeAdvancements() {
        advancements.clear()
        initializeAllAdvancements()
        initializeMilestones()
    }
    
    private fun initializeAllAdvancements() {
        // === ECONOMIC ADVANCEMENTS (12) ===
        
        // Bronze
        advancements.add(Advancement(1, "First Million", "Reach $1M GDP", 
            AdvancementCategory.ECONOMIC, AdvancementTier.BRONZE,
            { country -> country.gdp >= 1000000 },
            { country -> country.treasury += 500000 },
            "+$500K Treasury"))
        
        advancements.add(Advancement(2, "Balanced Budget", "Have positive treasury for 5 turns",
            AdvancementCategory.ECONOMIC, AdvancementTier.BRONZE,
            { country -> country.treasury > 100000000 },
            { country -> country.treasury += 1000000 },
            "+$1M Treasury"))
        
        advancements.add(Advancement(3, "Job Creator", "Reach 500K employed",
            AdvancementCategory.ECONOMIC, AdvancementTier.BRONZE,
            { country -> EconomyManager.getTotalEmployment() >= 500000 },
            { country -> country.happiness = (country.happiness + 5).coerceIn(0, 100) },
            "+5 Happiness"))
        
        // Silver
        advancements.add(Advancement(4, "Economic Miracle", "Reach $1B GDP",
            AdvancementCategory.ECONOMIC, AdvancementTier.SILVER,
            { country -> country.gdp >= 1000000000 },
            { country -> country.gdp = (country.gdp * 1.1).toLong() },
            "+10% GDP"))
        
        advancements.add(Advancement(5, "Trade Master", "Maintain positive trade balance for 10 turns",
            AdvancementCategory.ECONOMIC, AdvancementTier.SILVER,
            { country -> EconomyManager.indicators.tradeBalance > 10000000 },
            { country -> country.treasury += 10000000 },
            "+$10M Treasury"))
        
        advancements.add(Advancement(6, "Industrial Revolution", "Unlock all industries",
            AdvancementCategory.ECONOMIC, AdvancementTier.SILVER,
            { country -> EconomyManager.industries.all { it.level >= 5 } },
            { country -> EconomyManager.industries.forEach { it.efficiency = (it.efficiency + 0.2).coerceIn(0.5, 2.0) } },
            "+20% Industry Efficiency"))
        
        // Gold
        advancements.add(Advancement(7, "Trillionaire Nation", "Reach $1T GDP",
            AdvancementCategory.ECONOMIC, AdvancementTier.GOLD,
            { country -> country.gdp >= 1000000000000 },
            { country -> country.treasury += 100000000 },
            "+$100M Treasury"))
        
        advancements.add(Advancement(8, "Economic Superpower", "Have $500M treasury surplus",
            AdvancementCategory.ECONOMIC, AdvancementTier.GOLD,
            { country -> country.treasury >= 500000000 },
            { country -> country.creditRating = "AAA" },
            "Best Credit Rating"))
        
        // Diamond
        advancements.add(Advancement(9, "Post-Scarcity", "Reach $10T GDP",
            AdvancementCategory.ECONOMIC, AdvancementTier.DIAMOND,
            { country -> country.gdp >= 10000000000000 },
            { country -> country.treasury += 500000000 },
            "+$500M Treasury"))
        
        // Legendary
        advancements.add(Advancement(10, "Economic God", "Reach $100T GDP",
            AdvancementCategory.ECONOMIC, AdvancementTier.LEGENDARY,
            { country -> country.gdp >= 100000000000000 },
            { country -> 
                country.treasury += 1000000000
                country.gdp = (country.gdp * 1.5).toLong()
            },
            "+$1B Treasury, +50% GDP"))
        
        // === POLITICAL ADVANCEMENTS (10) ===
        
        advancements.add(Advancement(11, "First Law", "Pass your first law",
            AdvancementCategory.POLITICAL, AdvancementTier.BRONZE,
            { country -> PolicyManager.laws.any { it.status == LawStatus.ACTIVE } },
            { country -> country.stability = (country.stability + 3).coerceIn(0, 100) },
            "+3 Stability"))
        
        advancements.add(Advancement(12, "Legislator", "Pass 10 laws",
            AdvancementCategory.POLITICAL, AdvancementTier.SILVER,
            { country -> PolicyManager.laws.count { it.status == LawStatus.ACTIVE } >= 10 },
            { country -> country.stability = (country.stability + 10).coerceIn(0, 100) },
            "+10 Stability"))
        
        advancements.add(Advancement(13, "Lawmaker Supreme", "Pass 25 laws",
            AdvancementCategory.POLITICAL, AdvancementTier.GOLD,
            { country -> PolicyManager.laws.count { it.status == LawStatus.ACTIVE } >= 25 },
            { country -> country.stability = (country.stability + 15).coerceIn(0, 100) },
            "+15 Stability"))
        
        advancements.add(Advancement(14, "Election Victory", "Win an election",
            AdvancementCategory.POLITICAL, AdvancementTier.SILVER,
            { country -> PolicyManager.rulingCoalition.sumOf { PolicyManager.parties.find { p -> p.id == it }?.seats ?: 0 } >= 51 },
            { country -> country.happiness = (country.happiness + 10).coerceIn(0, 100) },
            "+10 Happiness"))
        
        advancements.add(Advancement(15, "Mandate of Heaven", "Win 3 elections",
            AdvancementCategory.POLITICAL, AdvancementTier.GOLD,
            { country -> PolicyManager.approvalRatings.presidentApproval >= 70 },
            { country -> country.stability = (country.stability + 20).coerceIn(0, 100) },
            "+20 Stability"))
        
        advancements.add(Advancement(16, "Constitutional Reform", "Pass constitutional law",
            AdvancementCategory.POLITICAL, AdvancementTier.DIAMOND,
            { country -> PolicyManager.laws.any { it.lawType == LawType.CONSTITUTIONAL && it.status == LawStatus.ACTIVE } },
            { country -> 
                country.stability = (country.stability + 25).coerceIn(0, 100)
                country.happiness = (country.happiness + 15).coerceIn(0, 100)
            },
            "+25 Stability, +15 Happiness"))
        
        advancements.add(Advancement(17, "Party Leader", "Control 60+ seats",
            AdvancementCategory.POLITICAL, AdvancementTier.SILVER,
            { country -> PolicyManager.parties.maxByOrNull { it.seats }?.seats ?: 0 >= 60 },
            { country -> country.stability = (country.stability + 5).coerceIn(0, 100) },
            "+5 Stability"))
        
        advancements.add(Advancement(18, "Political Master", "Control 80+ seats",
            AdvancementCategory.POLITICAL, AdvancementTier.GOLD,
            { country -> PolicyManager.parties.maxByOrNull { it.seats }?.seats ?: 0 >= 80 },
            { country -> country.stability = (country.stability + 15).coerceIn(0, 100) },
            "+15 Stability"))
        
        advancements.add(Advancement(19, "Dictator", "Control 90+ seats",
            AdvancementCategory.POLITICAL, AdvancementTier.DIAMOND,
            { country -> PolicyManager.parties.maxByOrNull { it.seats }?.seats ?: 0 >= 90 },
            { country -> 
                country.stability = (country.stability + 20).coerceIn(0, 100)
                country.happiness = (country.happiness - 10).coerceIn(0, 100)
            },
            "+20 Stability, -10 Happiness"))
        
        advancements.add(Advancement(20, "Utopia", "Perfect approval ratings",
            AdvancementCategory.POLITICAL, AdvancementTier.LEGENDARY,
            { country -> PolicyManager.approvalRatings.presidentApproval >= 95 },
            { country -> 
                country.stability = 100
                country.happiness = 100
            },
            "Max Stability & Happiness"))
        
        // === TECHNOLOGICAL ADVANCEMENTS (12) ===
        
        advancements.add(Advancement(21, "First Discovery", "Research first technology",
            AdvancementCategory.TECHNOLOGICAL, AdvancementTier.BRONZE,
            { country -> TechnologyManager.technologies.any { it.isResearched } },
            { country -> country.education = (country.education + 5).coerceIn(0, 100) },
            "+5 Education"))
        
        advancements.add(Advancement(22, "Innovator", "Research 5 technologies",
            AdvancementCategory.TECHNOLOGICAL, AdvancementTier.SILVER,
            { country -> TechnologyManager.technologies.count { it.isResearched } >= 5 },
            { country -> country.education = (country.education + 10).coerceIn(0, 100) },
            "+10 Education"))
        
        advancements.add(Advancement(23, "Scientist", "Research 10 technologies",
            AdvancementCategory.TECHNOLOGICAL, AdvancementTier.SILVER,
            { country -> TechnologyManager.technologies.count { it.isResearched } >= 10 },
            { country -> country.education = (country.education + 15).coerceIn(0, 100) },
            "+15 Education"))
        
        advancements.add(Advancement(24, "Tech Giant", "Research 20 technologies",
            AdvancementCategory.TECHNOLOGICAL, AdvancementTier.GOLD,
            { country -> TechnologyManager.technologies.count { it.isResearched } >= 20 },
            { country -> country.gdp = (country.gdp * 1.2).toLong() },
            "+20% GDP"))
        
        advancements.add(Advancement(25, "Singularity", "Research all technologies",
            AdvancementCategory.TECHNOLOGICAL, AdvancementTier.LEGENDARY,
            { country -> TechnologyManager.technologies.all { it.isResearched } },
            { country -> 
                country.gdp = (country.gdp * 2).toLong()
                country.education = 100
            },
            "2x GDP, Max Education"))
        
        advancements.add(Advancement(26, "Space Age", "Research space technology",
            AdvancementCategory.TECHNOLOGICAL, AdvancementTier.GOLD,
            { country -> TechnologyManager.technologies.any { it.category == TechCategory.SPACE && it.isResearched } },
            { country -> country.internationalRelations = (country.internationalRelations + 20).coerceIn(0, 100) },
            "+20 Int'l Relations"))
        
        advancements.add(Advancement(27, "AI Revolution", "Research AI technology",
            AdvancementCategory.TECHNOLOGICAL, AdvancementTier.GOLD,
            { country -> TechnologyManager.technologies.any { it.name.contains("AI") && it.isResearched } },
            { country -> country.gdp = (country.gdp * 1.3).toLong() },
            "+30% GDP"))
        
        advancements.add(Advancement(28, "Green Future", "Research renewable energy",
            AdvancementCategory.TECHNOLOGICAL, AdvancementTier.SILVER,
            { country -> TechnologyManager.technologies.any { it.name.contains("Renewable") && it.isResearched } },
            { country -> 
                country.environment = (country.environment + 20).coerceIn(0, 100)
                country.gdp = (country.gdp * 1.1).toLong()
            },
            "+20 Environment, +10% GDP"))
        
        advancements.add(Advancement(29, "Biotech Pioneer", "Research biotechnology",
            AdvancementCategory.TECHNOLOGICAL, AdvancementTier.GOLD,
            { country -> TechnologyManager.technologies.any { it.category == TechCategory.BIOTECH && it.isResearched } },
            { country -> 
                country.healthcare = (country.healthcare + 25).coerceIn(0, 100)
                country.happiness = (country.happiness + 10).coerceIn(0, 100)
            },
            "+25 Healthcare, +10 Happiness"))
        
        advancements.add(Advancement(30, "Quantum Leap", "Research quantum computing",
            AdvancementCategory.TECHNOLOGICAL, AdvancementTier.DIAMOND,
            { country -> TechnologyManager.technologies.any { it.name.contains("Quantum") && it.isResearched } },
            { country -> country.education = (country.education + 30).coerceIn(0, 100) },
            "+30 Education"))
        
        advancements.add(Advancement(31, "Fusion Power", "Research fusion energy",
            AdvancementCategory.TECHNOLOGICAL, AdvancementTier.DIAMOND,
            { country -> TechnologyManager.technologies.any { it.name.contains("Fusion") && it.isResearched } },
            { country -> 
                country.gdp = (country.gdp * 1.5).toLong()
                country.environment = (country.environment + 30).coerceIn(0, 100)
            },
            "+50% GDP, +30 Environment"))
        
        advancements.add(Advancement(32, "Immortality", "Research mind uploading",
            AdvancementCategory.TECHNOLOGICAL, AdvancementTier.LEGENDARY,
            { country -> TechnologyManager.technologies.any { it.name.contains("Mind") && it.isResearched } },
            { country -> 
                country.happiness = 100
                country.healthcare = 100
            },
            "Max Happiness & Healthcare"))
        
        // === MILITARY ADVANCEMENTS (10) ===
        
        advancements.add(Advancement(33, "First Recruitment", "Reach 50% military",
            AdvancementCategory.MILITARY, AdvancementTier.BRONZE,
            { country -> country.military >= 50 },
            { country -> country.stability = (country.stability + 5).coerceIn(0, 100) },
            "+5 Stability"))
        
        advancements.add(Advancement(34, "Strong Defense", "Reach 70% military",
            AdvancementCategory.MILITARY, AdvancementTier.SILVER,
            { country -> country.military >= 70 },
            { country -> country.stability = (country.stability + 10).coerceIn(0, 100) },
            "+10 Stability"))
        
        advancements.add(Advancement(35, "Military Superpower", "Reach 90% military",
            AdvancementCategory.MILITARY, AdvancementTier.GOLD,
            { country -> country.military >= 90 },
            { country -> 
                country.stability = (country.stability + 20).coerceIn(0, 100)
                country.internationalRelations = (country.internationalRelations - 10).coerceIn(0, 100)
            },
            "+20 Stability, -10 Int'l"))
        
        advancements.add(Advancement(36, "Warmaster", "Reach 100% military",
            AdvancementCategory.MILITARY, AdvancementTier.DIAMOND,
            { country -> country.military >= 100 },
            { country -> 
                country.stability = 100
                country.internationalRelations = (country.internationalRelations - 20).coerceIn(0, 100)
            },
            "Max Stability, -20 Int'l"))
        
        advancements.add(Advancement(37, "Peacekeeper", "Maintain high military without conflict",
            AdvancementCategory.MILITARY, AdvancementTier.SILVER,
            { country -> country.military >= 60 && country.stability >= 70 },
            { country -> country.happiness = (country.happiness + 10).coerceIn(0, 100) },
            "+10 Happiness"))
        
        advancements.add(Advancement(38, "Nuclear Deterrent", "Research nuclear technology",
            AdvancementCategory.MILITARY, AdvancementTier.GOLD,
            { country -> TechnologyManager.technologies.any { it.name.contains("Nuclear") && it.isResearched } },
            { country -> country.military = (country.military + 20).coerceIn(0, 100) },
            "+20 Military"))
        
        advancements.add(Advancement(39, "Hypersonic Arsenal", "Research hypersonic weapons",
            AdvancementCategory.MILITARY, AdvancementTier.DIAMOND,
            { country -> TechnologyManager.technologies.any { it.name.contains("Hypersonic") && it.isResearched } },
            { country -> country.military = (country.military + 25).coerceIn(0, 100) },
            "+25 Military"))
        
        advancements.add(Advancement(40, "Missile Shield", "Research defense systems",
            AdvancementCategory.MILITARY, AdvancementTier.DIAMOND,
            { country -> TechnologyManager.technologies.any { it.name.contains("Shield") && it.isResearched } },
            { country -> 
                country.military = (country.military + 20).coerceIn(0, 100)
                country.stability = (country.stability + 15).coerceIn(0, 100)
            },
            "+20 Military, +15 Stability"))
        
        advancements.add(Advancement(41, "Demilitarization", "Reduce military to 10% with high happiness",
            AdvancementCategory.MILITARY, AdvancementTier.GOLD,
            { country -> country.military <= 10 && country.happiness >= 80 },
            { country -> 
                country.happiness = (country.happiness + 15).coerceIn(0, 100)
                country.treasury += 50000000
            },
            "+15 Happiness, +$50M Treasury"))
        
        advancements.add(Advancement(42, "World Police", "Have highest military with global alliances",
            AdvancementCategory.MILITARY, AdvancementTier.LEGENDARY,
            { country -> country.military >= 95 && country.internationalRelations >= 70 },
            { country -> 
                country.stability = 100
                country.treasury += 200000000
            },
            "Max Stability, +$200M Treasury"))
        
        // === SOCIAL ADVANCEMENTS (11) ===
        
        advancements.add(Advancement(43, "Happy Nation", "Reach 70% happiness",
            AdvancementCategory.SOCIAL, AdvancementTier.BRONZE,
            { country -> country.happiness >= 70 },
            { country -> country.stability = (country.stability + 5).coerceIn(0, 100) },
            "+5 Stability"))
        
        advancements.add(Advancement(44, "Thriving Society", "Reach 80% happiness",
            AdvancementCategory.SOCIAL, AdvancementTier.SILVER,
            { country -> country.happiness >= 80 },
            { country -> country.happiness = (country.happiness + 5).coerceIn(0, 100) },
            "+5 Happiness"))
        
        advancements.add(Advancement(45, "Paradise", "Reach 90% happiness",
            AdvancementCategory.SOCIAL, AdvancementTier.GOLD,
            { country -> country.happiness >= 90 },
            { country -> country.happiness = (country.happiness + 5).coerceIn(0, 100) },
            "+5 Happiness"))
        
        advancements.add(Advancement(46, "Utopian Society", "Reach 100% happiness",
            AdvancementCategory.SOCIAL, AdvancementTier.LEGENDARY,
            { country -> country.happiness >= 100 },
            { country -> 
                country.stability = 100
                country.gdp = (country.gdp * 1.2).toLong()
            },
            "Max Stability, +20% GDP"))
        
        advancements.add(Advancement(47, "Education First", "Reach 70% education",
            AdvancementCategory.SOCIAL, AdvancementTier.SILVER,
            { country -> country.education >= 70 },
            { country -> country.gdp = (country.gdp * 1.1).toLong() },
            "+10% GDP"))
        
        advancements.add(Advancement(48, "Knowledge Economy", "Reach 90% education",
            AdvancementCategory.SOCIAL, AdvancementTier.GOLD,
            { country -> country.education >= 90 },
            { country -> country.gdp = (country.gdp * 1.25).toLong() },
            "+25% GDP"))
        
        advancements.add(Advancement(49, "Enlightenment", "Reach 100% education",
            AdvancementCategory.SOCIAL, AdvancementTier.DIAMOND,
            { country -> country.education >= 100 },
            { country -> 
                country.gdp = (country.gdp * 1.5).toLong()
                TechnologyManager.scientificPrestige = 100.0
            },
            "+50% GDP, Max Prestige"))
        
        advancements.add(Advancement(50, "Healthy Population", "Reach 70% healthcare",
            AdvancementCategory.SOCIAL, AdvancementTier.SILVER,
            { country -> country.healthcare >= 70 },
            { country -> 
                country.happiness = (country.happiness + 10).coerceIn(0, 100)
                country.population = (country.population * 1.1).toLong()
            },
            "+10 Happiness, +10% Population"))
        
        advancements.add(Advancement(51, "Universal Healthcare", "Reach 90% healthcare",
            AdvancementCategory.SOCIAL, AdvancementTier.GOLD,
            { country -> country.healthcare >= 90 },
            { country -> 
                country.happiness = (country.happiness + 15).coerceIn(0, 100)
                country.population = (country.population * 1.2).toLong()
            },
            "+15 Happiness, +20% Population"))
        
        advancements.add(Advancement(52, "Green Nation", "Reach 80% environment",
            AdvancementCategory.SOCIAL, AdvancementTier.SILVER,
            { country -> country.environment >= 80 },
            { country -> 
                country.happiness = (country.happiness + 10).coerceIn(0, 100)
                country.gdp = (country.gdp * 1.1).toLong()
            },
            "+10 Happiness, +10% GDP"))
        
        advancements.add(Advancement(53, "Carbon Neutral", "Reach 100% environment",
            AdvancementCategory.SOCIAL, AdvancementTier.LEGENDARY,
            { country -> country.environment >= 100 },
            { country -> 
                country.happiness = (country.happiness + 20).coerceIn(0, 100)
                country.gdp = (country.gdp * 1.3).toLong()
                country.environment = 100
            },
            "+20 Happiness, +30% GDP, Max Environment"))
        
        // === DIPLOMATIC ADVANCEMENTS (10) ===
        
        advancements.add(Advancement(54, "First Ally", "Reach 80 relations with a nation",
            AdvancementCategory.DIPLOMATIC, AdvancementTier.BRONZE,
            { country -> GameWorld.diplomaticRelations.any { it.value >= 80 } },
            { country -> country.treasury += 5000000 },
            "+$5M Treasury"))
        
        advancements.add(Advancement(55, "Good Neighbor", "Have 60+ relations with all nations",
            AdvancementCategory.DIPLOMATIC, AdvancementTier.SILVER,
            { country -> GameWorld.diplomaticRelations.all { it.value >= 60 } },
            { country -> country.treasury += 20000000 },
            "+$20M Treasury"))
        
        advancements.add(Advancement(56, "Global Leader", "Have 80+ relations with all nations",
            AdvancementCategory.DIPLOMATIC, AdvancementTier.GOLD,
            { country -> GameWorld.diplomaticRelations.all { it.value >= 80 } },
            { country -> 
                country.internationalRelations = (country.internationalRelations + 20).coerceIn(0, 100)
                country.gdp = (country.gdp * 1.15).toLong()
            },
            "+20 Int'l, +15% GDP"))
        
        advancements.add(Advancement(57, "World Government", "Have 100 relations with all nations",
            AdvancementCategory.DIPLOMATIC, AdvancementTier.LEGENDARY,
            { country -> GameWorld.diplomaticRelations.all { it.value >= 100 } },
            { country -> 
                country.internationalRelations = 100
                country.gdp = (country.gdp * 1.5).toLong()
            },
            "Max Int'l, +50% GDP"))
        
        advancements.add(Advancement(58, "Trade Empire", "Sign 10 trade deals",
            AdvancementCategory.DIPLOMATIC, AdvancementTier.SILVER,
            { country -> statistics.diplomaticDealsSigned >= 10 },
            { country -> country.gdp = (country.gdp * 1.2).toLong() },
            "+20% GDP"))
        
        advancements.add(Advancement(59, "Philanthropist", "Give $100M in foreign aid",
            AdvancementCategory.DIPLOMATIC, AdvancementTier.GOLD,
            { country -> statistics.foreignAidGiven >= 100000000 },
            { country -> 
                country.internationalRelations = (country.internationalRelations + 30).coerceIn(0, 100)
                country.happiness = (country.happiness + 10).coerceIn(0, 100)
            },
            "+30 Int'l, +10 Happiness"))
        
        advancements.add(Advancement(60, "Soft Power", "High international relations without military",
            AdvancementCategory.DIPLOMATIC, AdvancementTier.SILVER,
            { country -> country.internationalRelations >= 70 && country.military <= 40 },
            { country -> country.treasury += 30000000 },
            "+$30M Treasury"))
        
        advancements.add(Advancement(61, "Hard Power", "High military without alliances",
            AdvancementCategory.DIPLOMATIC, AdvancementTier.SILVER,
            { country -> country.military >= 80 && country.internationalRelations <= 40 },
            { country -> country.stability = (country.stability + 15).coerceIn(0, 100) },
            "+15 Stability"))
        
        advancements.add(Advancement(62, "Balanced Power", "High military and high relations",
            AdvancementCategory.DIPLOMATIC, AdvancementTier.DIAMOND,
            { country -> country.military >= 70 && country.internationalRelations >= 70 },
            { country -> 
                country.stability = (country.stability + 20).coerceIn(0, 100)
                country.gdp = (country.gdp * 1.2).toLong()
            },
            "+20 Stability, +20% GDP"))
        
        advancements.add(Advancement(63, "United Nations", "Host international summit successfully",
            AdvancementCategory.DIPLOMATIC, AdvancementTier.GOLD,
            { country -> country.internationalRelations >= 85 && country.treasury >= 100000000 },
            { country -> 
                country.internationalRelations = (country.internationalRelations + 25).coerceIn(0, 100)
                country.treasury += 50000000
            },
            "+25 Int'l, +$50M Treasury"))
        
        // === REGIONAL ADVANCEMENTS (10) ===
        
        advancements.add(Advancement(64, "Capital Developer", "Max capital development",
            AdvancementCategory.REGIONAL, AdvancementTier.SILVER,
            { country -> GameWorld.getRegionById(0)?.development ?: 0 >= 90 },
            { country -> country.gdp = (country.gdp * 1.1).toLong() },
            "+10% GDP"))
        
        advancements.add(Advancement(65, "Industrial Powerhouse", "Max industrial region",
            AdvancementCategory.REGIONAL, AdvancementTier.SILVER,
            { country -> GameWorld.getRegionById(1)?.development ?: 0 >= 90 },
            { country -> country.gdp = (country.gdp * 1.15).toLong() },
            "+15% GDP"))
        
        advancements.add(Advancement(66, "Breadbasket", "Max agricultural region",
            AdvancementCategory.REGIONAL, AdvancementTier.SILVER,
            { country -> GameWorld.getRegionById(2)?.development ?: 0 >= 90 },
            { country -> 
                country.gdp = (country.gdp * 1.1).toLong()
                country.happiness = (country.happiness + 5).coerceIn(0, 100)
            },
            "+10% GDP, +5 Happiness"))
        
        advancements.add(Advancement(67, "Trade Hub", "Max coastal region",
            AdvancementCategory.REGIONAL, AdvancementTier.SILVER,
            { country -> GameWorld.getRegionById(3)?.development ?: 0 >= 90 },
            { country -> 
                country.gdp = (country.gdp * 1.15).toLong()
                country.internationalRelations = (country.internationalRelations + 10).coerceIn(0, 100)
            },
            "+15% GDP, +10 Int'l"))
        
        advancements.add(Advancement(68, "Resource King", "Max northern region",
            AdvancementCategory.REGIONAL, AdvancementTier.GOLD,
            { country -> GameWorld.getRegionById(4)?.development ?: 0 >= 90 },
            { country -> 
                country.gdp = (country.gdp * 1.2).toLong()
                country.military = (country.military + 10).coerceIn(0, 100)
            },
            "+20% GDP, +10 Military"))
        
        advancements.add(Advancement(69, "Secure Border", "Max border region",
            AdvancementCategory.REGIONAL, AdvancementTier.SILVER,
            { country -> GameWorld.getRegionById(5)?.development ?: 0 >= 90 },
            { country -> 
                country.stability = (country.stability + 15).coerceIn(0, 100)
                country.military = (country.military + 5).coerceIn(0, 100)
            },
            "+15 Stability, +5 Military"))
        
        advancements.add(Advancement(70, "Regional Developer", "Unlock all regions",
            AdvancementCategory.REGIONAL, AdvancementTier.SILVER,
            { country -> GameWorld.getUnlockedRegions().size >= 6 },
            { country -> country.gdp = (country.gdp * 1.15).toLong() },
            "+15% GDP"))
        
        advancements.add(Advancement(71, "National Unity", "All regions 80+ loyalty",
            AdvancementCategory.REGIONAL, AdvancementTier.GOLD,
            { country -> GameWorld.getAllRegions().all { it.loyalty >= 80 } },
            { country -> 
                country.stability = (country.stability + 20).coerceIn(0, 100)
                country.happiness = (country.happiness + 10).coerceIn(0, 100)
            },
            "+20 Stability, +10 Happiness"))
        
        advancements.add(Advancement(72, "Federal Master", "All regions 100 development",
            AdvancementCategory.REGIONAL, AdvancementTier.DIAMOND,
            { country -> GameWorld.getAllRegions().all { it.development >= 100 } },
            { country -> 
                country.gdp = (country.gdp * 1.5).toLong()
                country.stability = 100
            },
            "+50% GDP, Max Stability"))
        
        advancements.add(Advancement(73, "Balanced Development", "All regions equal development",
            AdvancementCategory.REGIONAL, AdvancementTier.GOLD,
            { country -> 
                val devs = GameWorld.getAllRegions().map { it.development }
                (devs.max() - devs.min()) <= 10
            },
            { country -> 
                country.happiness = (country.happiness + 15).coerceIn(0, 100)
                country.stability = (country.stability + 10).coerceIn(0, 100)
            },
            "+15 Happiness, +10 Stability"))
        
        // === SPECIAL ADVANCEMENTS (12) ===
        
        advancements.add(Advancement(74, "Survivor", "Survive 50 turns",
            AdvancementCategory.SPECIAL, AdvancementTier.SILVER,
            { country -> country.turn >= 50 },
            { country -> country.treasury += 25000000 },
            "+$25M Treasury"))
        
        advancements.add(Advancement(75, "Veteran Leader", "Survive 100 turns",
            AdvancementCategory.SPECIAL, AdvancementTier.GOLD,
            { country -> country.turn >= 100 },
            { country -> country.treasury += 100000000 },
            "+$100M Treasury"))
        
        advancements.add(Advancement(76, "Century Club", "Survive 200 turns",
            AdvancementCategory.SPECIAL, AdvancementTier.DIAMOND,
            { country -> country.turn >= 200 },
            { country -> country.treasury += 500000000 },
            "+$500M Treasury"))
        
        advancements.add(Advancement(77, "Immortal Leader", "Survive 500 turns",
            AdvancementCategory.SPECIAL, AdvancementTier.LEGENDARY,
            { country -> country.turn >= 500 },
            { country -> 
                country.treasury += 1000000000
                country.stability = 100
                country.happiness = 100
            },
            "+$1B Treasury, Max Stability & Happiness"))
        
        advancements.add(Advancement(78, "Crisis Manager", "Survive economic crisis",
            AdvancementCategory.SPECIAL, AdvancementTier.BRONZE,
            { country -> statistics.crisesSurvived >= 1 },
            { country -> country.stability = (country.stability + 5).coerceIn(0, 100) },
            "+5 Stability"))
        
        advancements.add(Advancement(79, "Phoenix", "Recover from near bankruptcy",
            AdvancementCategory.SPECIAL, AdvancementTier.GOLD,
            { country -> country.treasury < 10000000 && country.treasury > 500000000 },
            { country -> country.treasury += 100000000 },
            "+$100M Treasury"))
        
        advancements.add(Advancement(80, "Comeback Kid", "Recover from low stability",
            AdvancementCategory.SPECIAL, AdvancementTier.SILVER,
            { country -> country.stability < 30 && country.stability > 70 },
            { country -> country.stability = (country.stability + 20).coerceIn(0, 100) },
            "+20 Stability"))
        
        advancements.add(Advancement(81, "Perfect Run", "50 turns without negative events",
            AdvancementCategory.SPECIAL, AdvancementTier.DIAMOND,
            { country -> statistics.perfectTurns >= 50 },
            { country -> 
                country.happiness = (country.happiness + 20).coerceIn(0, 100)
                country.stability = (country.stability + 20).coerceIn(0, 100)
            },
            "+20 Happiness, +20 Stability"))
        
        advancements.add(Advancement(82, "Jack of All Trades", "Have 50+ in all stats",
            AdvancementCategory.SPECIAL, AdvancementTier.SILVER,
            { country -> 
                country.stability >= 50 && country.happiness >= 50 && 
                country.military >= 50 && country.education >= 50 &&
                country.healthcare >= 50 && country.infrastructure >= 50 &&
                country.environment >= 50 && country.internationalRelations >= 50
            },
            { country -> country.treasury += 50000000 },
            "+$50M Treasury"))
        
        advancements.add(Advancement(83, "Master of All", "Have 80+ in all stats",
            AdvancementCategory.SPECIAL, AdvancementTier.DIAMOND,
            { country -> 
                country.stability >= 80 && country.happiness >= 80 && 
                country.military >= 80 && country.education >= 80 &&
                country.healthcare >= 80 && country.infrastructure >= 80 &&
                country.environment >= 80 && country.internationalRelations >= 80
            },
            { country -> 
                country.treasury += 200000000
                country.gdp = (country.gdp * 1.3).toLong()
            },
            "+$200M Treasury, +30% GDP"))
        
        advancements.add(Advancement(84, "Flawless Victory", "Have 100 in all stats",
            AdvancementCategory.SPECIAL, AdvancementTier.LEGENDARY,
            { country -> 
                country.stability >= 100 && country.happiness >= 100 && 
                country.military >= 100 && country.education >= 100 &&
                country.healthcare >= 100 && country.infrastructure >= 100 &&
                country.environment >= 100 && country.internationalRelations >= 100
            },
            { country -> 
                country.treasury += 1000000000
                country.gdp = (country.gdp * 2).toLong()
            },
            "+$1B Treasury, 2x GDP"))
        
        advancements.add(Advancement(85, "True Ending", "Unlock all advancements",
            AdvancementCategory.SPECIAL, AdvancementTier.LEGENDARY,
            { country -> advancements.all { it.isUnlocked } },
            { country -> 
                country.treasury += 5000000000
                country.gdp = (country.gdp * 3).toLong()
                country.stability = 100
                country.happiness = 100
            },
            "+$5B Treasury, 3x GDP, Max Stability & Happiness"))
    }
    
    private fun initializeMilestones() {
        milestones.clear()
        
        // Turn milestones
        milestones.add(Milestone(1, "10 Turns", 10, MilestoneType.TURNS, "+$5M Treasury"))
        milestones.add(Milestone(2, "50 Turns", 50, MilestoneType.TURNS, "+$25M Treasury"))
        milestones.add(Milestone(3, "100 Turns", 100, MilestoneType.TURNS, "+$100M Treasury"))
        milestones.add(Milestone(4, "250 Turns", 250, MilestoneType.TURNS, "+$500M Treasury"))
        
        // GDP milestones
        milestones.add(Milestone(5, "$1B GDP", 1000000000, MilestoneType.GDP, "+5 Happiness"))
        milestones.add(Milestone(6, "$10B GDP", 10000000000, MilestoneType.GDP, "+10 Happiness"))
        milestones.add(Milestone(7, "$100B GDP", 100000000000, MilestoneType.GDP, "+15 Happiness"))
        
        // Technology milestones
        milestones.add(Milestone(8, "5 Technologies", 5, MilestoneType.TECHNOLOGIES, "+5 Education"))
        milestones.add(Milestone(9, "15 Technologies", 15, MilestoneType.TECHNOLOGIES, "+15 Education"))
        milestones.add(Milestone(10, "30 Technologies", 30, MilestoneType.TECHNOLOGIES, "+30 Education"))
    }
    
    fun checkAdvancements(country: Country) {
        advancements.forEach { advancement ->
            if (!advancement.isUnlocked && advancement.requirement(country)) {
                advancement.isUnlocked = true
                advancement.reward(country)
                totalAdvancementPoints += when (advancement.tier) {
                    AdvancementTier.BRONZE -> 10
                    AdvancementTier.SILVER -> 25
                    AdvancementTier.GOLD -> 50
                    AdvancementTier.DIAMOND -> 100
                    AdvancementTier.LEGENDARY -> 250
                }
            }
        }
    }
    
    fun checkMilestones(country: Country): List<String> {
        val earned = mutableListOf<String>()
        
        milestones.forEach { milestone ->
            if (!milestone.isClaimed) {
                val claimed = when (milestone.type) {
                    MilestoneType.TURNS -> country.turn >= milestone.threshold
                    MilestoneType.GDP -> country.gdp >= milestone.threshold
                    MilestoneType.TREASURY -> country.treasury >= milestone.threshold
                    MilestoneType.POPULATION -> country.population >= milestone.threshold
                    MilestoneType.TECHNOLOGIES -> TechnologyManager.technologies.count { it.isResearched } >= milestone.threshold
                    MilestoneType.LAWS -> PolicyManager.laws.count { it.status == LawStatus.ACTIVE } >= milestone.threshold
                    MilestoneType.REGIONS -> GameWorld.getUnlockedRegions().size >= milestone.threshold
                }
                
                if (claimed) {
                    milestone.isClaimed = true
                    earned.add(milestone.reward)
                    applyMilestoneReward(milestone, country)
                }
            }
        }
        
        return earned
    }
    
    private fun applyMilestoneReward(milestone: Milestone, country: Country) {
        when {
            milestone.reward.contains("Treasury") -> {
                val amount = milestone.reward.replace("+", "").replace(" Treasury", "").replace("$", "").replace("M", "000000").toLongOrNull() ?: 0
                country.treasury += amount.toDouble()
            }
            milestone.reward.contains("Happiness") -> {
                val amount = milestone.reward.replace("+", "").replace(" Happiness", "").toIntOrNull() ?: 0
                country.happiness = (country.happiness + amount).coerceIn(0, 100)
            }
            milestone.reward.contains("Education") -> {
                val amount = milestone.reward.replace("+", "").replace(" Education", "").toIntOrNull() ?: 0
                country.education = (country.education + amount).coerceIn(0, 100)
            }
        }
    }
    
    fun getAdvancementProgress(): Pair<Int, Int> {
        return Pair(advancements.count { it.isUnlocked }, advancements.size)
    }
    
    fun getAdvancementsByCategory(category: AdvancementCategory): List<Advancement> {
        return advancements.filter { it.category == category }
    }
    
    fun getUnlockedAdvancements(): List<Advancement> {
        return advancements.filter { it.isUnlocked }
    }
    
    fun getLockedAdvancements(): List<Advancement> {
        return advancements.filter { !it.isUnlocked }
    }
}
