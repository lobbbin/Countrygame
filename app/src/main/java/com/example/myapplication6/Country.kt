package com.example.myapplication6

import java.io.Serializable

/**
 * Represents the player's country with all its attributes
 */
data class Country(
    var name: String = "New Nation",
    var population: Int = 1000000,
    var gdp: Double = 1000000000.0,
    var stability: Int = 50,
    var happiness: Int = 50,
    var military: Int = 50,
    var internationalRelations: Int = 50,
    var education: Int = 50,
    var healthcare: Int = 50,
    var infrastructure: Int = 50,
    var environment: Int = 50,
    var treasury: Double = 100000000.0,
    var year: Int = 2024,
    var turn: Int = 1,
    var governmentType: String = "Democracy",
    var leaderName: String = "Leader",
    var capitalCity: String = "Capital City"
) : Serializable {

    fun getGdpPerCapita(): Double = gdp / population
    
    fun getStabilityStatus(): String {
        return when {
            stability >= 80 -> "Excellent"
            stability >= 60 -> "Good"
            stability >= 40 -> "Moderate"
            stability >= 20 -> "Unstable"
            else -> "Critical"
        }
    }

    fun getHappinessStatus(): String {
        return when {
            happiness >= 80 -> "Thriving"
            happiness >= 60 -> "Content"
            happiness >= 40 -> "Neutral"
            happiness >= 20 -> "Unhappy"
            else -> "Rebellious"
        }
    }

    fun isGameOver(): Boolean {
        return stability <= 0 || happiness <= 0 || treasury <= -100000000
    }

    fun getGameOverReason(): String {
        return when {
            stability <= 0 -> "Your country has collapsed due to extreme instability!"
            happiness <= 0 -> "The people have overthrown you in a revolution!"
            treasury <= -100000000 -> "Your country has gone bankrupt!"
            else -> ""
        }
    }
}
