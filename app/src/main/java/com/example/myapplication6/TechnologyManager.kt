package com.example.myapplication6

import java.io.Serializable

/**
 * Advanced Technology and Research System
 */

// Technology tier
enum class TechTier {
    TIER_1,  // Basic
    TIER_2,  // Intermediate
    TIER_3,  // Advanced
    TIER_4,  // Cutting Edge
    TIER_5   // Futuristic
}

// Technology category
enum class TechCategory {
    MILITARY,
    CIVILIAN,
    MEDICAL,
    INDUSTRIAL,
    AGRICULTURAL,
    ENERGY,
    COMPUTING,
    SPACE,
    TRANSPORTATION,
    BIOTECH,
    MATERIALS,
    ENVIRONMENTAL
}

// Technology data
data class Technology(
    val id: Int,
    val name: String,
    val description: String,
    val category: TechCategory,
    val tier: TechTier,
    val researchCost: Int,
    val prerequisites: List<Int>,
    val effects: Map<String, Double>,
    val unlockFeatures: List<String>,
    var isResearched: Boolean = false,
    var isBeingResearched: Boolean = false,
    var researchProgress: Int = 0
) : Serializable

// Research project
data class ResearchProject(
    val id: Int,
    val name: String,
    val description: String,
    val cost: Double,
    val duration: Int,
    val requirements: Map<String, Double>,
    val rewards: Map<String, Double>,
    var progress: Int = 0,
    var isCompleted: Boolean = false,
    var scientists: Int = 0,
    var funding: Double = 0.0
) : Serializable

// Research facility
data class ResearchFacility(
    val id: Int,
    val name: String,
    val type: FacilityType,
    var level: Int,
    var capacity: Int,
    var efficiency: Double,
    val currentProjects: Int,
    val maxProjects: Int,
    var maintenanceCost: Double
) : Serializable

enum class FacilityType {
    UNIVERSITY,
    GOVERNMENT_LAB,
    PRIVATE_LAB,
    MILITARY_LAB,
    MEDICAL_CENTER,
    INDUSTRIAL_LAB,
    SPACE_CENTER
}

// Scientist
data class Scientist(
    val id: Int,
    val name: String,
    val specialty: TechCategory,
    val skill: Int,
    val assignedProject: Int?,
    val productivity: Double
) : Serializable

// Scientific publication
data class ScientificPublication(
    val id: Int,
    val title: String,
    val field: TechCategory,
    val impact: Double,
    val citations: Int,
    val year: Int
) : Serializable

// Object for technology management
object TechnologyManager : Serializable {
    
    val technologies = mutableListOf<Technology>()
    val researchProjects = mutableListOf<ResearchProject>()
    val facilities = mutableListOf<ResearchFacility>()
    val scientists = mutableListOf<Scientist>()
    val publications = mutableListOf<ScientificPublication>()
    
    var totalResearchBudget: Double = 0.0
    var researchOutput: Double = 1.0
    var knowledgeStock: Double = 0.0
    var internationalCollaboration: Double = 50.0
    var patentCount: Int = 0
    var scientificPrestige: Double = 50.0
    
    fun initializeTechnology() {
        technologies.clear()
        facilities.clear()
        scientists.clear()
        
        initializeTechnologies()
        initializeFacilities()
    }
    
    private fun initializeTechnologies() {
        // TIER 1 - Basic Technologies
        // Military
        technologies.add(Technology(1, "Basic Firearms", "Improved small arms technology", 
            TechCategory.MILITARY, TechTier.TIER_1, 10, emptyList(), 
            mapOf("military" to 5.0), listOf("infantry_upgrade_1")))
        
        technologies.add(Technology(2, "Armored Vehicles", "Basic tank and APC development",
            TechCategory.MILITARY, TechTier.TIER_1, 15, listOf(1),
            mapOf("military" to 8.0, "gdp" to 10000000.0), listOf("armor_upgrade_1")))
        
        // Civilian/Industrial
        technologies.add(Technology(3, "Mass Production", "Assembly line manufacturing",
            TechCategory.INDUSTRIAL, TechTier.TIER_1, 12, emptyList(),
            mapOf("gdp" to 30000000.0, "infrastructure" to 5.0), listOf("factory_efficiency_1")))
        
        technologies.add(Technology(4, "Electricity Grid", "National power distribution",
            TechCategory.ENERGY, TechTier.TIER_1, 20, emptyList(),
            mapOf("infrastructure" to 10.0, "gdp" to 20000000.0), listOf("power_grid")))
        
        // Medical
        technologies.add(Technology(5, "Vaccination Programs", "Disease prevention",
            TechCategory.MEDICAL, TechTier.TIER_1, 8, emptyList(),
            mapOf("healthcare" to 10.0, "happiness" to 5.0), listOf("vaccine_program")))
        
        // Agricultural
        technologies.add(Technology(6, "Modern Farming", "Mechanized agriculture",
            TechCategory.AGRICULTURAL, TechTier.TIER_1, 10, emptyList(),
            mapOf("gdp" to 15000000.0, "environment" to -2.0), listOf("mechanized_farming")))
        
        // Computing
        technologies.add(Technology(7, "Basic Computing", "Early computer systems",
            TechCategory.COMPUTING, TechTier.TIER_1, 15, emptyList(),
            mapOf("education" to 5.0, "gdp" to 10000000.0), listOf("computerization")))
        
        // Transportation
        technologies.add(Technology(8, "Highway System", "National road network",
            TechCategory.TRANSPORTATION, TechTier.TIER_1, 18, emptyList(),
            mapOf("infrastructure" to 15.0, "gdp" to 25000000.0), listOf("highway_network")))
        
        // TIER 2 - Intermediate Technologies
        technologies.add(Technology(9, "Jet Propulsion", "Jet aircraft technology",
            TechCategory.MILITARY, TechTier.TIER_2, 25, listOf(2),
            mapOf("military" to 12.0, "gdp" to 20000000.0), listOf("jet_aircraft")))
        
        technologies.add(Technology(10, "Nuclear Power", "Nuclear energy generation",
            TechCategory.ENERGY, TechTier.TIER_2, 35, listOf(4),
            mapOf("energy" to 20.0, "environment" to 5.0, "gdp" to 30000000.0), listOf("nuclear_plants")))
        
        technologies.add(Technology(11, "Antibiotics", "Advanced medicine production",
            TechCategory.MEDICAL, TechTier.TIER_2, 20, listOf(5),
            mapOf("healthcare" to 15.0, "happiness" to 8.0), listOf("antibiotic_production")))
        
        technologies.add(Technology(12, "Green Revolution", "High-yield crops",
            TechCategory.AGRICULTURAL, TechTier.TIER_2, 22, listOf(6),
            mapOf("gdp" to 40000000.0, "environment" to -3.0), listOf("high_yield_crops")))
        
        technologies.add(Technology(13, "Internet", "Digital communication network",
            TechCategory.COMPUTING, TechTier.TIER_2, 30, listOf(7),
            mapOf("education" to 10.0, "gdp" to 50000000.0), listOf("internet_infrastructure")))
        
        technologies.add(Technology(14, "Container Shipping", "Global trade optimization",
            TechCategory.TRANSPORTATION, TechTier.TIER_2, 20, listOf(8),
            mapOf("gdp" to 40000000.0, "internationalRelations" to 5.0), listOf("container_ports")))
        
        // TIER 3 - Advanced Technologies
        technologies.add(Technology(15, "Precision Guidance", "Smart weapons systems",
            TechCategory.MILITARY, TechTier.TIER_3, 40, listOf(9),
            mapOf("military" to 18.0, "gdp" to 25000000.0), listOf("smart_weapons")))
        
        technologies.add(Technology(16, "Renewable Energy", "Solar and wind power",
            TechCategory.ENERGY, TechTier.TIER_3, 35, listOf(10),
            mapOf("environment" to 20.0, "gdp" to 35000000.0), listOf("renewable_grid")))
        
        technologies.add(Technology(17, "Genetic Engineering", "DNA modification",
            TechCategory.BIOTECH, TechTier.TIER_3, 45, listOf(11),
            mapOf("healthcare" to 20.0, "education" to 10.0, "gdp" to 30000000.0), listOf("gene_therapy")))
        
        technologies.add(Technology(18, "Artificial Intelligence", "Machine learning systems",
            TechCategory.COMPUTING, TechTier.TIER_3, 50, listOf(13),
            mapOf("gdp" to 80000000.0, "education" to 15.0, "unemployment" to 2.0), listOf("ai_systems")))
        
        technologies.add(Technology(19, "High-Speed Rail", "Bullet train network",
            TechCategory.TRANSPORTATION, TechTier.TIER_3, 40, listOf(8, 14),
            mapOf("infrastructure" to 20.0, "gdp" to 45000000.0, "environment" to 5.0), listOf("hsr_network")))
        
        technologies.add(Technology(20, "Advanced Materials", "Carbon fiber and composites",
            TechCategory.MATERIALS, TechTier.TIER_3, 35, listOf(3),
            mapOf("gdp" to 35000000.0, "military" to 8.0), listOf("composite_materials")))
        
        // TIER 4 - Cutting Edge Technologies
        technologies.add(Technology(21, "Hypersonic Weapons", "Mach 5+ missile systems",
            TechCategory.MILITARY, TechTier.TIER_4, 60, listOf(15),
            mapOf("military" to 25.0, "gdp" to 30000000.0), listOf("hypersonic_missiles")))
        
        technologies.add(Technology(22, "Fusion Power", "Clean unlimited energy",
            TechCategory.ENERGY, TechTier.TIER_4, 80, listOf(10, 16),
            mapOf("environment" to 30.0, "gdp" to 100000000.0), listOf("fusion_reactors")))
        
        technologies.add(Technology(23, "Nanotechnology", "Molecular engineering",
            TechCategory.MATERIALS, TechTier.TIER_4, 70, listOf(17, 20),
            mapOf("healthcare" to 25.0, "gdp" to 60000000.0, "education" to 15.0), listOf("nano_materials")))
        
        technologies.add(Technology(24, "Quantum Computing", "Next-gen computation",
            TechCategory.COMPUTING, TechTier.TIER_4, 75, listOf(18),
            mapOf("gdp" to 100000000.0, "education" to 20.0, "military" to 15.0), listOf("quantum_computers")))
        
        technologies.add(Technology(25, "Autonomous Vehicles", "Self-driving transport",
            TechCategory.TRANSPORTATION, TechTier.TIER_4, 55, listOf(18, 19),
            mapOf("gdp" to 50000000.0, "environment" to 8.0, "infrastructure" to 10.0), listOf("autonomous_cars")))
        
        // TIER 5 - Futuristic Technologies
        technologies.add(Technology(26, "Space Colonization", "Off-world settlements",
            TechCategory.SPACE, TechTier.TIER_5, 100, listOf(22, 24),
            mapOf("gdp" to 200000000.0, "internationalRelations" to 20.0, "education" to 25.0), listOf("space_colonies")))
        
        technologies.add(Technology(27, "Mind Uploading", "Consciousness transfer",
            TechCategory.BIOTECH, TechTier.TIER_5, 120, listOf(17, 23, 24),
            mapOf("healthcare" to 40.0, "happiness" to 20.0, "education" to 30.0), listOf("digital_immortality")))
        
        technologies.add(Technology(28, "Anti-Gravity", "Gravity manipulation",
            TechCategory.TRANSPORTATION, TechTier.TIER_5, 150, listOf(22, 25),
            mapOf("gdp" to 150000000.0, "environment" to 15.0, "infrastructure" to 25.0), listOf("anti_gravity_transport")))
        
        technologies.add(Technology(29, "Global Shield", "Planetary defense system",
            TechCategory.MILITARY, TechTier.TIER_5, 130, listOf(21, 24),
            mapOf("military" to 40.0, "stability" to 15.0), listOf("missile_shield")))
        
        technologies.add(Technology(30, "Terraforming", "Planet modification",
            TechCategory.ENVIRONMENTAL, TechTier.TIER_5, 140, listOf(16, 22, 23),
            mapOf("environment" to 50.0, "gdp" to 100000000.0), listOf("climate_control")))
    }
    
    private fun initializeFacilities() {
        facilities.add(ResearchFacility(0, "National University", FacilityType.UNIVERSITY, 1, 100, 1.0, 0, 5, 5000000.0))
        facilities.add(ResearchFacility(1, "Government Research Lab", FacilityType.GOVERNMENT_LAB, 1, 50, 1.2, 0, 3, 8000000.0))
        facilities.add(ResearchFacility(2, "Military Research Center", FacilityType.MILITARY_LAB, 1, 30, 1.5, 0, 2, 12000000.0))
        facilities.add(ResearchFacility(3, "Medical Research Institute", FacilityType.MEDICAL_CENTER, 1, 40, 1.1, 0, 3, 6000000.0))
    }
    
    fun startResearch(techId: Int, country: Country): Boolean {
        val tech = technologies.find { it.id == techId } ?: return false
        
        if (tech.isResearched || tech.isBeingResearched) return false
        
        // Check prerequisites
        if (!tech.prerequisites.all { prereqId -> 
            technologies.find { it.id == prereqId }?.isResearched == true 
        }) {
            return false
        }
        
        // Check if we have enough treasury for initial investment
        val initialCost = tech.researchCost * 1000000.0
        if (country.treasury < initialCost) return false
        
        country.treasury -= initialCost
        tech.isBeingResearched = true
        
        return true
    }
    
    fun processResearchTurn(country: Country) {
        technologies.forEach { tech ->
            if (tech.isBeingResearched && !tech.isResearched) {
                // Calculate research progress
                val baseProgress = calculateResearchOutput(country)
                val tierMultiplier = when (tech.tier) {
                    TechTier.TIER_1 -> 1.0
                    TechTier.TIER_2 -> 0.8
                    TechTier.TIER_3 -> 0.6
                    TechTier.TIER_4 -> 0.4
                    TechTier.TIER_5 -> 0.25
                }
                
                tech.researchProgress += (baseProgress * tierMultiplier).toInt()
                
                // Check if research is complete
                if (tech.researchProgress >= tech.researchCost * 10) {
                    tech.isResearched = true
                    tech.isBeingResearched = false
                    applyTechnologyEffects(tech, country)
                }
            }
        }
        
        // Process research projects
        researchProjects.forEach { project ->
            if (!project.isCompleted && project.funding > 0) {
                project.progress++
                if (project.progress >= project.duration) {
                    project.isCompleted = true
                    applyProjectRewards(project, country)
                }
            }
        }
        
        // Update knowledge stock
        knowledgeStock = technologies.filter { it.isResearched }.sumOf { it.researchCost } * 1000000.0
        
        // Update scientific prestige
        scientificPrestige = (50.0 + knowledgeStock / 100000000.0 + patentCount * 0.1).coerceIn(0.0, 100.0)
    }
    
    private fun calculateResearchOutput(country: Country): Double {
        var output = 10.0 // Base output
        
        // Education bonus
        output *= (country.education / 50.0)
        
        // Facility bonus
        facilities.forEach { facility ->
            output *= (1.0 + facility.efficiency * facility.level / 100.0)
        }
        
        // Budget bonus
        if (totalResearchBudget > 0) {
            output *= (1.0 + totalResearchBudget / 100000000.0)
        }
        
        // International collaboration
        output *= (internationalCollaboration / 50.0)
        
        // Policy bonus
        EconomyManager.activePolicies.filter { it.isActive }.forEach { policy ->
            if (policy.policyType == PolicyType.INDUSTRIAL || policy.policyType == PolicyType.SOCIAL) {
                output *= 1.1
            }
        }
        
        return output * researchOutput
    }
    
    private fun applyTechnologyEffects(tech: Technology, country: Country) {
        tech.effects.forEach { (stat, value) ->
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
                "unemployment" -> EconomyManager.indicators.unemploymentRate = (EconomyManager.indicators.unemploymentRate + value).coerceIn(0.0, 50.0)
            }
        }
        
        // Increment patent count for certain technologies
        if (tech.category == TechCategory.COMPUTING || tech.category == TechCategory.BIOTECH || 
            tech.category == TechCategory.MATERIALS) {
            patentCount += 5
        }
    }
    
    private fun applyProjectRewards(project: ResearchProject, country: Country) {
        project.rewards.forEach { (stat, value) ->
            when (stat) {
                "treasury" -> country.treasury += value
                "gdp" -> country.gdp = (country.gdp + value).coerceAtLeast(0.0)
                "happiness" -> country.happiness = (country.happiness + value.toInt()).coerceIn(0, 100)
                "education" -> country.education = (country.education + value.toInt()).coerceIn(0, 100)
                "healthcare" -> country.healthcare = (country.healthcare + value.toInt()).coerceIn(0, 100)
            }
        }
    }
    
    fun getAvailableTechnologies(): List<Technology> {
        return technologies.filter { !it.isResearched && !it.isBeingResearched }
    }
    
    fun getResearchableTechnologies(): List<Technology> {
        return getAvailableTechnologies().filter { tech ->
            tech.prerequisites.all { prereqId ->
                technologies.find { it.id == prereqId }?.isResearched == true
            }
        }
    }
    
    fun getTechnologyProgress(techId: Int): Pair<Int, Int> {
        val tech = technologies.find { it.id == techId }
        return if (tech != null) {
            Pair(tech.researchProgress, tech.researchCost * 10)
        } else {
            Pair(0, 0)
        }
    }
    
    fun startResearchProject(project: ResearchProject, country: Country): Boolean {
        if (country.treasury < project.cost) return false
        
        // Check requirements
        if (!project.requirements.all { (stat, value) ->
            when (stat) {
                "education" -> country.education >= value.toInt()
                "treasury" -> country.treasury >= value
                "gdp" -> country.gdp >= value
                else -> false
            }
        }) return false
        
        country.treasury -= project.cost
        project.funding = project.cost
        researchProjects.add(project)
        
        return true
    }
    
    fun getAvailableProjects(): List<ResearchProject> {
        return listOf(
            ResearchProject(1, "Climate Study", "Research climate change impacts", 
                10000000.0, 5, mapOf("education" to 50.0), mapOf("environment" to 5.0, "education" to 3.0)),
            ResearchProject(2, "Disease Eradication", "Work towards eliminating major diseases",
                25000000.0, 8, mapOf("education" to 60.0, "healthcare" to 50.0), mapOf("healthcare" to 10.0, "happiness" to 5.0)),
            ResearchProject(3, "AI Safety", "Ensure AI development is safe",
                30000000.0, 6, mapOf("education" to 70.0), mapOf("education" to 8.0, "stability" to 5.0)),
            ResearchProject(4, "Clean Energy", "Develop new clean energy sources",
                40000000.0, 10, mapOf("education" to 55.0, "environment" to 40.0), mapOf("environment" to 12.0, "gdp" to 20000000.0)),
            ResearchProject(5, "Space Exploration", "Fund space research program",
                50000000.0, 12, mapOf("education" to 65.0, "gdp" to 1000000000.0), mapOf("education" to 15.0, "internationalRelations" to 10.0)),
            ResearchProject(6, "Poverty Reduction", "Research effective poverty solutions",
                15000000.0, 6, mapOf("education" to 45.0), mapOf("happiness" to 8.0, "stability" to 5.0)),
            ResearchProject(7, "Cybersecurity", "Develop advanced cyber defenses",
                20000000.0, 5, mapOf("education" to 50.0), mapOf("military" to 8.0, "stability" to 5.0)),
            ResearchProject(8, "Agricultural Innovation", "Improve crop yields sustainably",
                18000000.0, 7, mapOf("education" to 45.0, "environment" to 35.0), mapOf("gdp" to 25000000.0, "environment" to 3.0))
        )
    }
    
    fun upgradeFacility(facilityId: Int, country: Country): Boolean {
        val facility = facilities.find { it.id == facilityId } ?: return false
        
        val upgradeCost = facility.maintenanceCost * 10 * facility.level
        if (country.treasury < upgradeCost) return false
        
        country.treasury -= upgradeCost
        facility.level++
        facility.capacity = (facility.capacity * 1.2).toInt()
        facility.efficiency = (facility.efficiency * 1.1).coerceAtMost(2.0)
        facility.maintenanceCost = facility.maintenanceCost * 1.2
        
        return true
    }
    
    fun getTechnologySummary(): String {
        val summary = StringBuilder()
        summary.append("=== TECHNOLOGY SUMMARY ===\n\n")
        
        val researchedCount = technologies.count { it.isResearched }
        val totalTechs = technologies.size
        
        summary.append("Technologies Researched: $researchedCount / $totalTechs\n")
        summary.append("Knowledge Stock: $${String.format("%,d", knowledgeStock.toLong())}\n")
        summary.append("Patents: $patentCount\n")
        summary.append("Scientific Prestige: ${scientificPrestige.toInt()}/100\n")
        summary.append("International Collaboration: ${internationalCollaboration.toInt()}%\n\n")
        
        summary.append("By Tier:\n")
        TechTier.values().forEach { tier ->
            val count = technologies.count { it.tier == tier && it.isResearched }
            val total = technologies.count { it.tier == tier }
            summary.append("  ${tier.name}: $count / $total\n")
        }
        
        summary.append("\nBy Category:\n")
        TechCategory.values().forEach { cat ->
            val count = technologies.count { it.category == cat && it.isResearched }
            if (count > 0) {
                summary.append("  ${cat.name}: $count\n")
            }
        }
        
        val inProgress = technologies.count { it.isBeingResearched }
        if (inProgress > 0) {
            summary.append("\nIn Progress: $inProgress technologies\n")
        }
        
        return summary.toString()
    }
}
