package com.example.myapplication6

import java.io.Serializable

/**
 * Country Sim v3.0 - Housing & Real Estate System
 * Handles residential, commercial, and industrial property markets
 */

// Property types
enum class PropertyType {
    RESIDENTIAL,
    COMMERCIAL,
    INDUSTRIAL,
    AGRICULTURAL,
    MIXED_USE,
    GOVERNMENT,
    RECREATIONAL
}

// Housing types
enum class HousingType {
    SINGLE_FAMILY,
    APARTMENT,
    CONDOMINIUM,
    TOWNHOUSE,
    DUPLEX,
    MANSION,
    DORMITORY,
    PUBLIC_HOUSING,
    SLUM,
    LUXURY_TOWER
}

// Property data
data class Property(
    val id: Int,
    val address: String,
    val type: PropertyType,
    val housingType: HousingType?,
    val value: Double,
    val size: Double, // square meters
    val yearBuilt: Int,
    val condition: Int, // 0-100
    val region: Int,
    val isOccupied: Boolean,
    val owner: String,
    val monthlyRent: Double
) : Serializable

// Real estate development
data class RealEstateDevelopment(
    val id: Int,
    val name: String,
    val type: PropertyType,
    val totalUnits: Int,
    val soldUnits: Int,
    val totalValue: Double,
    val completionDate: Int,
    val developer: String,
    val region: Int
) : Serializable

// Housing market data
data class HousingMarket(
    val region: Int,
    val averagePrice: Double,
    val priceChange: Double, // percentage
    val inventory: Int,
    val salesVolume: Int,
    val rentalYield: Double,
    val vacancyRate: Double,
    val affordabilityIndex: Double // 0-100, higher = more affordable
) : Serializable

// Housing statistics
data class HousingStatistics(
    var totalProperties: Int = 0,
    var totalValue: Double = 0.0,
    var homeownershipRate: Double = 50.0,
    var vacancyRate: Double = 10.0,
    var averageRent: Double = 1000.0,
    var priceToIncomeRatio: Double = 5.0,
    var constructionRate: Double = 2.0, // percentage growth
    var homelessnessRate: Double = 1.0,
    var slumPopulation: Int = 0,
    var publicHousingUnits: Int = 0
) : Serializable

// Object for housing and real estate management
object HousingRealEstateManager : Serializable {

    val properties = mutableListOf<Property>()
    val developments = mutableListOf<RealEstateDevelopment>()
    val housingMarkets = mutableListOf<HousingMarket>()
    val statistics = HousingStatistics()

    var housingBudget: Double = 100000000.0
    var propertyTaxRate: Double = 0.01 // 1% annual
    var stampDutyRate: Double = 0.03 // 3% on purchase
    var rentControlEnabled: Boolean = false
    var rentControlLimit: Double = 0.05 // 5% annual increase max
    var zoningStrictness: Int = 50 // 0-100

    fun initializeHousing() {
        properties.clear()
        developments.clear()
        housingMarkets.clear()

        // Create starting properties
        for (i in 1..100) {
            val type = PropertyType.values().random()
            val housingType = if (type == PropertyType.RESIDENTIAL) HousingType.values().random() else null
            val value = (100000.0..2000000.0).random()

            properties.add(Property(
                id = i,
                address = "${(1..9999).random()} ${getRandomStreetName()}",
                type = type,
                housingType = housingType,
                value = value,
                size = (50.0..500.0).random(),
                yearBuilt = (1950..2024).random(),
                condition = (50..100).random(),
                region = (0..5).random(),
                isOccupied = Math.random() < 0.85,
                owner = "Owner_${i}",
                monthlyRent = value * 0.005 // 0.5% monthly
            ))
        }

        // Create housing markets for each region
        for (region in 0..5) {
            housingMarkets.add(HousingMarket(
                region = region,
                averagePrice = (500000.0 + region * 100000.0),
                priceChange = (-5.0..10.0).random(),
                inventory = (1000..5000).random(),
                salesVolume = (100..500).random(),
                rentalYield = (3.0..8.0).random(),
                vacancyRate = (5.0..20.0).random(),
                affordabilityIndex = (100.0 - region * 10.0)
            ))
        }

        updateStatistics()
    }

    private fun getRandomStreetName(): String {
        val names = listOf("Main", "Oak", "Maple", "Cedar", "Pine", "Elm", "Washington", "Lincoln", "Park", "Lake")
        val types = listOf("Street", "Avenue", "Boulevard", "Drive", "Lane", "Road", "Way", "Court")
        return "${names.random()} ${types.random()}"
    }

    fun constructDevelopment(name: String, type: PropertyType, units: Int, cost: Double, region: Int, country: Country): Boolean {
        if (country.treasury < cost) return false

        country.treasury -= cost

        val development = RealEstateDevelopment(
            id = developments.size + 1,
            name = name,
            type = type,
            totalUnits = units,
            soldUnits = 0,
            totalValue = cost * 1.3, // 30% profit margin
            completionDate = country.turn + 3,
            developer = "DevCorp_${developments.size + 1}",
            region = region
        )

        developments.add(development)

        // Economic boost
        country.gdp = country.gdp * 1.005 // 0.5% GDP boost
        country.infrastructure = (country.infrastructure + 1).coerceIn(0, 100)

        updateStatistics()
        return true
    }

    fun completeDevelopment(developmentId: Int, country: Country) {
        val development = developments.find { it.id == developmentId } ?: return

        // Add new properties
        for (i in 0 until development.totalUnits) {
            val property = Property(
                id = properties.size + 1,
                address = "${development.name} Unit ${i + 1}",
                type = development.type,
                housingType = if (development.type == PropertyType.RESIDENTIAL) HousingType.APARTMENT else null,
                value = development.totalValue / development.totalUnits,
                size = (50.0..200.0).random(),
                yearBuilt = country.year,
                condition = 100,
                region = development.region,
                isOccupied = false,
                owner = "Investor_${i}",
                monthlyRent = (development.totalValue / development.totalUnits) * 0.005
            )
            properties.add(property)
        }

        developments.remove(development)
        updateStatistics()
    }

    fun processHousingTurn(country: Country) {
        // Process property values
        properties.forEach { property ->
            // Value appreciation/depreciation
            val market = housingMarkets.find { it.region == property.region }
            val appreciation = market?.priceChange ?: 2.0

            property.value = property.value * (1.0 + appreciation / 100.0)

            // Condition degradation
            if (property.yearBuilt < country.year - 50) {
                property.condition = (property.condition - 1).coerceIn(0, 100)
            }

            // Tax collection
            val propertyTax = property.value * propertyTaxRate / 12 // Monthly
            country.treasury += propertyTax
        }

        // Process developments
        developments.forEach { dev ->
            if (country.turn >= dev.completionDate) {
                completeDevelopment(dev.id, country)
            }
        }

        // Update market data
        housingMarkets.forEach { market ->
            // Supply and demand
            val regionProperties = properties.filter { it.region == market.region }
            val occupied = regionProperties.count { it.isOccupied }
            val total = regionProperties.size

            market.vacancyRate = ((total - occupied).toDouble() / total * 100).coerceIn(0.0, 100.0)
            market.salesVolume = (total * 0.05).toInt() // 5% turnover

            // Price adjustments
            if (market.vacancyRate > 15) {
                market.priceChange = (market.priceChange - 1.0).coerceIn(-20.0, 20.0)
            } else if (market.vacancyRate < 5) {
                market.priceChange = (market.priceChange + 1.0).coerceIn(-20.0, 20.0)
            }

            market.averagePrice = regionProperties.map { it.value }.average()
        }

        // Rent control effects
        if (rentControlEnabled) {
            properties.filter { it.type == PropertyType.RESIDENTIAL }.forEach { property ->
                property.monthlyRent = (property.monthlyRent * (1.0 + rentControlLimit)).coerceAtMost(property.value * 0.008)
            }
        }

        updateStatistics()
    }

    fun setPropertyTaxRate(rate: Double, country: Country): String {
        propertyTaxRate = rate.coerceIn(0.0, 0.05) // 0-5%

        val revenue = properties.sumOf { it.value } * propertyTaxRate
        country.treasury += revenue

        if (propertyTaxRate > 0.03) {
            country.happiness = (country.happiness - 3).coerceIn(0, 100)
        }

        return "Property tax rate set to ${(propertyTaxRate * 100).toInt()}%"
    }

    fun buildPublicHousing(units: Int, cost: Double, region: Int, country: Country): Boolean {
        if (country.treasury < cost) return false

        country.treasury -= cost

        for (i in 0 until units) {
            properties.add(Property(
                id = properties.size + 1,
                address = "Public Housing Unit ${properties.size + 1}",
                type = PropertyType.RESIDENTIAL,
                housingType = HousingType.PUBLIC_HOUSING,
                value = cost / units,
                size = (40.0..80.0).random(),
                yearBuilt = country.year,
                condition = 100,
                region = region,
                isOccupied = true,
                owner = "Government",
                monthlyRent = 100.0 // Subsidized rent
            ))
        }

        statistics.publicHousingUnits += units
        statistics.homelessnessRate = (statistics.homelessnessRate - 0.1).coerceIn(0.0, 10.0)
        country.happiness = (country.happiness + 2).coerceIn(0, 100)

        return true
    }

    fun slumClearance(region: Int, cost: Double, country: Country): Boolean {
        if (country.treasury < cost) return false

        country.treasury -= cost

        val slums = properties.filter { it.housingType == HousingType.SLUM && it.region == region }
        slums.forEach { slum ->
            properties.remove(slum)
        }

        // Build replacement housing
        val newUnits = slums.size * 2
        for (i in 0 until newUnits) {
            properties.add(Property(
                id = properties.size + 1,
                address = "New Development Unit ${i + 1}",
                type = PropertyType.RESIDENTIAL,
                housingType = HousingType.APARTMENT,
                value = 200000.0,
                size = (50.0..100.0).random(),
                yearBuilt = country.year,
                condition = 100,
                region = region,
                isOccupied = true,
                owner = "Resident_${i}",
                monthlyRent = 500.0
            ))
        }

        statistics.slumPopulation = (statistics.slumPopulation - slums.size * 10).coerceAtLeast(0)
        country.happiness = (country.happiness + 5).coerceIn(0, 100)
        country.stability = (country.stability + 3).coerceIn(0, 100)

        return true
    }

    private fun updateStatistics() {
        statistics.totalProperties = properties.size
        statistics.totalValue = properties.sumOf { it.value }

        val residential = properties.filter { it.type == PropertyType.RESIDENTIAL }
        val owners = residential.distinctBy { it.owner }.count()
        statistics.homeownershipRate = (owners.toDouble() / residential.size * 100).coerceIn(0.0, 100.0)

        statistics.vacancyRate = (properties.count { !it.isOccupied }.toDouble() / properties.size * 100).coerceIn(0.0, 100.0)
        statistics.averageRent = residential.map { it.monthlyRent }.average()

        val avgIncome = 50000.0 // Simplified
        statistics.priceToIncomeRatio = residential.map { it.value }.average() / avgIncome

        statistics.slumPopulation = properties.count { it.housingType == HousingType.SLUM } * 10
    }

    fun getHousingReport(): String {
        val report = StringBuilder()
        report.append("=== HOUSING & REAL ESTATE REPORT ===\n\n")

        report.append("Housing Budget: $${String.format("%,d", (housingBudget / 1000000).toLong())}M\n")
        report.append("Property Tax Rate: ${(propertyTaxRate * 100).toInt()}%\n")
        report.append("Rent Control: ${if (rentControlEnabled) "Enabled (${(rentControlLimit * 100).toInt()}% max)" else "Disabled"}\n")
        report.append("Homeownership Rate: ${statistics.homeownershipRate.toInt()}%\n")
        report.append("Vacancy Rate: ${statistics.vacancyRate.toInt()}%\n")
        report.append("Price-to-Income Ratio: ${statistics.priceToIncomeRatio.toInt()}x\n")
        report.append("Homelessness Rate: ${statistics.homelessnessRate.toInt()}%\n\n")

        report.append("Housing Markets by Region:\n")
        housingMarkets.forEach { market ->
            report.append("- Region ${market.region}: Avg Price $${String.format("%,d", market.averagePrice.toLong())}\n")
            report.append("  Change: ${(market.priceChange).toInt()}% | Vacancy: ${market.vacancyRate.toInt()}%\n")
            report.append("  Affordability: ${market.affordabilityIndex.toInt()}/100\n\n")
        }

        report.append("Property Types:\n")
        PropertyType.values().forEach { type ->
            val count = properties.count { it.type == type }
            val value = properties.filter { it.type == type }.sumOf { it.value }
            report.append("- ${type}: ${count} properties, $${String.format("%,d", (value / 1000000).toLong())}M total value\n")
        }

        if (developments.isNotEmpty()) {
            report.append("\nActive Developments:\n")
            developments.forEach { dev ->
                report.append("- ${dev.name}: ${dev.soldUnits}/${dev.totalUnits} units sold\n")
            }
        }

        report.append("\n=== STATISTICS ===\n")
        report.append("Total Properties: ${statistics.totalProperties}\n")
        report.append("Total Value: $${String.format("%,d", (statistics.totalValue / 1000000).toLong())}M\n")
        report.append("Average Rent: $${statistics.averageRent.toInt()}/month\n")
        report.append("Public Housing: ${statistics.publicHousingUnits} units\n")
        report.append("Slum Population: ${statistics.slumPopulation}\n")

        return report.toString()
    }
}
