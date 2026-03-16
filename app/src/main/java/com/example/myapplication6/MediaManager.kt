package com.example.myapplication6

import java.io.Serializable

/**
 * Country Sim v3.0 - Media & Propaganda System
 * Controls news media, public opinion, and information warfare
 */

// Media types
enum class MediaType {
    NEWSPAPER,
    TELEVISION,
    RADIO,
    ONLINE_NEWS,
    SOCIAL_MEDIA,
    STATE_MEDIA,
    INDEPENDENT,
    FOREIGN_MEDIA
}

// News channel
data class NewsChannel(
    val id: Int,
    val name: String,
    val type: MediaType,
    var audience: Int,
    var credibility: Int,
    var politicalBias: Int, // -100 (left) to 100 (right)
    var isStateControlled: Boolean = false,
    var isBanned: Boolean = false,
    val owner: String,
    val foundingYear: Int
) : Serializable

// News article
data class NewsArticle(
    val id: Int,
    val headline: String,
    val content: String,
    val channelId: Int,
    val sentiment: Double, // -1.0 (negative) to 1.0 (positive)
    val topics: List<String>,
    val publishedTurn: Int,
    var isPublished: Boolean = false
) : Serializable

// Propaganda campaign
data class PropagandaCampaign(
    val id: Int,
    val name: String,
    val description: String,
    val target: PropagandaTarget,
    val cost: Double,
    val duration: Int,
    val effectiveness: Double,
    val risk: Double,
    var progress: Int = 0,
    var isActive: Boolean = false,
    var isComplete: Boolean = false,
    var isSuccess: Boolean = false
) : Serializable

enum class PropagandaTarget {
    PUBLIC_SUPPORT,
    WAR_SUPPORT,
    RECRUITMENT,
    DISSENT_SUPPRESSION,
    INTERNATIONAL_IMAGE,
    ENEMY_MORALE,
    ELECTION_INFLUENCE,
    POLICY_SUPPORT
}

// Press freedom status
enum class PressFreedomLevel {
    COMPLETE_FREEDOM,
    MOSTLY_FREE,
    PARTIALLY_FREE,
    MOSTLY_UNFREE,
    COMPLETELY_UNFREE,
    STATE_CENSORSHIP
}

// Media statistics
data class MediaStatistics(
    var totalChannels: Int = 0,
    var stateControlledChannels: Int = 0,
    var bannedChannels: Int = 0,
    var averageCredibility: Double = 50.0,
    var pressFreedomIndex: Double = 50.0,
    var propagandaSuccessRate: Double = 50.0,
    var publicTrustInMedia: Double = 50.0,
    var socialMediaPenetration: Double = 50.0,
    var internetPenetration: Double = 50.0
) : Serializable

// Object for media management
object MediaManager : Serializable {

    val newsChannels = mutableListOf<NewsChannel>()
    val activeArticles = mutableListOf<NewsArticle>()
    val propagandaCampaigns = mutableListOf<PropagandaCampaign>()
    val statistics = MediaStatistics()

    var pressFreedomLevel: PressFreedomLevel = PressFreedomLevel.MOSTLY_FREE
    var censorshipLevel: Int = 20 // 0-100
    var mediaLiteracy: Int = 50
    var statePropagandaLevel: Int = 30

    fun initializeMedia() {
        newsChannels.clear()
        activeArticles.clear()
        propagandaCampaigns.clear()

        // Create starting news channels
        newsChannels.add(NewsChannel(1, "National News Network", MediaType.TELEVISION,
            5000000, 75, 10, false, false, "MediaCorp Inc.", 1985))
        newsChannels.add(NewsChannel(2, "The Daily Herald", MediaType.NEWSPAPER,
            1000000, 85, 0, false, false, "Herald Publishing", 1920))
        newsChannels.add(NewsChannel(3, "State Television", MediaType.TELEVISION,
            8000000, 60, 50, true, false, "Government", 1950))
        newsChannels.add(NewsChannel(4, "Independent Radio", MediaType.RADIO,
            2000000, 70, -20, false, false, "Independent Collective", 1995))
        newsChannels.add(NewsChannel(5, "Social News Online", MediaType.ONLINE_NEWS,
            10000000, 45, 30, false, false, "TechMedia Ltd.", 2010))
        newsChannels.add(NewsChannel(6, "Global News Service", MediaType.FOREIGN_MEDIA,
            3000000, 80, 0, false, false, "International Corp.", 1975))

        updateStatistics()
    }

    fun createNewsChannel(name: String, type: MediaType, owner: String, cost: Double, country: Country): Boolean {
        if (country.treasury < cost) return false

        country.treasury -= cost

        val channel = NewsChannel(
            id = newsChannels.size + 1,
            name = name,
            type = type,
            audience = (1000000..5000000).random(),
            credibility = (50..90).random(),
            politicalBias = (-50..50).random(),
            isStateControlled = false,
            isBanned = false,
            owner = owner,
            foundingYear = country.year
        )

        newsChannels.add(channel)
        updateStatistics()

        return true
    }

    fun banChannel(channelId: Int, country: Country): Boolean {
        val channel = newsChannels.find { it.id == channelId }
        if (channel == null || channel.isBanned) return false

        channel.isBanned = true
        country.stability = (country.stability + 2).coerceIn(0, 100)
        country.happiness = (country.happiness - 3).coerceIn(0, 100)

        updatePressFreedom()
        updateStatistics()

        return true
    }

    fun nationalizeChannel(channelId: Int, cost: Double, country: Country): Boolean {
        val channel = newsChannels.find { it.id == channelId }
        if (channel == null || channel.isStateControlled) return false

        if (country.treasury < cost) return false

        country.treasury -= cost
        channel.isStateControlled = true
        channel.politicalBias = (channel.politicalBias + 50).coerceIn(-100, 100)

        updateStatistics()

        return true
    }

    fun startPropagandaCampaign(campaign: PropagandaCampaign, country: Country): String {
        if (country.treasury < campaign.cost) {
            return "Insufficient funds!"
        }

        country.treasury -= campaign.cost
        campaign.isActive = true
        propagandaCampaigns.add(campaign)

        return "Propaganda campaign '${campaign.name}' started!"
    }

    fun processPropagandaTurn(country: Country): List<String> {
        val results = mutableListOf<String>()
        val campaignsToRemove = mutableListOf<PropagandaCampaign>()

        propagandaCampaigns.forEach { campaign ->
            if (campaign.isActive) {
                campaign.progress++

                if (campaign.progress >= campaign.duration) {
                    campaign.isActive = false
                    campaign.isComplete = true

                    val roll = Math.random() * 100
                    campaign.isSuccess = roll < (campaign.effectiveness * 100)

                    if (campaign.isSuccess) {
                        applyPropagandaSuccess(campaign, country)
                        results.add("✓ ${campaign.name}: SUCCESS")
                    } else {
                        applyPropagandaFailure(campaign, country)
                        results.add("✗ ${campaign.name}: FAILED")
                    }

                    campaignsToRemove.add(campaign)
                }
            }
        }

        propagandaCampaigns.removeAll(campaignsToRemove)
        return results
    }

    private fun applyPropagandaSuccess(campaign: PropagandaCampaign, country: Country) {
        when (campaign.target) {
            PropagandaTarget.PUBLIC_SUPPORT -> country.happiness = (country.happiness + 10).coerceIn(0, 100)
            PropagandaTarget.WAR_SUPPORT -> {
                country.military = (country.military + 10).coerceIn(0, 100)
                country.stability = (country.stability + 5).coerceIn(0, 100)
            }
            PropagandaTarget.RECRUITMENT -> country.military = (country.military + 15).coerceIn(0, 100)
            PropagandaTarget.DISSENT_SUPPRESSION -> country.stability = (country.stability + 15).coerceIn(0, 100)
            PropagandaTarget.INTERNATIONAL_IMAGE -> country.internationalRelations = (country.internationalRelations + 10).coerceIn(0, 100)
            PropagandaTarget.ENEMY_MORALE -> country.internationalRelations = (country.internationalRelations - 10).coerceIn(0, 100)
            PropagandaTarget.ELECTION_INFLUENCE -> country.stability = (country.stability + 8).coerceIn(0, 100)
            PropagandaTarget.POLICY_SUPPORT -> country.stability = (country.stability + 12).coerceIn(0, 100)
        }
    }

    private fun applyPropagandaFailure(campaign: PropagandaCampaign, country: Country) {
        when (campaign.target) {
            PropagandaTarget.PUBLIC_SUPPORT -> country.happiness = (country.happiness - 5).coerceIn(0, 100)
            PropagandaTarget.WAR_SUPPORT -> country.stability = (country.stability - 5).coerceIn(0, 100)
            PropagandaTarget.RECRUITMENT -> country.military = (country.military - 5).coerceIn(0, 100)
            PropagandaTarget.DISSENT_SUPPRESSION -> {
                country.stability = (country.stability - 10).coerceIn(0, 100)
                country.happiness = (country.happiness - 5).coerceIn(0, 100)
            }
            PropagandaTarget.INTERNATIONAL_IMAGE -> country.internationalRelations = (country.internationalRelations - 5).coerceIn(0, 100)
            PropagandaTarget.ENEMY_MORALE -> country.internationalRelations = (country.internationalRelations + 5).coerceIn(0, 100)
            PropagandaTarget.ELECTION_INFLUENCE -> {
                country.stability = (country.stability - 8).coerceIn(0, 100)
                country.happiness = (country.happiness - 5).coerceIn(0, 100)
            }
            PropagandaTarget.POLICY_SUPPORT -> country.stability = (country.stability - 5).coerceIn(0, 100)
        }
    }

    fun generateNewsArticle(country: Country): NewsArticle {
        val headlines = listOf(
            "Government Announces New Economic Plan",
            "Opposition Criticizes Leadership",
            "International Summit Scheduled",
            "Economic Growth Exceeds Expectations",
            "Protests Erupt Over New Policy",
            "Military Parade Showcases Strength",
            "Healthcare Reform Proposed",
            "Education Budget Increased",
            "Infrastructure Project Completed",
            "Environmental Regulations Tightened"
        )

        val sentiments = listOf(-0.5, -0.3, -0.1, 0.0, 0.1, 0.3, 0.5, 0.7)
        val topics = listOf("Economy", "Politics", "Military", "Healthcare", "Education", "Environment", "Foreign Policy")

        val channel = newsChannels.filter { !it.isBanned }.randomOrNull() ?: newsChannels.first()

        val article = NewsArticle(
            id = activeArticles.size + 1,
            headline = headlines.random(),
            content = "News content about current events...",
            channelId = channel.id,
            sentiment = sentiments.random(),
            topics = listOf(topics.random()),
            publishedTurn = country.turn,
            isPublished = true
        )

        activeArticles.add(article)

        // Apply sentiment effect based on channel reach
        val effect = article.sentiment * (channel.audience / 10000000.0) * 0.5
        country.happiness = (country.happiness + (effect * 5).toInt()).coerceIn(0, 100)

        return article
    }

    fun updatePressFreedom() {
        pressFreedomLevel = when {
            censorshipLevel < 20 -> PressFreedomLevel.COMPLETE_FREEDOM
            censorshipLevel < 40 -> PressFreedomLevel.MOSTLY_FREE
            censorshipLevel < 60 -> PressFreedomLevel.PARTIALLY_FREE
            censorshipLevel < 80 -> PressFreedomLevel.MOSTLY_UNFREE
            censorshipLevel < 95 -> PressFreedomLevel.COMPLETELY_UNFREE
            else -> PressFreedomLevel.STATE_CENSORSHIP
        }
    }

    fun setCensorshipLevel(level: Int, country: Country) {
        censorshipLevel = level.coerceIn(0, 100)
        updatePressFreedom()

        when {
            level < 30 -> {
                country.happiness = (country.happiness + 5).coerceIn(0, 100)
                country.stability = (country.stability - 3).coerceIn(0, 100)
            }
            level > 70 -> {
                country.stability = (country.stability + 10).coerceIn(0, 100)
                country.happiness = (country.happiness - 10).coerceIn(0, 100)
                country.internationalRelations = (country.internationalRelations - 15).coerceIn(0, 100)
            }
        }
    }

    private fun updateStatistics() {
        statistics.totalChannels = newsChannels.size
        statistics.stateControlledChannels = newsChannels.count { it.isStateControlled }
        statistics.bannedChannels = newsChannels.count { it.isBanned }
        statistics.averageCredibility = newsChannels.map { it.credibility }.average()
        statistics.pressFreedomIndex = 100.0 - censorshipLevel
        statistics.publicTrustInMedia = statistics.averageCredibility * (1.0 - censorshipLevel / 200.0)
    }

    fun getMediaReport(): String {
        val report = StringBuilder()
        report.append("=== MEDIA REPORT ===\n\n")

        report.append("Press Freedom: $pressFreedomLevel\n")
        report.append("Censorship Level: $censorshipLevel%\n")
        report.append("Media Literacy: $mediaLiteracy%\n")
        report.append("State Propaganda: $statePropagandaLevel%\n\n")

        report.append("News Channels (${newsChannels.size}):\n")
        newsChannels.forEach { channel ->
            val status = when {
                channel.isBanned -> "⛔ Banned"
                channel.isStateControlled -> "🏛️ State"
                else -> "✓ Independent"
            }
            report.append("- ${channel.name} ($status)\n")
            report.append("  Type: ${channel.type} | Audience: ${channel.audience / 1000}K\n")
            report.append("  Credibility: ${channel.credibility}% | Bias: ${channel.politicalBias}\n\n")
        }

        if (propagandaCampaigns.isNotEmpty()) {
            report.append("Active Campaigns:\n")
            propagandaCampaigns.forEach { campaign ->
                report.append("- ${campaign.name}: ${campaign.progress}/${campaign.duration}\n")
            }
        }

        report.append("\n=== STATISTICS ===\n")
        report.append("Average Credibility: ${statistics.averageCredibility.toInt()}%\n")
        report.append("Public Trust: ${statistics.publicTrustInMedia.toInt()}%\n")

        return report.toString()
    }

    fun getAvailablePropagandaCampaigns(): List<PropagandaCampaign> {
        return listOf(
            PropagandaCampaign(1, "Patriotic Unity", "Promote national unity and pride",
                PropagandaTarget.PUBLIC_SUPPORT, 20000000.0, 5, 0.7, 0.2),
            PropagandaCampaign(2, "War Effort", "Boost support for military action",
                PropagandaTarget.WAR_SUPPORT, 30000000.0, 8, 0.65, 0.3),
            PropagandaCampaign(3, "Serve Your Country", "Military recruitment drive",
                PropagandaTarget.RECRUITMENT, 25000000.0, 6, 0.75, 0.15),
            PropagandaCampaign(4, "Silence Dissent", "Suppress opposition voices",
                PropagandaTarget.DISSENT_SUPPRESSION, 15000000.0, 4, 0.6, 0.4),
            PropagandaCampaign(5, "Global Image", "Improve international reputation",
                PropagandaTarget.INTERNATIONAL_IMAGE, 35000000.0, 10, 0.55, 0.2),
            PropagandaCampaign(6, "Election Influence", "Sway public opinion before election",
                PropagandaTarget.ELECTION_INFLUENCE, 40000000.0, 7, 0.6, 0.35),
            PropagandaCampaign(7, "Policy Support", "Build support for new legislation",
                PropagandaTarget.POLICY_SUPPORT, 18000000.0, 5, 0.7, 0.25)
        )
    }
}
