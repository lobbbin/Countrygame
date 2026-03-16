package com.example.myapplication6

import java.io.Serializable

/**
 * Represents an NPC (Non-Player Character) in the game
 */
data class NPC(
    val id: Int,
    val name: String,
    val role: NPCRole,
    val description: String,
    var relationship: Int = 50,  // 0-100, higher is better
    var influence: Int = 50,      // 0-100, how much power they have
    var mood: NPCMood = NPCMood.NEUTRAL,
    var isAlly: Boolean = true,
    val personality: Personality,
    var opinions: MutableMap<String, Int> = mutableMapOf(),  // Topics and opinions (-100 to 100)
    var quests: MutableList<Quest> = mutableListOf(),
    var dialogueHistory: MutableList<String> = mutableListOf()
) : Serializable {

    fun getRelationshipStatus(): String {
        return when {
            relationship >= 90 -> "Champion"
            relationship >= 70 -> "Ally"
            relationship >= 50 -> "Neutral"
            relationship >= 30 -> "Critical"
            else -> "Enemy"
        }
    }

    fun getMoodDescription(): String {
        return when (mood) {
            NPCMood.HAPPY -> "in a great mood"
            NPCMood.CONTENT -> "feeling content"
            NPCMood.NEUTRAL -> "neutral"
            NPCMood.CONCERNED -> "concerned about recent events"
            NPCMood.ANGRY -> "furious"
            NPCMood.DESPERATE -> "desperate"
        }
    }

    fun updateMood() {
        mood = when {
            relationship >= 80 -> NPCMood.HAPPY
            relationship >= 60 -> NPCMood.CONTENT
            relationship >= 40 -> NPCMood.NEUTRAL
            relationship >= 20 -> NPCMood.CONCERNED
            relationship >= 10 -> NPCMood.ANGRY
            else -> NPCMood.DESPERATE
        }
    }

    fun canOfferQuest(): Boolean {
        return relationship >= 40 && quests.any { !it.isCompleted }
    }

    fun getAvailableQuest(): Quest? {
        return quests.find { !it.isCompleted && !it.isFailed }
    }
}

enum class NPCRole {
    ADVISOR,
    GENERAL,
    DIPLOMAT,
    ECONOMIST,
    SCIENTIST,
    ACTIVIST,
    BUSINESS_LEADER,
    RELIGIOUS_LEADER,
    MEDIA_MOGL,
    FOREIGN_LEADER,
    REBEL_LEADER,
    CRIME_BOSS
}

enum class NPCMood {
    HAPPY,
    CONTENT,
    NEUTRAL,
    CONCERNED,
    ANGRY,
    DESPERATE
}

data class Personality(
    val trait1: PersonalityTrait,
    val trait2: PersonalityTrait,
    val motivation: String,
    val fear: String,
    val values: List<String>
) : Serializable

enum class PersonalityTrait {
    HONEST,
    DECEITFUL,
    AMBITIOUS,
    LAZY,
    COMPASSIONATE,
    RUTHLESS,
    OPTIMISTIC,
    PESSIMISTIC,
    PATRIOTIC,
    CORRUPT,
    IDEALISTIC,
    PRAGMATIC,
    AGGRESSIVE,
    PACIFIST,
    CONSERVATIVE,
    PROGRESSIVE
}

/**
 * Represents a quest that an NPC can offer
 */
data class Quest(
    val id: Int,
    val title: String,
    val description: String,
    val requirements: List<QuestRequirement>,
    val rewards: List<QuestReward>,
    val failureConsequences: List<QuestConsequence>,
    var isCompleted: Boolean = false,
    var isFailed: Boolean = false,
    var progress: Int = 0,
    val maxProgress: Int = 1,
    val turnsToComplete: Int = 3,
    var turnsRemaining: Int = 3
) : Serializable

data class QuestRequirement(
    val type: RequirementType,
    val value: Int,
    val description: String
) : Serializable

data class QuestReward(
    val type: RewardType,
    val value: Int,
    val description: String
) : Serializable

data class QuestConsequence(
    val type: ConsequenceType,
    val value: Int,
    val description: String
) : Serializable

enum class RequirementType {
    TREASURY,
    STABILITY,
    HAPPINESS,
    MILITARY,
    EDUCATION,
    HEALTHCARE,
    INFRASTRUCTURE,
    ENVIRONMENT,
    GDP,
    RELATIONSHIP,
    TURN_COUNT
}

enum class RewardType {
    TREASURY,
    STABILITY,
    HAPPINESS,
    MILITARY,
    EDUCATION,
    HEALTHCARE,
    INFRASTRUCTURE,
    ENVIRONMENT,
    GDP,
    RELATIONSHIP,
    INFLUENCE,
    UNLOCK_FEATURE
}

enum class ConsequenceType {
    TREASURY,
    STABILITY,
    HAPPINESS,
    RELATIONSHIP,
    INFLUENCE,
    QUEST_LOCK,
    GDP
}

/**
 * Manages all NPCs in the game world
 */
object NPCManager : Serializable {

    private val npcs = mutableListOf<NPC>()
    private val unlockedNPCs = mutableSetOf<Int>()

    fun initializeNPCs() {
        npcs.clear()
        unlockedNPCs.clear()

        // Core Advisors (always available)
        npcs.add(createChiefOfStaff())
        npcs.add(createGeneral())
        npcs.add(createEconomicAdvisor())
        npcs.add(createForeignMinister())

        // Unlockable NPCs
        npcs.add(createScientist())
        npcs.add(createActivist())
        npcs.add(createBusinessLeader())
        npcs.add(createReligiousLeader())
        npcs.add(createMediaMogul())
        npcs.add(createRebelLeader())
        npcs.add(createCrimeBoss())
        npcs.add(createForeignLeader())

        // Unlock core advisors
        unlockedNPCs.addAll(listOf(0, 1, 2, 3))
    }

    private fun createChiefOfStaff(): NPC {
        return NPC(
            id = 0,
            name = "Margaret Chen",
            role = NPCRole.ADVISOR,
            description = "Your Chief of Staff. Sharp, efficient, and fiercely loyal. She manages your office and filters information.",
            relationship = 70,
            influence = 80,
            personality = Personality(
                trait1 = PersonalityTrait.HONEST,
                trait2 = PersonalityTrait.PRAGMATIC,
                motivation = "Serve the nation effectively",
                fear = "Policy failures due to poor advice",
                values = listOf("Efficiency", "Loyalty", "Pragmatism")
            ),
            quests = mutableListOf(
                Quest(
                    id = 1,
                    title = "Streamline Government",
                    description = "Reduce bureaucratic inefficiency by improving coordination between departments.",
                    requirements = listOf(
                        QuestRequirement(RequirementType.STABILITY, 60, "Stability at 60%+")
                    ),
                    rewards = listOf(
                        QuestReward(RewardType.TREASURY, 20000000, "+$20M Treasury"),
                        QuestReward(RewardType.RELATIONSHIP, 15, "+15 Relationship with Margaret")
                    ),
                    failureConsequences = listOf(
                        QuestConsequence(ConsequenceType.RELATIONSHIP, -10, "-10 Relationship with Margaret")
                    ),
                    maxProgress = 1
                )
            )
        )
    }

    private fun createGeneral(): NPC {
        return NPC(
            id = 1,
            name = "General Marcus Stone",
            role = NPCRole.GENERAL,
            description = "Chairman of the Joint Chiefs. Decorated veteran with 40 years of service. Believes in strong defense.",
            relationship = 55,
            influence = 75,
            personality = Personality(
                trait1 = PersonalityTrait.PATRIOTIC,
                trait2 = PersonalityTrait.AGGRESSIVE,
                motivation = "Protect national security at all costs",
                fear = "Military weakness leading to attack",
                values = listOf("Strength", "Honor", "Duty")
            ),
            quests = mutableListOf(
                Quest(
                    id = 2,
                    title = "Modernize Armed Forces",
                    description = "Invest in modern military equipment and training programs.",
                    requirements = listOf(
                        QuestRequirement(RequirementType.TREASURY, 50000000, "50M available"),
                        QuestRequirement(RequirementType.MILITARY, 40, "Military at 40%+")
                    ),
                    rewards = listOf(
                        QuestReward(RewardType.MILITARY, 20, "+20 Military"),
                        QuestReward(RewardType.RELATIONSHIP, 15, "+15 Relationship with General Stone")
                    ),
                    failureConsequences = listOf(
                        QuestConsequence(ConsequenceType.RELATIONSHIP, -15, "-15 Relationship with General Stone")
                    ),
                    maxProgress = 2
                )
            )
        )
    }

    private fun createEconomicAdvisor(): NPC {
        return NPC(
            id = 2,
            name = "Dr. Elena Rodriguez",
            role = NPCRole.ECONOMIST,
            description = "Chief Economic Advisor. PhD from MIT, former World Bank economist. Data-driven and cautious.",
            relationship = 60,
            influence = 70,
            personality = Personality(
                trait1 = PersonalityTrait.PRAGMATIC,
                trait2 = PersonalityTrait.PESSIMISTIC,
                motivation = "Ensure long-term economic stability",
                fear = "Economic collapse or hyperinflation",
                values = listOf("Stability", "Evidence", "Fiscal Responsibility")
            ),
            quests = mutableListOf(
                Quest(
                    id = 3,
                    title = "Balance the Budget",
                    description = "Achieve a balanced budget through careful fiscal management.",
                    requirements = listOf(
                        QuestRequirement(RequirementType.GDP, 1500000000, "GDP of 1.5B+"),
                        QuestRequirement(RequirementType.TREASURY, 50000000, "Positive treasury flow")
                    ),
                    rewards = listOf(
                        QuestReward(RewardType.GDP, 100000000, "+$100M GDP"),
                        QuestReward(RewardType.RELATIONSHIP, 20, "+20 Relationship with Dr. Rodriguez")
                    ),
                    failureConsequences = listOf(
                        QuestConsequence(ConsequenceType.RELATIONSHIP, -10, "-10 Relationship with Dr. Rodriguez")
                    ),
                    maxProgress = 3
                )
            )
        )
    }

    private fun createForeignMinister(): NPC {
        return NPC(
            id = 3,
            name = "Ambassador James Okonkwo",
            role = NPCRole.DIPLOMAT,
            description = "Minister of Foreign Affairs. Career diplomat who has served in 12 countries. Master of protocol.",
            relationship = 65,
            influence = 65,
            personality = Personality(
                trait1 = PersonalityTrait.HONEST,
                trait2 = PersonalityTrait.PROGRESSIVE,
                motivation = "Build lasting international partnerships",
                fear = "International isolation",
                values = listOf("Cooperation", "Dialogue", "Mutual Respect")
            ),
            quests = mutableListOf(
                Quest(
                    id = 4,
                    title = "Strengthen Alliances",
                    description = "Improve international relations through diplomatic initiatives.",
                    requirements = listOf(
                        QuestRequirement(RequirementType.TREASURY, 20000000, "20M for diplomatic missions")
                    ),
                    rewards = listOf(
                        QuestReward(RewardType.TREASURY, -20000000, "Investment in diplomacy"),
                        QuestReward(RewardType.RELATIONSHIP, 15, "+15 Relationship with Ambassador Okonkwo"),
                        QuestReward(RewardType.INFLUENCE, 10, "+10 International Influence")
                    ),
                    failureConsequences = listOf(
                        QuestConsequence(ConsequenceType.RELATIONSHIP, -10, "-10 Relationship with Ambassador Okonkwo")
                    ),
                    maxProgress = 2
                )
            )
        )
    }

    private fun createScientist(): NPC {
        return NPC(
            id = 4,
            name = "Dr. Yuki Tanaka",
            role = NPCRole.SCIENTIST,
            description = "Director of National Research. Brilliant physicist leading innovation initiatives.",
            relationship = 40,
            influence = 50,
            personality = Personality(
                trait1 = PersonalityTrait.IDEALISTIC,
                trait2 = PersonalityTrait.OPTIMISTIC,
                motivation = "Advance human knowledge",
                fear = "Scientific stagnation",
                values = listOf("Innovation", "Truth", "Progress")
            ),
            quests = mutableListOf(
                Quest(
                    id = 5,
                    title = "Research Initiative",
                    description = "Fund a major scientific research program.",
                    requirements = listOf(
                        QuestRequirement(RequirementType.EDUCATION, 60, "Education at 60%+"),
                        QuestRequirement(RequirementType.TREASURY, 30000000, "30M research budget")
                    ),
                    rewards = listOf(
                        QuestReward(RewardType.EDUCATION, 15, "+15 Education"),
                        QuestReward(RewardType.GDP, 50000000, "+$50M GDP from innovation")
                    ),
                    failureConsequences = listOf(),
                    maxProgress = 2
                )
            )
        )
    }

    private fun createActivist(): NPC {
        return NPC(
            id = 5,
            name = "Maya Patel",
            role = NPCRole.ACTIVIST,
            description = "Leader of the People's Coalition. Grassroots organizer fighting for social justice.",
            relationship = 35,
            influence = 45,
            personality = Personality(
                trait1 = PersonalityTrait.COMPASSIONATE,
                trait2 = PersonalityTrait.PROGRESSIVE,
                motivation = "Fight for the marginalized",
                fear = "Systemic oppression continuing",
                values = listOf("Justice", "Equality", "Community")
            ),
            quests = mutableListOf(
                Quest(
                    id = 6,
                    title = "Social Reform",
                    description = "Implement policies to improve social welfare.",
                    requirements = listOf(
                        QuestRequirement(RequirementType.HAPPINESS, 50, "Happiness at 50%+"),
                        QuestRequirement(RequirementType.HEALTHCARE, 50, "Healthcare at 50%+")
                    ),
                    rewards = listOf(
                        QuestReward(RewardType.HAPPINESS, 20, "+20 Happiness"),
                        QuestReward(RewardType.RELATIONSHIP, 25, "+25 Relationship with Maya")
                    ),
                    failureConsequences = listOf(
                        QuestConsequence(ConsequenceType.HAPPINESS, -10, "-10 Happiness from protests")
                    ),
                    maxProgress = 2
                )
            )
        )
    }

    private fun createBusinessLeader(): NPC {
        return NPC(
            id = 6,
            name = "Robert Blackwood",
            role = NPCRole.BUSINESS_LEADER,
            description = "CEO of Blackwood Industries. One of the country's wealthiest individuals.",
            relationship = 45,
            influence = 70,
            personality = Personality(
                trait1 = PersonalityTrait.AMBITIOUS,
                trait2 = PersonalityTrait.PRAGMATIC,
                motivation = "Maximize profits and market share",
                fear = "Excessive regulation",
                values = listOf("Profit", "Efficiency", "Growth")
            ),
            quests = mutableListOf(
                Quest(
                    id = 7,
                    title = "Business Partnership",
                    description = "Create favorable conditions for business investment.",
                    requirements = listOf(
                        QuestRequirement(RequirementType.GDP, 1200000000, "GDP of 1.2B+"),
                        QuestRequirement(RequirementType.INFRASTRUCTURE, 50, "Infrastructure at 50%+")
                    ),
                    rewards = listOf(
                        QuestReward(RewardType.GDP, 100000000, "+$100M GDP"),
                        QuestReward(RewardType.TREASURY, 25000000, "+$25M Treasury from taxes")
                    ),
                    failureConsequences = listOf(
                        QuestConsequence(ConsequenceType.GDP, -20000000, "-$20M GDP from lost investment")
                    ),
                    maxProgress = 2
                )
            )
        )
    }

    private fun createReligiousLeader(): NPC {
        return NPC(
            id = 7,
            name = "Archbishop Thomas Moretti",
            role = NPCRole.RELIGIOUS_LEADER,
            description = "Head of the National Church. Spiritual leader with significant moral authority.",
            relationship = 50,
            influence = 60,
            personality = Personality(
                trait1 = PersonalityTrait.COMPASSIONATE,
                trait2 = PersonalityTrait.CONSERVATIVE,
                motivation = "Guide souls to righteousness",
                fear = "Moral decay of society",
                values = listOf("Faith", "Tradition", "Charity")
            ),
            quests = mutableListOf(
                Quest(
                    id = 8,
                    title = "Moral Initiative",
                    description = "Support faith-based community programs.",
                    requirements = listOf(
                        QuestRequirement(RequirementType.STABILITY, 50, "Stability at 50%+")
                    ),
                    rewards = listOf(
                        QuestReward(RewardType.STABILITY, 15, "+15 Stability"),
                        QuestReward(RewardType.HAPPINESS, 10, "+10 Happiness")
                    ),
                    failureConsequences = listOf(),
                    maxProgress = 1
                )
            )
        )
    }

    private fun createMediaMogul(): NPC {
        return NPC(
            id = 8,
            name = "Victoria Sterling",
            role = NPCRole.MEDIA_MOGL,
            description = "Owner of Sterling Media Group. Controls major news outlets and entertainment channels.",
            relationship = 40,
            influence = 75,
            personality = Personality(
                trait1 = PersonalityTrait.AMBITIOUS,
                trait2 = PersonalityTrait.DECEITFUL,
                motivation = "Shape public opinion",
                fear = "Loss of media influence",
                values = listOf("Influence", "Ratings", "Power")
            ),
            quests = mutableListOf(
                Quest(
                    id = 9,
                    title = "Media Campaign",
                    description = "Work with media to improve public perception.",
                    requirements = listOf(
                        QuestRequirement(RequirementType.TREASURY, 15000000, "15M for media budget")
                    ),
                    rewards = listOf(
                        QuestReward(RewardType.HAPPINESS, 15, "+15 Happiness through positive coverage"),
                        QuestReward(RewardType.STABILITY, 10, "+10 Stability")
                    ),
                    failureConsequences = listOf(
                        QuestConsequence(ConsequenceType.HAPPINESS, -10, "-10 Happiness from bad press")
                    ),
                    maxProgress = 1
                )
            )
        )
    }

    private fun createRebelLeader(): NPC {
        return NPC(
            id = 9,
            name = "Commander Alex Volkov",
            role = NPCRole.REBEL_LEADER,
            description = "Leader of the Freedom Front. Former military officer turned insurgent.",
            relationship = 15,
            influence = 40,
            isAlly = false,
            personality = Personality(
                trait1 = PersonalityTrait.IDEALISTIC,
                trait2 = PersonalityTrait.RUTHLESS,
                motivation = "Overthrow the current regime",
                fear = "Failed revolution",
                values = listOf("Revolution", "Freedom", "Justice")
            ),
            quests = mutableListOf(
                Quest(
                    id = 10,
                    title = "Peace Negotiations",
                    description = "Attempt to negotiate peace with rebel forces.",
                    requirements = listOf(
                        QuestRequirement(RequirementType.STABILITY, 40, "Stability at 40%+"),
                        QuestRequirement(RequirementType.MILITARY, 50, "Military at 50%+")
                    ),
                    rewards = listOf(
                        QuestReward(RewardType.STABILITY, 25, "+25 Stability from peace"),
                        QuestReward(RewardType.RELATIONSHIP, 30, "+30 Relationship with Volkov")
                    ),
                    failureConsequences = listOf(
                        QuestConsequence(ConsequenceType.STABILITY, -20, "-20 Stability from failed talks"),
                        QuestConsequence(ConsequenceType.RELATIONSHIP, -20, "-20 Relationship, conflict escalates")
                    ),
                    maxProgress = 3
                )
            )
        )
    }

    private fun createCrimeBoss(): NPC {
        return NPC(
            id = 10,
            name = "Vincent 'The Ghost' Marino",
            role = NPCRole.CRIME_BOSS,
            description = "Head of the Marino Syndicate. Operates from the shadows.",
            relationship = 20,
            influence = 50,
            isAlly = false,
            personality = Personality(
                trait1 = PersonalityTrait.CORRUPT,
                trait2 = PersonalityTrait.PRAGMATIC,
                motivation = "Expand criminal empire",
                fear = "Law enforcement crackdown",
                values = listOf("Loyalty", "Profit", "Silence")
            ),
            quests = mutableListOf(
                Quest(
                    id = 11,
                    title = "Underground Deal",
                    description = "Make a discreet arrangement with criminal elements.",
                    requirements = listOf(
                        QuestRequirement(RequirementType.TREASURY, 10000000, "10M for the deal")
                    ),
                    rewards = listOf(
                        QuestReward(RewardType.TREASURY, 30000000, "+$30M Treasury (illegal)"),
                        QuestReward(RewardType.RELATIONSHIP, 15, "+15 Relationship with Marino")
                    ),
                    failureConsequences = listOf(
                        QuestConsequence(ConsequenceType.STABILITY, -15, "-15 Stability from scandal"),
                        QuestConsequence(ConsequenceType.TREASURY, -20000000, "-$20M Treasury lost")
                    ),
                    maxProgress = 1
                )
            )
        )
    }

    private fun createForeignLeader(): NPC {
        return NPC(
            id = 11,
            name = "President Alexandra Petrov",
            role = NPCRole.FOREIGN_LEADER,
            description = "President of the neighboring superpower. Known for aggressive foreign policy.",
            relationship = 45,
            influence = 90,
            personality = Personality(
                trait1 = PersonalityTrait.AMBITIOUS,
                trait2 = PersonalityTrait.AGGRESSIVE,
                motivation = "Expand regional influence",
                fear = "Weakness on the world stage",
                values = listOf("Power", "National Interest", "Strength")
            ),
            quests = mutableListOf(
                Quest(
                    id = 12,
                    title = "Trade Agreement",
                    description = "Negotiate a bilateral trade deal.",
                    requirements = listOf(
                        QuestRequirement(RequirementType.GDP, 1000000000, "GDP of 1B+")
                    ),
                    rewards = listOf(
                        QuestReward(RewardType.GDP, 75000000, "+$75M GDP from trade"),
                        QuestReward(RewardType.RELATIONSHIP, 20, "+20 Relationship with President Petrov")
                    ),
                    failureConsequences = listOf(
                        QuestConsequence(ConsequenceType.RELATIONSHIP, -15, "-15 International Relations")
                    ),
                    maxProgress = 2
                )
            )
        )
    }

    fun getAllNPCs(): List<NPC> = npcs.toList()

    fun getNPCById(id: Int): NPC? = npcs.find { it.id == id }

    fun getNPCsByRole(role: NPCRole): List<NPC> = npcs.filter { it.role == role }

    fun getAvailableNPCs(): List<NPC> = npcs.filter { unlockedNPCs.contains(it.id) }

    fun unlockNPC(id: Int) {
        unlockedNPCs.add(id)
    }

    fun isNPCUnlocked(id: Int): Boolean = unlockedNPCs.contains(id)

    fun updateAllNPCMoods(country: Country) {
        npcs.forEach { npc ->
            // Adjust mood based on country stats related to their role
            when (npc.role) {
                NPCRole.GENERAL -> {
                    if (country.military < 30) npc.relationship -= 2
                    if (country.military > 70) npc.relationship += 1
                }
                NPCRole.ECONOMIST -> {
                    if (country.gdp < 500000000) npc.relationship -= 2
                    if (country.treasury < 0) npc.relationship -= 3
                }
                NPCRole.ACTIVIST -> {
                    if (country.happiness < 40) npc.relationship -= 2
                    if (country.stability < 40) npc.relationship += 2  // Opportunity for change
                }
                NPCRole.SCIENTIST -> {
                    if (country.education < 40) npc.relationship -= 2
                    if (country.education > 70) npc.relationship += 2
                }
                else -> {}
            }

            // Clamp relationship
            npc.relationship = npc.relationship.coerceIn(0, 100)
            npc.updateMood()
        }
    }

    fun getNPCAdvice(npc: NPC, country: Country): String {
        return when (npc.role) {
            NPCRole.ADVISOR -> {
                when {
                    country.stability < 40 -> "President, our stability is critically low. We need to address internal divisions immediately."
                    country.treasury < 0 -> "Sir/Ma'am, we're running a deficit. Spending cuts or revenue increases are necessary."
                    else -> "Everything is running smoothly. Focus on long-term strategic goals."
                }
            }
            NPCRole.GENERAL -> {
                when {
                    country.military < 40 -> "Our forces are underprepared. I recommend increased defense spending."
                    country.military > 70 -> "Our military is the envy of the world. We can project power if needed."
                    else -> "Defense readiness is adequate. Continue training exercises."
                }
            }
            NPCRole.ECONOMIST -> {
                when {
                    country.gdp < 800000000 -> "The economy needs stimulation. Consider infrastructure investment."
                    country.treasury > 200000000 -> "We have a healthy surplus. Consider debt reduction or strategic investments."
                    else -> "Economic indicators are stable. Maintain current fiscal policy."
                }
            }
            NPCRole.DIPLOMAT -> {
                when {
                    country.internationalRelations < 40 -> "Our international standing is poor. Diplomatic outreach is essential."
                    country.internationalRelations > 70 -> "We have strong alliances. Consider leading international initiatives."
                    else -> "Foreign relations are stable. Continue bilateral engagements."
                }
            }
            else -> "I have no specific advice at this time."
        }
    }

    fun processNPCTurns(country: Country) {
        npcs.forEach { npc ->
            // Process active quests
            npc.quests.forEach { quest ->
                if (!quest.isCompleted && !quest.isFailed) {
                    quest.turnsRemaining--
                    if (quest.turnsRemaining <= 0) {
                        // Check if quest is complete
                        val requirementsMet = quest.requirements.all { req ->
                            when (req.type) {
                                RequirementType.TREASURY -> country.treasury >= req.value
                                RequirementType.STABILITY -> country.stability >= req.value
                                RequirementType.HAPPINESS -> country.happiness >= req.value
                                RequirementType.MILITARY -> country.military >= req.value
                                RequirementType.EDUCATION -> country.education >= req.value
                                RequirementType.HEALTHCARE -> country.healthcare >= req.value
                                RequirementType.INFRASTRUCTURE -> country.infrastructure >= req.value
                                RequirementType.ENVIRONMENT -> country.environment >= req.value
                                RequirementType.GDP -> country.gdp >= req.value
                                RequirementType.RELATIONSHIP -> npc.relationship >= req.value
                                RequirementType.TURN_COUNT -> country.turn >= req.value
                            }
                        }

                        if (requirementsMet) {
                            quest.isCompleted = true
                            // Apply rewards
                            quest.rewards.forEach { reward ->
                                applyReward(country, npc, reward)
                            }
                        } else {
                            quest.isFailed = true
                            // Apply failure consequences
                            quest.failureConsequences.forEach { consequence ->
                                applyConsequence(country, npc, consequence)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun applyReward(country: Country, npc: NPC, reward: QuestReward) {
        when (reward.type) {
            RewardType.TREASURY -> country.treasury += reward.value.toDouble()
            RewardType.STABILITY -> country.stability = (country.stability + reward.value).coerceIn(0, 100)
            RewardType.HAPPINESS -> country.happiness = (country.happiness + reward.value).coerceIn(0, 100)
            RewardType.MILITARY -> country.military = (country.military + reward.value).coerceIn(0, 100)
            RewardType.EDUCATION -> country.education = (country.education + reward.value).coerceIn(0, 100)
            RewardType.HEALTHCARE -> country.healthcare = (country.healthcare + reward.value).coerceIn(0, 100)
            RewardType.INFRASTRUCTURE -> country.infrastructure = (country.infrastructure + reward.value).coerceIn(0, 100)
            RewardType.ENVIRONMENT -> country.environment = (country.environment + reward.value).coerceIn(0, 100)
            RewardType.GDP -> country.gdp = (country.gdp + reward.value.toDouble()).coerceAtLeast(0.0)
            RewardType.RELATIONSHIP -> npc.relationship = (npc.relationship + reward.value).coerceIn(0, 100)
            RewardType.INFLUENCE -> npc.influence = (npc.influence + reward.value).coerceIn(0, 100)
            RewardType.UNLOCK_FEATURE -> { /* Handle feature unlock */ }
        }
    }

    private fun applyConsequence(country: Country, npc: NPC, consequence: QuestConsequence) {
        when (consequence.type) {
            ConsequenceType.TREASURY -> country.treasury += consequence.value.toDouble()
            ConsequenceType.STABILITY -> country.stability = (country.stability + consequence.value).coerceIn(0, 100)
            ConsequenceType.HAPPINESS -> country.happiness = (country.happiness + consequence.value).coerceIn(0, 100)
            ConsequenceType.RELATIONSHIP -> npc.relationship = (npc.relationship + consequence.value).coerceIn(0, 100)
            ConsequenceType.INFLUENCE -> npc.influence = (npc.influence + consequence.value).coerceIn(0, 100)
            ConsequenceType.QUEST_LOCK -> { /* Handle quest lock */ }
            ConsequenceType.GDP -> country.gdp = (country.gdp + consequence.value.toDouble()).coerceAtLeast(0.0)
        }
    }

    fun checkNPCTriggerConditions(country: Country) {
        // Unlock NPCs based on game state
        if (country.education >= 60 && !isNPCUnlocked(4)) {
            unlockNPC(4)  // Unlock Scientist
        }
        if (country.happiness <= 40 && !isNPCUnlocked(5)) {
            unlockNPC(5)  // Unlock Activist
        }
        if (country.gdp >= 1500000000 && !isNPCUnlocked(6)) {
            unlockNPC(6)  // Unlock Business Leader
        }
        if (country.stability <= 30 && !isNPCUnlocked(9)) {
            unlockNPC(9)  // Unlock Rebel Leader
        }
    }
}
