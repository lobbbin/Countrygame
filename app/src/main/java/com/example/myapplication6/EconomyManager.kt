package com.example.myapplication6

import java.io.Serializable

/**
 * Advanced Economic Management System v2.0
 * Handles micro and macro economic management
 */

// Economic Sectors
enum class EconomicSector {
    AGRICULTURE,
    MANUFACTURING,
    SERVICES,
    TECHNOLOGY,
    ENERGY,
    FINANCE,
    HEALTHCARE,
    EDUCATION,
    TOURISM,
    DEFENSE
}

// Industry types within sectors
data class Industry(
    val id: Int,
    val name: String,
    val sector: EconomicSector,
    val baseOutput: Double,
    val employmentCapacity: Int,
    val taxRate: Double,
    val environmentalImpact: Int,
    val description: String = "Industrial facility",
    var level: Int = 1,
    var efficiency: Double = 1.0,
    var employees: Int = 0,
    var isOperational: Boolean = true
) : Serializable

// Economic indicators
data class EconomicIndicators(
    var gdpReal: Double = 1000000000.0,
    var gdpNominal: Double = 1000000000.0,
    var gdpGrowthRate: Double = 2.0,
    var inflationRate: Double = 2.0,
    var unemploymentRate: Double = 5.0,
    var interestRate: Double = 3.0,
    var exchangeRate: Double = 1.0,
    var tradeBalance: Double = 0.0,
    var budgetDeficit: Double = 0.0,
    var nationalDebt: Double = 100000000.0,
    var debtToGdpRatio: Double = 10.0,
    var consumerConfidence: Double = 50.0,
    var businessConfidence: Double = 50.0,
    var productivityIndex: Double = 100.0,
    var wageGrowth: Double = 2.0,
    var housingPrices: Double = 100.0,
    var stockMarketIndex: Double = 1000.0
) : Serializable

// Tax brackets
data class TaxBracket(
    val minIncome: Double,
    val maxIncome: Double,
    val taxRate: Double
) : Serializable

// Budget allocation
data class BudgetAllocation(
    var defense: Double = 15.0,
    var education: Double = 10.0,
    var healthcare: Double = 10.0,
    var infrastructure: Double = 8.0,
    var socialWelfare: Double = 12.0,
    var debtService: Double = 5.0,
    var research: Double = 3.0,
    var environment: Double = 2.0,
    var lawEnforcement: Double = 5.0,
    var foreignAid: Double = 1.0,
    var administration: Double = 4.0,
    var emergency: Double = 5.0
) : Serializable {
    fun getTotal(): Double {
        return defense + education + healthcare + infrastructure + socialWelfare + 
               debtService + research + environment + lawEnforcement + foreignAid + 
               administration + emergency
    }
    
    fun isValid(): Boolean {
        return getTotal() <= 100.0
    }
}

// Economic policy
data class EconomicPolicy(
    val id: Int,
    val name: String,
    val description: String,
    val policyType: PolicyType,
    val effects: Map<String, Double>,
    val cost: Double,
    val duration: Int,
    var isActive: Boolean = false,
    var turnsRemaining: Int = 0,
    val requirements: Map<String, Double> = emptyMap()
) : Serializable

enum class PolicyType {
    FISCAL,
    MONETARY,
    TRADE,
    LABOR,
    INDUSTRIAL,
    ENVIRONMENTAL,
    SOCIAL
}

// Resource types
data class Resource(
    val id: Int,
    val name: String,
    val type: ResourceType,
    val basePrice: Double,
    var currentPrice: Double,
    var stockpile: Double,
    var production: Double,
    var consumption: Double,
    var importAmount: Double,
    var exportAmount: Double
) : Serializable

enum class ResourceType {
    RAW_MATERIAL,
    ENERGY,
    FOOD,
    MANUFACTURED,
    STRATEGIC,
    PRECIOUS
}

// Trade deal
data class TradeDeal(
    val id: Int,
    val partnerNation: String,
    val resources: Map<String, Double>,
    val duration: Int,
    val value: Double,
    val relationImpact: Int,
    var isActive: Boolean = false
) : Serializable

// Economic zone
data class EconomicZone(
    val id: Int,
    val name: String,
    val zoneType: ZoneType,
    val taxIncentive: Double,
    val regulations: Double,
    val investment: Double,
    val companies: Int,
    val jobs: Int,
    val output: Double
) : Serializable

enum class ZoneType {
    FREE_TRADE,
    INDUSTRIAL,
    TECHNOLOGY,
    AGRICULTURAL,
    TOURISM,
    RESIDENTIAL,
    COMMERCIAL
}

// Market conditions
data class MarketCondition(
    val conditionType: MarketConditionType,
    val severity: Int,
    var turnsRemaining: Int,
    val effects: Map<String, Double>
) : Serializable

enum class MarketConditionType {
    BOOM,
    RECESSION,
    DEPRESSION,
    STAGFLATION,
    HYPERINFLATION,
    DEFLATION,
    NORMAL
}

// Central Bank actions
enum class CentralBankAction {
    RAISE_RATES,
    LOWER_RATES,
    HOLD_RATES,
    QUANTITATIVE_EASING,
    QUANTITATIVE_TIGHTENING,
    FORWARD_GUIDANCE
}

// Object for economic management
object EconomyManager : Serializable {
    
    val industries = mutableListOf<Industry>()
    val resources = mutableListOf<Resource>()
    val activePolicies = mutableListOf<EconomicPolicy>()
    val tradeDeals = mutableListOf<TradeDeal>()
    val economicZones = mutableListOf<EconomicZone>()
    val marketConditions = mutableListOf<MarketCondition>()
    
    val indicators = EconomicIndicators()
    val budget = BudgetAllocation()
    
    var centralBankIndependence: Double = 70.0
    var creditRating: String = "AA"
    var sovereignWealthFund: Double = 0.0
    var foreignReserves: Double = 50000000.0
    var moneySupply: Double = 500000000.0
    var velocityOfMoney: Double = 1.5
    
    fun initializeEconomy() {
        industries.clear()
        resources.clear()
        economicZones.clear()
        
        initializeIndustries()
        initializeResources()
        initializeEconomicZones()
        initializePolicies()
    }
    
    private fun initializeIndustries() {
        // Agriculture
        industries.add(Industry(1, "Farming", EconomicSector.AGRICULTURE, 50000000.0, 50000, 0.05, 3))
        industries.add(Industry(2, "Fishing", EconomicSector.AGRICULTURE, 20000000.0, 10000, 0.05, 1))
        industries.add(Industry(3, "Forestry", EconomicSector.AGRICULTURE, 30000000.0, 15000, 0.05, 2))
        
        // Manufacturing
        industries.add(Industry(4, "Automotive", EconomicSector.MANUFACTURING, 100000000.0, 80000, 0.15, 5))
        industries.add(Industry(5, "Electronics", EconomicSector.MANUFACTURING, 80000000.0, 60000, 0.15, 4))
        industries.add(Industry(6, "Textiles", EconomicSector.MANUFACTURING, 40000000.0, 40000, 0.10, 3))
        industries.add(Industry(7, "Steel", EconomicSector.MANUFACTURING, 60000000.0, 35000, 0.12, 6))
        
        // Services
        industries.add(Industry(8, "Banking", EconomicSector.FINANCE, 150000000.0, 30000, 0.20, 1))
        industries.add(Industry(9, "Insurance", EconomicSector.FINANCE, 80000000.0, 25000, 0.18, 1))
        industries.add(Industry(10, "Retail", EconomicSector.SERVICES, 120000000.0, 150000, 0.10, 2))
        
        // Technology
        industries.add(Industry(11, "Software", EconomicSector.TECHNOLOGY, 200000000.0, 40000, 0.15, 1))
        industries.add(Industry(12, "Biotech", EconomicSector.TECHNOLOGY, 150000000.0, 25000, 0.15, 2))
        industries.add(Industry(13, "AI Research", EconomicSector.TECHNOLOGY, 100000000.0, 15000, 0.12, 1))
        
        // Energy
        industries.add(Industry(14, "Oil & Gas", EconomicSector.ENERGY, 180000000.0, 30000, 0.20, 8))
        industries.add(Industry(15, "Renewables", EconomicSector.ENERGY, 100000000.0, 25000, 0.10, 0))
        industries.add(Industry(16, "Nuclear", EconomicSector.ENERGY, 250000000.0, 10000, 0.15, 3))
        
        // Healthcare
        industries.add(Industry(17, "Pharmaceuticals", EconomicSector.HEALTHCARE, 120000000.0, 20000, 0.15, 3))
        industries.add(Industry(18, "Medical Devices", EconomicSector.HEALTHCARE, 80000000.0, 15000, 0.15, 2))
        
        // Tourism
        industries.add(Industry(19, "Hotels", EconomicSector.TOURISM, 60000000.0, 50000, 0.10, 2))
        industries.add(Industry(20, "Airlines", EconomicSector.TOURISM, 100000000.0, 20000, 0.12, 5))
    }
    
    private fun initializeResources() {
        resources.add(Resource(1, "Crude Oil", ResourceType.ENERGY, 80.0, 80.0, 1000000.0, 50000.0, 40000.0, 10000.0, 20000.0))
        resources.add(Resource(2, "Natural Gas", ResourceType.ENERGY, 3.0, 3.0, 5000000.0, 100000.0, 80000.0, 20000.0, 40000.0))
        resources.add(Resource(3, "Coal", ResourceType.ENERGY, 100.0, 100.0, 2000000.0, 30000.0, 25000.0, 5000.0, 10000.0))
        resources.add(Resource(4, "Electricity", ResourceType.ENERGY, 0.1, 0.1, 10000000.0, 500000.0, 480000.0, 0.0, 20000.0))
        
        resources.add(Resource(5, "Wheat", ResourceType.FOOD, 6.0, 6.0, 500000.0, 100000.0, 90000.0, 5000.0, 15000.0))
        resources.add(Resource(6, "Rice", ResourceType.FOOD, 12.0, 12.0, 300000.0, 60000.0, 55000.0, 3000.0, 8000.0))
        resources.add(Resource(7, "Corn", ResourceType.FOOD, 5.0, 5.0, 400000.0, 80000.0, 75000.0, 2000.0, 7000.0))
        resources.add(Resource(8, "Livestock", ResourceType.FOOD, 100.0, 100.0, 100000.0, 20000.0, 18000.0, 1000.0, 3000.0))
        
        resources.add(Resource(9, "Iron Ore", ResourceType.RAW_MATERIAL, 120.0, 120.0, 800000.0, 40000.0, 35000.0, 5000.0, 10000.0))
        resources.add(Resource(10, "Copper", ResourceType.RAW_MATERIAL, 9000.0, 9000.0, 200000.0, 10000.0, 9000.0, 2000.0, 3000.0))
        resources.add(Resource(11, "Aluminum", ResourceType.RAW_MATERIAL, 2500.0, 2500.0, 300000.0, 15000.0, 14000.0, 3000.0, 4000.0))
        
        resources.add(Resource(12, "Gold", ResourceType.PRECIOUS, 1900.0, 1900.0, 50000.0, 500.0, 200.0, 100.0, 400.0))
        resources.add(Resource(13, "Silver", ResourceType.PRECIOUS, 23.0, 23.0, 100000.0, 2000.0, 1000.0, 500.0, 1500.0))
        
        resources.add(Resource(14, "Semiconductors", ResourceType.MANUFACTURED, 500.0, 500.0, 100000.0, 10000.0, 9500.0, 500.0, 1000.0))
        resources.add(Resource(15, "Machinery", ResourceType.MANUFACTURED, 10000.0, 10000.0, 50000.0, 5000.0, 4800.0, 500.0, 700.0))
        
        resources.add(Resource(16, "Rare Earth Elements", ResourceType.STRATEGIC, 50000.0, 50000.0, 10000.0, 1000.0, 800.0, 100.0, 300.0))
        resources.add(Resource(17, "Uranium", ResourceType.STRATEGIC, 100000.0, 100000.0, 5000.0, 500.0, 400.0, 50.0, 150.0))
    }
    
    private fun initializeEconomicZones() {
        economicZones.add(EconomicZone(1, "Capital Business District", ZoneType.COMMERCIAL, 0.0, 0.8, 500000000.0, 500, 50000, 200000000.0))
        economicZones.add(EconomicZone(2, "Industrial Park Alpha", ZoneType.INDUSTRIAL, 0.05, 0.5, 300000000.0, 200, 30000, 150000000.0))
        economicZones.add(EconomicZone(3, "Tech Hub", ZoneType.TECHNOLOGY, 0.10, 0.3, 400000000.0, 150, 20000, 180000000.0))
        economicZones.add(EconomicZone(4, "Free Trade Port", ZoneType.FREE_TRADE, 0.15, 0.2, 600000000.0, 300, 40000, 250000000.0))
        economicZones.add(EconomicZone(5, "Agricultural Zone", ZoneType.AGRICULTURAL, 0.02, 0.4, 150000000.0, 100, 25000, 80000000.0))
    }
    
    private fun initializePolicies() {
        activePolicies.clear()
    }
    
    fun getAvailablePolicies(): List<EconomicPolicy> {
        return listOf(
            // Fiscal Policies
            EconomicPolicy(1, "Tax Cut for Middle Class", "Reduce income tax rates for middle-income earners", 
                PolicyType.FISCAL, mapOf("happiness" to 10.0, "gdp" to 50000000.0, "treasury" to -20000000.0), 
                0.0, 12),
            EconomicPolicy(2, "Corporate Tax Reduction", "Lower corporate tax rate to stimulate business investment",
                PolicyType.FISCAL, mapOf("gdp" to 80000000.0, "businessConfidence" to 15.0, "treasury" to -15000000.0),
                0.0, 12, mapOf("gdp" to 1500000000.0)),
            EconomicPolicy(3, "Infrastructure Investment", "Major public works program",
                PolicyType.FISCAL, mapOf("infrastructure" to 15.0, "gdp" to 60000000.0, "unemployment" to -2.0),
                100000000.0, 24),
            EconomicPolicy(4, "Austerity Measures", "Reduce government spending to balance budget",
                PolicyType.FISCAL, mapOf("budgetDeficit" to -30000000.0, "happiness" to -15.0, "stability" to -10.0),
                0.0, 12),
            
            // Monetary Policies
            EconomicPolicy(5, "Quantitative Easing", "Central bank purchases government bonds",
                PolicyType.MONETARY, mapOf("interestRate" to -1.0, "inflation" to 2.0, "gdp" to 40000000.0),
                0.0, 6, mapOf("interestRate" to 1.0)),
            EconomicPolicy(6, "Interest Rate Hike", "Raise rates to combat inflation",
                PolicyType.MONETARY, mapOf("inflation" to -3.0, "gdp" to -20000000.0, "unemployment" to 1.0),
                0.0, 6, mapOf("inflation" to 5.0)),
            
            // Trade Policies
            EconomicPolicy(7, "Free Trade Agreement", "Reduce tariffs with partner nations",
                PolicyType.TRADE, mapOf("gdp" to 50000000.0, "tradeBalance" to 20000000.0, "internationalRelations" to 10.0),
                0.0, 24),
            EconomicPolicy(8, "Protectionist Tariffs", "Impose tariffs on imports to protect domestic industry",
                PolicyType.TRADE, mapOf("manufacturing" to 10.0, "tradeBalance" to -10000000.0, "internationalRelations" to -15.0),
                0.0, 12),
            
            // Labor Policies
            EconomicPolicy(9, "Minimum Wage Increase", "Raise the minimum wage",
                PolicyType.LABOR, mapOf("happiness" to 8.0, "unemployment" to 1.0, "wageGrowth" to 3.0),
                0.0, 12),
            EconomicPolicy(10, "Labor Market Deregulation", "Reduce employment regulations",
                PolicyType.LABOR, mapOf("unemployment" to -2.0, "businessConfidence" to 10.0, "happiness" to -5.0),
                0.0, 12),
            
            // Industrial Policies
            EconomicPolicy(11, "Green Energy Subsidy", "Subsidize renewable energy production",
                PolicyType.INDUSTRIAL, mapOf("environment" to 15.0, "energy" to 10.0, "treasury" to -25000000.0),
                50000000.0, 24),
            EconomicPolicy(12, "Manufacturing Revival", "Incentives for domestic manufacturing",
                PolicyType.INDUSTRIAL, mapOf("manufacturing" to 20.0, "unemployment" to -1.5, "gdp" to 40000000.0),
                30000000.0, 18),
            
            // Environmental Policies
            EconomicPolicy(13, "Carbon Tax", "Tax on carbon emissions",
                PolicyType.ENVIRONMENTAL, mapOf("environment" to 20.0, "treasury" to 30000000.0, "gdp" to -15000000.0),
                0.0, 24),
            EconomicPolicy(14, "Conservation Program", "Protect natural habitats",
                PolicyType.ENVIRONMENTAL, mapOf("environment" to 25.0, "treasury" to -15000000.0),
                20000000.0, 12),
            
            // Social Policies
            EconomicPolicy(15, "Universal Basic Income", "Monthly payment to all citizens",
                PolicyType.SOCIAL, mapOf("happiness" to 20.0, "poverty" to -30.0, "treasury" to -100000000.0),
                0.0, 24, mapOf("gdp" to 2000000000.0)),
            EconomicPolicy(16, "Healthcare Expansion", "Universal healthcare coverage",
                PolicyType.SOCIAL, mapOf("healthcare" to 25.0, "happiness" to 15.0, "treasury" to -50000000.0),
                75000000.0, 24)
        )
    }
    
    fun activatePolicy(policy: EconomicPolicy, country: Country): Boolean {
        if (!policy.requirements.all { checkRequirement(country, it.key, it.value) }) {
            return false
        }
        
        if (policy.cost > 0 && country.treasury < policy.cost) {
            return false
        }
        
        // Apply cost
        if (policy.cost > 0) {
            country.treasury -= policy.cost
        }
        
        // Activate policy
        policy.isActive = true
        policy.turnsRemaining = policy.duration
        activePolicies.add(policy)
        
        // Apply immediate effects
        applyPolicyEffects(policy, country)
        
        return true
    }
    
    private fun checkRequirement(country: Country, key: String, value: Double): Boolean {
        return when (key) {
            "gdp" -> country.gdp >= value
            "treasury" -> country.treasury >= value
            "stability" -> country.stability >= value.toInt()
            "happiness" -> country.happiness >= value.toInt()
            "education" -> country.education >= value.toInt()
            "infrastructure" -> country.infrastructure >= value.toInt()
            "interestRate" -> indicators.interestRate >= value
            "inflation" -> indicators.inflationRate >= value
            "military" -> country.military >= value.toInt()
            "internationalRelations" -> country.internationalRelations >= value.toInt()
            "healthcare" -> country.healthcare >= value.toInt()
            "unemployment" -> indicators.unemploymentRate <= value
            else -> false
        }
    }
    
    private fun applyPolicyEffects(policy: EconomicPolicy, country: Country) {
        policy.effects.forEach { (stat, value) ->
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
                "unemployment" -> indicators.unemploymentRate = (indicators.unemploymentRate + value).coerceIn(0.0, 50.0)
                "inflation" -> indicators.inflationRate = (indicators.inflationRate + value).coerceIn(-5.0, 50.0)
                "interestRate" -> indicators.interestRate = (indicators.interestRate + value).coerceIn(0.0, 25.0)
                "businessConfidence" -> indicators.businessConfidence = (indicators.businessConfidence + value).coerceIn(0.0, 100.0)
                "consumerConfidence" -> indicators.consumerConfidence = (indicators.consumerConfidence + value).coerceIn(0.0, 100.0)
            }
        }
    }
    
    fun processEconomicTurn(country: Country) {
        // Process active policies
        activePolicies.removeAll { policy ->
            if (policy.isActive) {
                policy.turnsRemaining--
                if (policy.turnsRemaining <= 0) {
                    policy.isActive = false
                    true
                } else {
                    false
                }
            } else {
                true
            }
        }
        
        // Update economic indicators
        updateEconomicIndicators(country)
        
        // Process industries
        processIndustries(country)
        
        // Process resources
        processResources()
        
        // Process trade
        processTrade(country)
        
        // Process market conditions
        processMarketConditions(country)
        
        // Update budget
        updateBudget(country)
    }
    
    private fun updateEconomicIndicators(country: Country) {
        // GDP calculation based on industry output
        val totalIndustryOutput = industries.sumOf { it.baseOutput * it.level * it.efficiency }
        indicators.gdpReal = totalIndustryOutput
        
        // GDP growth
        indicators.gdpGrowthRate = ((indicators.gdpReal - country.gdp) / country.gdp) * 100
        
        // Inflation based on money supply and velocity
        val nominalGDP = moneySupply * velocityOfMoney
        indicators.inflationRate = ((nominalGDP - indicators.gdpReal) / indicators.gdpReal) * 100
        indicators.inflationRate = indicators.inflationRate.coerceIn(-5.0, 50.0)
        
        // Unemployment based on industry employment
        val totalEmployment = industries.sumOf { it.employees }
        val laborForce = country.population * 0.6 // 60% of population is labor force
        indicators.unemploymentRate = ((laborForce - totalEmployment).toDouble() / laborForce) * 100
        indicators.unemploymentRate = indicators.unemploymentRate.coerceIn(0.0, 50.0)
        
        // Business confidence
        indicators.businessConfidence = (50.0 + 
            (indicators.gdpGrowthRate * 2) - 
            (indicators.inflationRate) - 
            (indicators.unemploymentRate / 2)).coerceIn(0.0, 100.0)
        
        // Consumer confidence
        indicators.consumerConfidence = (50.0 + 
            (country.happiness / 2.0) - 
            (indicators.inflationRate * 2) - 
            (indicators.unemploymentRate)).coerceIn(0.0, 100.0)
        
        // Debt to GDP ratio
        indicators.debtToGdpRatio = (indicators.nationalDebt / indicators.gdpReal) * 100
        
        // Credit rating based on debt ratio
        creditRating = when {
            indicators.debtToGdpRatio < 30 -> "AAA"
            indicators.debtToGdpRatio < 50 -> "AA"
            indicators.debtToGdpRatio < 70 -> "A"
            indicators.debtToGdpRatio < 90 -> "BBB"
            indicators.debtToGdpRatio < 120 -> "BB"
            indicators.debtToGdpRatio < 150 -> "B"
            else -> "CCC"
        }
    }
    
    private fun processIndustries(country: Country) {
        industries.forEach { industry ->
            if (!industry.isOperational) return@forEach
            
            // Calculate employment based on level and efficiency
            val maxEmployment = (industry.employmentCapacity * industry.level).toDouble() * industry.efficiency
            industry.employees = (maxEmployment * (100 - indicators.unemploymentRate) / 100).toInt()
            
            // Efficiency changes based on various factors
            val efficiencyChange = when (industry.sector) {
                EconomicSector.TECHNOLOGY -> (country.education - 50) / 200.0
                EconomicSector.MANUFACTURING -> (country.infrastructure - 50) / 200.0
                EconomicSector.AGRICULTURE -> (country.environment - 50) / 300.0
                EconomicSector.ENERGY -> if (industry.environmentalImpact > 5) -0.01 else 0.01
                else -> 0.0
            }
            
            industry.efficiency = (industry.efficiency + efficiencyChange).coerceIn(0.5, 2.0)
            
            // Apply policy effects
            activePolicies.filter { it.isActive }.forEach { policy ->
                if (policy.policyType == PolicyType.INDUSTRIAL) {
                    industry.efficiency = (industry.efficiency + 0.01).coerceIn(0.5, 2.0)
                }
            }
        }
    }
    
    private fun processResources() {
        resources.forEach { resource ->
            // Price fluctuation based on supply/demand
            val supply = resource.stockpile + resource.production + resource.importAmount
            val demand = resource.consumption + resource.exportAmount
            
            val priceChange = if (supply > demand) {
                -((supply - demand) / supply * 0.1)
            } else {
                ((demand - supply) / demand * 0.1)
            }
            
            resource.currentPrice = (resource.currentPrice * (1 + priceChange)).coerceAtLeast(resource.basePrice * 0.1)
            
            // Stockpile changes
            resource.stockpile = resource.stockpile + resource.production - resource.consumption + 
                                resource.importAmount - resource.exportAmount
            resource.stockpile = resource.stockpile.coerceAtLeast(0.0)
        }
    }
    
    private fun processTrade(country: Country) {
        // Calculate trade balance
        val totalImports = resources.sumOf { it.importAmount * it.currentPrice }
        val totalExports = resources.sumOf { it.exportAmount * it.currentPrice }
        indicators.tradeBalance = totalExports - totalImports
        
        // Apply trade deal effects
        tradeDeals.filter { it.isActive }.forEach { deal ->
            country.internationalRelations = (country.internationalRelations + deal.relationImpact / deal.duration).coerceIn(0, 100)
        }
    }
    
    private fun processMarketConditions(country: Country) {
        // Random market condition changes
        if (Math.random() < 0.05) { // 5% chance per turn
            val condition = when ((Math.random() * 10).toInt()) {
                0, 1 -> MarketConditionType.RECESSION
                2 -> MarketConditionType.DEPRESSION
                3 -> MarketConditionType.STAGFLATION
                4 -> MarketConditionType.HYPERINFLATION
                5 -> MarketConditionType.DEFLATION
                6, 7 -> MarketConditionType.BOOM
                else -> MarketConditionType.NORMAL
            }
            
            if (condition != MarketConditionType.NORMAL && marketConditions.none { it.conditionType == condition }) {
                val effects = when (condition) {
                    MarketConditionType.BOOM -> mapOf("gdp" to 50000000.0, "inflation" to 2.0, "happiness" to 5.0)
                    MarketConditionType.RECESSION -> mapOf("gdp" to -30000000.0, "unemployment" to 2.0, "happiness" to -10.0)
                    MarketConditionType.DEPRESSION -> mapOf("gdp" to -100000000.0, "unemployment" to 5.0, "stability" to -20.0)
                    MarketConditionType.STAGFLATION -> mapOf("inflation" to 5.0, "unemployment" to 3.0, "gdp" to -20000000.0)
                    MarketConditionType.HYPERINFLATION -> mapOf("inflation" to 20.0, "happiness" to -30.0, "stability" to -25.0)
                    MarketConditionType.DEFLATION -> mapOf("inflation" to -5.0, "gdp" to -20000000.0, "unemployment" to 2.0)
                    else -> emptyMap()
                }
                
                marketConditions.add(MarketCondition(condition, 5, 6, effects))
            }
        }
        
        // Process active conditions
        marketConditions.removeAll { condition ->
            condition.turnsRemaining--
            
            // Apply effects
            condition.effects.forEach { (stat, value) ->
                when (stat) {
                    "gdp" -> country.gdp = (country.gdp + value).coerceAtLeast(0.0)
                    "inflation" -> indicators.inflationRate = (indicators.inflationRate + value).coerceIn(-5.0, 50.0)
                    "unemployment" -> indicators.unemploymentRate = (indicators.unemploymentRate + value).coerceIn(0.0, 50.0)
                    "happiness" -> country.happiness = (country.happiness + value.toInt()).coerceIn(0, 100)
                    "stability" -> country.stability = (country.stability + value.toInt()).coerceIn(0, 100)
                }
            }
            
            condition.turnsRemaining <= 0
        }
    }
    
    private fun updateBudget(country: Country) {
        // Calculate total budget percentage
        val totalPercentage = budget.getTotal()
        
        // Calculate revenue (taxes)
        val taxRevenue = country.gdp * 0.15 // Base 15% tax rate
        
        // Calculate expenditures
        val totalExpenditure = taxRevenue * (totalPercentage / 100.0)
        
        // Budget deficit/surplus
        indicators.budgetDeficit = totalExpenditure - taxRevenue
        
        // Update national debt
        if (indicators.budgetDeficit > 0) {
            indicators.nationalDebt += indicators.budgetDeficit
        } else {
            indicators.nationalDebt = (indicators.nationalDebt - Math.abs(indicators.budgetDeficit) * 0.5).coerceAtLeast(0.0)
        }
    }
    
    fun getIndustryOutput(sector: EconomicSector): Double {
        return industries.filter { it.sector == sector }.sumOf { it.baseOutput * it.level * it.efficiency }
    }
    
    fun getTotalEmployment(): Int {
        return industries.sumOf { it.employees }
    }
    
    fun getTaxRevenue(country: Country): Double {
        var revenue = 0.0
        
        // Industry taxes
        industries.forEach { industry ->
            revenue += industry.baseOutput * industry.level * industry.efficiency * industry.taxRate
        }
        
        // Income tax
        revenue += country.gdp * 0.08
        
        // Resource taxes
        resources.forEach { resource ->
            revenue += resource.exportAmount * resource.currentPrice * 0.05
        }
        
        return revenue
    }
    
    fun canAffordPolicy(policy: EconomicPolicy, country: Country): Boolean {
        return country.treasury >= policy.cost && 
               policy.requirements.all { checkRequirement(country, it.key, it.value) }
    }
    
    fun getEconomicSummary(country: Country): String {
        val summary = StringBuilder()
        summary.append("=== ECONOMIC SUMMARY ===\n\n")
        
        summary.append("GDP: $${String.format("%,d", indicators.gdpReal.toLong())}\n")
        summary.append("GDP Growth: ${String.format("%.2f", indicators.gdpGrowthRate)}%\n")
        summary.append("Inflation: ${String.format("%.2f", indicators.inflationRate)}%\n")
        summary.append("Unemployment: ${String.format("%.2f", indicators.unemploymentRate)}%\n")
        summary.append("Interest Rate: ${String.format("%.2f", indicators.interestRate)}%\n\n")
        
        summary.append("Trade Balance: $${String.format("%,d", indicators.tradeBalance.toLong())}\n")
        summary.append("Budget Deficit: $${String.format("%,d", indicators.budgetDeficit.toLong())}\n")
        summary.append("National Debt: $${String.format("%,d", indicators.nationalDebt.toLong())}\n")
        summary.append("Debt-to-GDP: ${String.format("%.2f", indicators.debtToGdpRatio)}%\n")
        summary.append("Credit Rating: $creditRating\n\n")
        
        summary.append("Business Confidence: ${String.format("%.2f", indicators.businessConfidence)}\n")
        summary.append("Consumer Confidence: ${String.format("%.2f", indicators.consumerConfidence)}\n\n")
        
        summary.append("Active Policies: ${activePolicies.count { it.isActive }}\n")
        summary.append("Market Condition: ${marketConditions.firstOrNull()?.conditionType ?: MarketConditionType.NORMAL}\n")
        
        return summary.toString()
    }
}
