package com.example.myapplication6

import java.io.Serializable

/**
 * Country Sim v3.0 - Transportation & Logistics System
 * Handles roads, railways, airports, ports, and public transit
 */

// Transportation mode
enum class TransportMode {
    ROAD,
    RAIL,
    AIR,
    SEA,
    PUBLIC_TRANSIT,
    CYCLING,
    WALKING,
    PIPELINE
}

// Infrastructure type
enum class InfrastructureType {
    HIGHWAY,
    LOCAL_ROAD,
    BRIDGE,
    TUNNEL,
    RAILWAY,
    SUBWAY,
    AIRPORT,
    SEAPORT,
    BUS_STATION,
    TRAIN_STATION,
    LOGISTICS_HUB,
    FUEL_STATION,
    CHARGING_STATION
}

// Transportation infrastructure
data class TransportInfrastructure(
    val id: Int,
    val name: String,
    val type: InfrastructureType,
    val mode: TransportMode,
    val capacity: Int,
    val usage: Int,
    val condition: Int, // 0-100
    val region: Int,
    val constructionCost: Double,
    val maintenanceCost: Double,
    val revenue: Double,
    val yearBuilt: Int
) : Serializable

// Vehicle fleet
data class VehicleFleet(
    val id: Int,
    val name: String,
    val type: TransportMode,
    val vehicleCount: Int,
    val averageAge: Double,
    val efficiency: Double, // 0-100
    val emissions: Double, // tons CO2 per year
    val operatingCost: Double
) : Serializable

// Logistics network
data class LogisticsNetwork(
    val id: Int,
    val name: String,
    val hubs: Int,
    val routes: Int,
    val efficiency: Double, // 0-100
    val capacity: Int,
    val utilization: Double, // 0-100
    val deliveryTime: Double // average days
) : Serializable

// Transportation statistics
data class TransportStatistics(
    var totalInfrastructure: Int = 0,
    var totalInvestment: Double = 0.0,
    var averageCondition: Double = 50.0,
    var publicTransitUsage: Double = 30.0,
    var freightVolume: Double = 0.0,
    var passengerVolume: Double = 0.0,
    var averageCommute: Double = 30.0, // minutes
    var trafficCongestion: Double = 50.0,
    var transportEmissions: Double = 0.0,
    var logisticsEfficiency: Double = 50.0
) : Serializable

// Object for transportation management
object TransportationManager : Serializable {

    val infrastructure = mutableListOf<TransportInfrastructure>()
    val fleets = mutableListOf<VehicleFleet>()
    val logisticsNetworks = mutableListOf<LogisticsNetwork>()
    val statistics = TransportStatistics()

    var transportBudget: Double = 200000000.0
    var infrastructureInvestment: Double = 100000000.0
    var publicTransitSubsidy: Double = 50000000.0
    var fuelTax: Double = 0.3 // per liter
    var electricVehicleIncentive: Double = 5000.0 // per vehicle

    fun initializeTransportation() {
        infrastructure.clear()
        fleets.clear()
        logisticsNetworks.clear()

        // Create starting infrastructure
        infrastructure.add(TransportInfrastructure(1, "National Highway System", InfrastructureType.HIGHWAY,
            TransportMode.ROAD, 10000000, 7000000, 70, 0, 500000000.0, 50000000.0, 30000000.0, 1960))
        infrastructure.add(TransportInfrastructure(2, "Capital Metro", InfrastructureType.SUBWAY,
            TransportMode.PUBLIC_TRANSIT, 2000000, 1500000, 75, 0, 200000000.0, 20000000.0, 15000000.0, 1980))
        infrastructure.add(TransportInfrastructure(3, "International Airport", InfrastructureType.AIRPORT,
            TransportMode.AIR, 50000000, 35000000, 80, 0, 300000000.0, 30000000.0, 50000000.0, 1970))
        infrastructure.add(TransportInfrastructure(4, "Main Seaport", InfrastructureType.SEAPORT,
            TransportMode.SEA, 100000000, 80000000, 75, 0, 400000000.0, 40000000.0, 80000000.0, 1950))
        infrastructure.add(TransportInfrastructure(5, "National Railway", InfrastructureType.RAILWAY,
            TransportMode.RAIL, 50000000, 30000000, 65, 0, 350000000.0, 35000000.0, 25000000.0, 1955))

        // Create vehicle fleets
        fleets.add(VehicleFleet(1, "Public Bus Fleet", TransportMode.PUBLIC_TRANSIT,
            5000, 8.5, 70, 500000.0, 100000000.0))
        fleets.add(VehicleFleet(2, "National Rail Fleet", TransportMode.RAIL,
            2000, 15.0, 65, 800000.0, 150000000.0))
        fleets.add(VehicleFleet(3, "Freight Truck Fleet", TransportMode.ROAD,
            50000, 10.0, 75, 2000000.0, 500000000.0))

        // Create logistics networks
        logisticsNetworks.add(LogisticsNetwork(1, "National Freight Network",
            50, 200, 70, 100000000, 75.0, 3.5))
        logisticsNetworks.add(LogisticsNetwork(2, "Express Delivery Network",
            30, 150, 85, 50000000, 80.0, 1.5))

        updateStatistics()
    }

    fun buildInfrastructure(name: String, type: InfrastructureType, mode: TransportMode,
                           cost: Double, region: Int, country: Country): Boolean {
        if (country.treasury < cost) return false

        country.treasury -= cost

        val infra = TransportInfrastructure(
            id = infrastructure.size + 1,
            name = name,
            type = type,
            mode = mode,
            capacity = (cost / 10000.0).toInt(),
            usage = 0,
            condition = 100,
            region = region,
            constructionCost = cost,
            maintenanceCost = cost * 0.05, // 5% annual
            revenue = 0.0,
            yearBuilt = country.year
        )

        infrastructure.add(infra)

        // Apply effects
        country.gdp = country.gdp * 1.01 // 1% GDP boost
        country.infrastructure = (country.infrastructure + 2).coerceIn(0, 100)

        updateStatistics()
        return true
    }

    fun upgradeInfrastructure(infraId: Int, upgradeCost: Double, country: Country): Boolean {
        val infra = infrastructure.find { it.id == infraId }
        if (infra == null || country.treasury < upgradeCost) return false

        country.treasury -= upgradeCost
        infra.condition = (infra.condition + 20).coerceIn(0, 100)
        infra.capacity = (infra.capacity * 1.3).toInt()

        updateStatistics()
        return true
    }

    fun maintainInfrastructure(country: Country): Double {
        val totalMaintenance = infrastructure.sumOf { it.maintenanceCost }
        if (country.treasury >= totalMaintenance) {
            country.treasury -= totalMaintenance

            // Prevent degradation
            infrastructure.forEach { infra ->
                infra.condition = (infra.condition - 1 + 5).coerceIn(0, 100) // Net +4 with maintenance
            }

            return totalMaintenance
        }

        // Partial maintenance if insufficient funds
        val partialMaintenance = country.treasury * 0.8
        country.treasury -= partialMaintenance

        infrastructure.forEach { infra ->
            infra.condition = (infra.condition - 2).coerceIn(0, 100) // Degradation
        }

        return partialMaintenance
    }

    fun processTransportTurn(country: Country) {
        // Process infrastructure usage and revenue
        infrastructure.forEach { infra ->
            // Calculate usage based on capacity and economic activity
            val usageRate = when (infra.mode) {
                TransportMode.ROAD -> 0.7 + (country.gdp / 10000000000.0) * 0.1
                TransportMode.RAIL -> 0.6 + (country.gdp / 10000000000.0) * 0.08
                TransportMode.AIR -> 0.5 + (country.gdp / 10000000000.0) * 0.15
                TransportMode.SEA -> 0.8 + (country.gdp / 10000000000.0) * 0.05
                TransportMode.PUBLIC_TRANSIT -> 0.75 + (country.happiness / 200.0)
                else -> 0.5
            }

            infra.usage = (infra.capacity * usageRate).toInt()
            infra.revenue = infra.usage * 0.5 // $0.5 per unit

            // Tax revenue
            country.treasury += infra.revenue * 0.15 // 15% tax
        }

        // Process fleets
        fleets.forEach { fleet ->
            // Update efficiency based on age
            fleet.efficiency = (100.0 - fleet.averageAge * 2).coerceIn(30.0, 100.0)

            // Update emissions
            fleet.emissions = fleet.vehicleCount * (100.0 - fleet.efficiency / 2.0)

            // Operating costs
            country.treasury -= fleet.operatingCost * 0.1 // 10% per turn
        }

        // Update logistics efficiency
        logisticsNetworks.forEach { network ->
            network.utilization = (network.capacity * 0.7 / network.capacity * 100).coerceIn(0.0, 100.0)
            network.efficiency = (network.efficiency + 0.5).coerceIn(0.0, 100.0)
        }

        // Calculate congestion
        val roadUsage = infrastructure.filter { it.mode == TransportMode.ROAD }.sumOf { it.usage }
        val roadCapacity = infrastructure.filter { it.mode == TransportMode.ROAD }.sumOf { it.capacity }
        statistics.trafficCongestion = ((roadUsage.toDouble() / roadCapacity) * 100).coerceIn(0.0, 100.0)

        // Economic impact
        val avgCondition = infrastructure.map { it.condition }.average()
        if (avgCondition < 50) {
            country.gdp = country.gdp * 0.99 // 1% GDP loss from poor infrastructure
        } else if (avgCondition > 80) {
            country.gdp = country.gdp * 1.005 // 0.5% GDP gain from excellent infrastructure
        }

        updateStatistics()
    }

    fun setFuelTax(rate: Double, country: Country): String {
        fuelTax = rate.coerceIn(0.0, 1.0)

        val revenue = fuelTax * 100000000 // Simplified revenue calculation
        country.treasury += revenue

        if (fuelTax > 0.5) {
            country.happiness = (country.happiness - 5).coerceIn(0, 100)
            country.environment = (country.environment + 3).coerceIn(0, 100)
        } else if (fuelTax < 0.2) {
            country.happiness = (country.happiness + 3).coerceIn(0, 100)
            country.environment = (country.environment - 2).coerceIn(0, 100)
        }

        return "Fuel tax set to ${(fuelTax * 100).toInt()}%"
    }

    fun promoteElectricVehicles(incentive: Double, country: Country): Boolean {
        if (country.treasury < incentive * 10000) return false

        country.treasury -= incentive * 10000
        electricVehicleIncentive = incentive

        // Environmental benefit
        country.environment = (country.environment + 5).coerceIn(0, 100)

        // Reduce emissions
        fleets.forEach { fleet ->
            if (fleet.type == TransportMode.ROAD) {
                fleet.emissions = fleet.emissions * 0.9 // 10% reduction
            }
        }

        return true
    }

    private fun updateStatistics() {
        statistics.totalInfrastructure = infrastructure.size
        statistics.totalInvestment = infrastructure.sumOf { it.constructionCost }
        statistics.averageCondition = infrastructure.map { it.condition }.average()
        statistics.publicTransitUsage = infrastructure.filter { it.mode == TransportMode.PUBLIC_TRANSIT }
            .sumOf { it.usage }.toDouble() / 1000000
        statistics.freightVolume = logisticsNetworks.sumOf { it.capacity }.toDouble()
        statistics.passengerVolume = infrastructure.filter { it.mode == TransportMode.PUBLIC_TRANSIT || it.mode == TransportMode.RAIL }
            .sumOf { it.usage }.toDouble()
        statistics.transportEmissions = fleets.sumOf { it.emissions }
        statistics.logisticsEfficiency = logisticsNetworks.map { it.efficiency }.average()
    }

    fun getTransportReport(): String {
        val report = StringBuilder()
        report.append("=== TRANSPORTATION & LOGISTICS REPORT ===\n\n")

        report.append("Transport Budget: $${String.format("%,d", (transportBudget / 1000000).toLong())}M\n")
        report.append("Infrastructure Investment: $${String.format("%,d", (infrastructureInvestment / 1000000).toLong())}M\n")
        report.append("Fuel Tax: ${(fuelTax * 100).toInt()}%\n")
        report.append("Traffic Congestion: ${statistics.trafficCongestion.toInt()}%\n")
        report.append("Average Commute: ${statistics.averageCommute.toInt()} min\n\n")

        report.append("Infrastructure (${infrastructure.size}):\n")
        infrastructure.forEach { infra ->
            report.append("- ${infra.name} (${infra.type})\n")
            report.append("  Mode: ${infra.mode} | Condition: ${infra.condition}%\n")
            report.append("  Capacity: ${infra.capacity / 1000}K | Usage: ${infra.usage / 1000}K\n")
            report.append("  Revenue: $${String.format("%,d", (infra.revenue / 1000000).toLong())}M\n\n")
        }

        report.append("Vehicle Fleets (${fleets.size}):\n")
        fleets.forEach { fleet ->
            report.append("- ${fleet.name}\n")
            report.append("  Vehicles: ${fleet.vehicleCount} | Age: ${fleet.averageAge} years\n")
            report.append("  Efficiency: ${fleet.efficiency.toInt()}% | Emissions: ${fleet.emissions.toInt()} tons\n\n")
        }

        report.append("Logistics Networks (${logisticsNetworks.size}):\n")
        logisticsNetworks.forEach { network ->
            report.append("- ${network.name}\n")
            report.append("  Hubs: ${network.hubs} | Routes: ${network.routes}\n")
            report.append("  Efficiency: ${network.efficiency.toInt()}% | Delivery: ${network.deliveryTime} days\n\n")
        }

        report.append("=== STATISTICS ===\n")
        report.append("Total Investment: $${String.format("%,d", (statistics.totalInvestment / 1000000).toLong())}M\n")
        report.append("Average Condition: ${statistics.averageCondition.toInt()}%\n")
        report.append("Annual Emissions: ${statistics.transportEmissions.toInt()} tons CO2\n")
        report.append("Logistics Efficiency: ${statistics.logisticsEfficiency.toInt()}%\n")

        return report.toString()
    }
}
