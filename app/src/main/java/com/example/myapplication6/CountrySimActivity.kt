package com.example.myapplication6

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class CountrySimActivity : AppCompatActivity() {

    private lateinit var country: Country
    private var currentEvent: GameEvent? = null

    // UI Components - Header
    private lateinit var txtCountryName: TextView
    private lateinit var txtYear: TextView
    private lateinit var txtTurn: TextView
    private lateinit var txtLeaderName: TextView
    private lateinit var txtCapitalCity: TextView
    private lateinit var txtGovernmentType: TextView

    // UI Components - Stats
    private lateinit var txtPopulation: TextView
    private lateinit var txtGDP: TextView
    private lateinit var txtTreasury: TextView
    private lateinit var txtStability: TextView
    private lateinit var txtHappiness: TextView
    private lateinit var txtMilitary: TextView
    private lateinit var txtInternationalRelations: TextView
    private lateinit var txtEducation: TextView
    private lateinit var txtHealthcare: TextView
    private lateinit var txtInfrastructure: TextView
    private lateinit var txtEnvironment: TextView

    // UI Components - Event
    private lateinit var txtEventTitle: TextView
    private lateinit var txtEventDescription: TextView
    private lateinit var btnChoice1: Button
    private lateinit var btnChoice2: Button
    private lateinit var btnChoice3: Button
    private lateinit var btnNextTurn: Button
    private lateinit var btnNewGame: Button
    private lateinit var btnNPCs: Button
    private lateinit var btnWorldStatus: Button
    private lateinit var btnRegions: Button
    private lateinit var btnEconomy: Button
    private lateinit var btnPolicies: Button
    private lateinit var btnTechnology: Button

    private lateinit var layoutEvent: LinearLayout
    private lateinit var layoutStats: ScrollView
    private lateinit var layoutActions: LinearLayout

    private val decimalFormat = DecimalFormat("#,###")
    private val currencyFormat = DecimalFormat("$#,###,###,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_country_sim)

            initializeViews()
            setupClickListeners()

            // Load saved game or start new game
            if (savedInstanceState != null) {
                country = savedInstanceState.getSerializable("country") as Country
            } else {
                startNewGame()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Crash: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("country", country)
    }

    private fun initializeViews() {
        txtCountryName = findViewById(R.id.txtCountryName)
        txtYear = findViewById(R.id.txtYear)
        txtTurn = findViewById(R.id.txtTurn)
        txtLeaderName = findViewById(R.id.txtLeaderName)
        txtCapitalCity = findViewById(R.id.txtCapitalCity)
        txtGovernmentType = findViewById(R.id.txtGovernmentType)

        txtPopulation = findViewById(R.id.txtPopulation)
        txtGDP = findViewById(R.id.txtGDP)
        txtTreasury = findViewById(R.id.txtTreasury)
        txtStability = findViewById(R.id.txtStability)
        txtHappiness = findViewById(R.id.txtHappiness)
        txtMilitary = findViewById(R.id.txtMilitary)
        txtInternationalRelations = findViewById(R.id.txtInternationalRelations)
        txtEducation = findViewById(R.id.txtEducation)
        txtHealthcare = findViewById(R.id.txtHealthcare)
        txtInfrastructure = findViewById(R.id.txtInfrastructure)
        txtEnvironment = findViewById(R.id.txtEnvironment)

        txtEventTitle = findViewById(R.id.txtEventTitle)
        txtEventDescription = findViewById(R.id.txtEventDescription)
        btnChoice1 = findViewById(R.id.btnChoice1)
        btnChoice2 = findViewById(R.id.btnChoice2)
        btnChoice3 = findViewById(R.id.btnChoice3)
        btnNextTurn = findViewById(R.id.btnNextTurn)
        btnNewGame = findViewById(R.id.btnNewGame)
        btnNPCs = findViewById(R.id.btnNPCs)
        btnWorldStatus = findViewById(R.id.btnWorldStatus)
        btnRegions = findViewById(R.id.btnRegions)
        btnEconomy = findViewById(R.id.btnEconomy)
        btnPolicies = findViewById(R.id.btnPolicies)
        btnTechnology = findViewById(R.id.btnTechnology)

        layoutEvent = findViewById(R.id.layoutEvent)
        layoutStats = findViewById(R.id.layoutStats)
        layoutActions = findViewById(R.id.layoutActions)
    }

    private fun setupClickListeners() {
        btnChoice1.setOnClickListener { handleChoice(0) }
        btnChoice2.setOnClickListener { handleChoice(1) }
        btnChoice3.setOnClickListener { handleChoice(2) }
        btnNextTurn.setOnClickListener { nextTurn() }
        btnNewGame.setOnClickListener { showNewGameConfirmation() }
        btnNPCs.setOnClickListener { showNPCMenu() }
        btnWorldStatus.setOnClickListener { showWorldStatus() }
        btnRegions.setOnClickListener { showRegionsMenu() }
        btnEconomy.setOnClickListener { showEconomyMenu() }
        btnPolicies.setOnClickListener { showPoliciesMenu() }
        btnTechnology.setOnClickListener { showTechnologyMenu() }
    }

    private fun startNewGame() {
        try {
            country = Country()
            country.name = "Your Nation"
            country.leaderName = "President"
            country.capitalCity = "Capital City"
            country.governmentType = "Democracy"

            // Initialize game systems
            NPCManager.initializeNPCs()
            GameWorld.initializeWorld()
            EconomyManager.initializeEconomy()
            PolicyManager.initializePolicySystem()
            TechnologyManager.initializeTechnology()

            updateUI()
            showToast("Welcome, President! Lead your nation to prosperity.")
        } catch (e: Exception) {
            Toast.makeText(this, "Error starting game: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun updateUI() {
        // Update basic info
        txtCountryName.text = country.name
        txtYear.text = "Year: ${country.year}"
        txtTurn.text = "Turn: ${country.turn}"
        txtLeaderName.text = "Leader: ${country.leaderName}"
        txtCapitalCity.text = "Capital: ${country.capitalCity}"
        txtGovernmentType.text = "Government: ${country.governmentType}"

        // Update stats with color coding
        txtPopulation.text = "Population: ${decimalFormat.format(country.population)}"
        txtGDP.text = "GDP: ${currencyFormat.format(country.gdp)}"
        txtTreasury.text = "Treasury: ${currencyFormat.format(country.treasury)}"

        txtStability.text = "Stability: ${country.stability}% (${country.getStabilityStatus()})"
        txtStability.setTextColor(getColorForValue(country.stability))

        txtHappiness.text = "Happiness: ${country.happiness}% (${country.getHappinessStatus()})"
        txtHappiness.setTextColor(getColorForValue(country.happiness))

        txtMilitary.text = "Military: ${country.military}%"
        txtMilitary.setTextColor(getColorForValue(country.military))

        txtInternationalRelations.text = "Int'l Relations: ${country.internationalRelations}%"
        txtInternationalRelations.setTextColor(getColorForValue(country.internationalRelations))

        txtEducation.text = "Education: ${country.education}%"
        txtEducation.setTextColor(getColorForValue(country.education))

        txtHealthcare.text = "Healthcare: ${country.healthcare}%"
        txtHealthcare.setTextColor(getColorForValue(country.healthcare))

        txtInfrastructure.text = "Infrastructure: ${country.infrastructure}%"
        txtInfrastructure.setTextColor(getColorForValue(country.infrastructure))

        txtEnvironment.text = "Environment: ${country.environment}%"
        txtEnvironment.setTextColor(getColorForValue(country.environment))

        // Check for game over
        if (country.isGameOver()) {
            showGameOver()
        }
    }

    private fun getColorForValue(value: Int): Int {
        return when {
            value >= 70 -> getColor(android.R.color.holo_green_dark)
            value >= 40 -> getColor(android.R.color.holo_orange_dark)
            else -> getColor(android.R.color.holo_red_dark)
        }
    }

    private fun nextTurn() {
        try {
            country.turn++
            country.year++

            // Apply passive effects based on stats
            applyPassiveEffects()

            // Update game world
            GameWorld.updateWorldState(country)
            GameWorld.processActiveEvents(country)

            // Process economic systems
            EconomyManager.processEconomicTurn(country)

            // Process policy systems
            PolicyManager.processMinistryTurn()
            val passedLaws = PolicyManager.processLawsTurn(country)
            passedLaws.forEach { law ->
                showToast("Law passed: $law!")
            }

            // Process technology
            TechnologyManager.processResearchTurn(country)

            // Check advancements and milestones
            AdvancementManager.checkAdvancements(country)
            val earnedMilestones = AdvancementManager.checkMilestones(country)
            earnedMilestones.forEach { reward ->
                showToast("Milestone: $reward")
            }

            // Update statistics
            AdvancementManager.statistics.totalTurns++
            AdvancementManager.statistics.totalGdpEarned += country.gdp * 0.02
            if (country.gdp > AdvancementManager.statistics.highestGdp) {
                AdvancementManager.statistics.highestGdp = country.gdp
            }
            if (country.stability > AdvancementManager.statistics.highestStability) {
                AdvancementManager.statistics.highestStability = country.stability
            }
            if (country.happiness > AdvancementManager.statistics.highestHappiness) {
                AdvancementManager.statistics.highestHappiness = country.happiness
            }

            // Update NPCs
            NPCManager.updateAllNPCMoods(country)
            NPCManager.processNPCTurns(country)
            NPCManager.checkNPCTriggerConditions(country)

            // Check for election
            PolicyManager.nextElectionTurns--
            if (PolicyManager.nextElectionTurns <= 0) {
                PolicyManager.processElection(country)
                showToast("Election held! Government updated.")
            }

            // Check for game over
            if (country.isGameOver()) {
                updateUI()
                return
            }

            // Trigger random event (70% chance)
            if (Math.random() < 0.7) {
                triggerRandomEvent()
            } else {
                layoutEvent.visibility = android.view.View.GONE
                layoutActions.visibility = android.view.View.VISIBLE

                // Show NPC advice occasionally
                if (Math.random() < 0.3) {
                    showRandomNPCAdvice()
                }
            }

            updateUI()
        } catch (e: Exception) {
            Toast.makeText(this, "Next Turn Error: ${e.message}\n${e.stackTraceToString()}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun applyPassiveEffects() {
        try {
            // GDP growth based on various factors
            val gdpGrowthRate = ((country.education + country.infrastructure + country.stability) / 300.0) - 0.05

            // Apply global economy modifier
            val economyModifier = when (GameWorld.globalEconomyState) {
                EconomyState.BOOMING -> 1.1
                EconomyState.STABLE -> 1.0
                EconomyState.SLOWING -> 0.95
                EconomyState.RECESSION -> 0.9
            }

            country.gdp = country.gdp * (1 + gdpGrowthRate) * economyModifier

            // Population growth
            val populationGrowthRate = ((country.healthcare + country.happiness) / 200.0) - 0.02
            country.population = ((country.population * (1 + populationGrowthRate)).toInt()).coerceAtLeast(1000)

            // Treasury changes
            val taxRevenue = country.gdp * 0.02 // 2% tax per turn

            // Apply regional bonuses
            val regionBonus = GameWorld.getUnlockedRegions().sumOf { region ->
                val bonus = GameWorld.getRegionBonus(region)
                ((bonus["tax"] ?: 1.0) - 1.0) * country.gdp * 0.001
            }

            val expenses = (country.population * 10.0) + (country.military * 100000.0) + (country.education * 50000.0)
            country.treasury = country.treasury + taxRevenue + regionBonus - expenses

            // Clamp values
            country.stability = country.stability.coerceIn(0, 100)
            country.happiness = country.happiness.coerceIn(0, 100)
            country.military = country.military.coerceIn(0, 100)
            country.internationalRelations = country.internationalRelations.coerceIn(0, 100)
            country.education = country.education.coerceIn(0, 100)
            country.healthcare = country.healthcare.coerceIn(0, 100)
            country.infrastructure = country.infrastructure.coerceIn(0, 100)
            country.environment = country.environment.coerceIn(0, 100)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun triggerRandomEvent() {
        currentEvent = EventManager.getRandomEvent()
        currentEvent?.let { event ->
            txtEventTitle.text = event.title
            txtEventDescription.text = event.description

            event.choices.forEachIndexed { index, choice ->
                when (index) {
                    0 -> {
                        btnChoice1.text = choice.text
                        btnChoice1.visibility = android.view.View.VISIBLE
                    }
                    1 -> {
                        btnChoice2.text = choice.text
                        btnChoice2.visibility = android.view.View.VISIBLE
                    }
                    2 -> {
                        btnChoice3.text = choice.text
                        btnChoice3.visibility = android.view.View.VISIBLE
                    }
                }
            }

            layoutEvent.visibility = android.view.View.VISIBLE
            layoutActions.visibility = android.view.View.GONE
        }
    }

    private fun handleChoice(choiceIndex: Int) {
        currentEvent?.let { event ->
            val choice = event.choices[choiceIndex]

            // Apply effects
            choice.effects.forEach { (stat, value) ->
                when (stat) {
                    "treasury" -> country.treasury += value.toDouble()
                    "happiness" -> country.happiness = (country.happiness + value).coerceIn(0, 100)
                    "stability" -> country.stability = (country.stability + value).coerceIn(0, 100)
                    "military" -> country.military = (country.military + value).coerceIn(0, 100)
                    "internationalRelations" -> country.internationalRelations = (country.internationalRelations + value).coerceIn(0, 100)
                    "education" -> country.education = (country.education + value).coerceIn(0, 100)
                    "healthcare" -> country.healthcare = (country.healthcare + value).coerceIn(0, 100)
                    "infrastructure" -> country.infrastructure = (country.infrastructure + value).coerceIn(0, 100)
                    "environment" -> country.environment = (country.environment + value).coerceIn(0, 100)
                    "gdp" -> country.gdp = (country.gdp + value.toDouble()).coerceAtLeast(0.0)
                    "population" -> country.population = (country.population + value).coerceAtLeast(0)
                }
            }

            showToast(choice.message)

            // Hide event layout and show action buttons
            layoutEvent.visibility = android.view.View.GONE
            layoutActions.visibility = android.view.View.VISIBLE

            // Reset button visibility
            btnChoice1.visibility = android.view.View.VISIBLE
            btnChoice2.visibility = android.view.View.VISIBLE
            btnChoice3.visibility = android.view.View.VISIBLE

            currentEvent = null
            updateUI()
        }
    }

    private fun showNPCMenu() {
        val npcs = NPCManager.getAvailableNPCs()
        val npcNames = npcs.map { npc ->
            "${npc.name} (${npc.role}) - ${npc.getRelationshipStatus()}"
        }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Advisors & NPCs")
            .setItems(npcNames) { _, which ->
                val selectedNPC = npcs[which]
                showNPCDetail(selectedNPC)
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showNPCDetail(npc: NPC) {
        val message = StringBuilder()
        message.append("${npc.name}\n")
        message.append("Role: ${npc.role}\n")
        message.append("Relationship: ${npc.relationship}/100 (${npc.getRelationshipStatus()})\n")
        message.append("Influence: ${npc.influence}/100\n")
        message.append("Mood: ${npc.getMoodDescription()}\n\n")
        message.append("Description:\n${npc.description}\n\n")
        message.append("Personality:\n")
        message.append("- Traits: ${npc.personality.trait1}, ${npc.personality.trait2}\n")
        message.append("- Motivation: ${npc.personality.motivation}\n")
        message.append("- Fear: ${npc.personality.fear}\n\n")
        message.append("Advice:\n${NPCManager.getNPCAdvice(npc, country)}")

        if (npc.quests.isNotEmpty()) {
            message.append("\n\nAvailable Quests: ${npc.quests.count { !it.isCompleted && !it.isFailed }}")
        }

        AlertDialog.Builder(this)
            .setTitle("NPC Details")
            .setMessage(message.toString())
            .setPositiveButton("OK", null)
            .setNeutralButton("View Quests") { _, _ ->
                showNPCQuests(npc)
            }
            .show()
    }

    private fun showNPCQuests(npc: NPC) {
        val availableQuests = npc.quests.filter { !it.isCompleted && !it.isFailed }
        
        if (availableQuests.isEmpty()) {
            showToast("No available quests from ${npc.name}")
            return
        }

        val quest = availableQuests.first()
        
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

        var message = "${quest.title}\n\n"
        message += "${quest.description}\n\n"
        message += "Requirements:\n"
        quest.requirements.forEach { req ->
            val met = when (req.type) {
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
            message += "- ${req.description} [${if (met) "✓" else "✗"}]\n"
        }

        message += "\nRewards:\n"
        quest.rewards.forEach { reward ->
            message += "- ${reward.description}\n"
        }

        if (quest.failureConsequences.isNotEmpty()) {
            message += "\nFailure Consequences:\n"
            quest.failureConsequences.forEach { consequence ->
                message += "- ${consequence.description}\n"
            }
        }

        AlertDialog.Builder(this)
            .setTitle("Quest Details")
            .setMessage(message.toString())
            .setPositiveButton(if (requirementsMet) "Accept Quest" else "Requirements Not Met") { dialog, _ ->
                if (requirementsMet) {
                    quest.progress = 1
                    showToast("Quest '${quest.title}' accepted!")
                    npc.relationship = (npc.relationship + 5).coerceIn(0, 100)
                }
            }
            .setNegativeButton("Back", null)
            .show()
    }

    private fun showRandomNPCAdvice() {
        val advisor = NPCManager.getNPCsByRole(NPCRole.ADVISOR).firstOrNull()
        advisor?.let {
            val advice = NPCManager.getNPCAdvice(it, country)
            Toast.makeText(this, "${it.name}: $advice", Toast.LENGTH_LONG).show()
        }
    }

    private fun showWorldStatus() {
        val items = arrayOf(
            "World Report",
            "Factions",
            "Diplomatic Relations",
            "Threat Level & Security"
        )
        
        AlertDialog.Builder(this)
            .setTitle("World Management")
            .setItems(items) { _, which ->
                when (which) {
                    0 -> showWorldReport()
                    1 -> showFactionsMenu()
                    2 -> showDiplomacyMenu()
                    3 -> showThreatLevelMenu()
                }
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showWorldReport() {
        val report = GameWorld.getWorldStatusReport(country)
        AlertDialog.Builder(this)
            .setTitle("World Report")
            .setMessage(report)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showFactionsMenu() {
        val factions = PolicyManager.getAllFactions()
        val factionNames = factions.map { f -> 
            "${f.name} (${f.ideology})\nPower: ${f.power}% | ${if (f.isLegal) "Legal" else "Illegal"}" 
        }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Political Factions")
            .setItems(factionNames) { _, which ->
                val faction = factions[which]
                var message = "${faction.name}\n\n"
                message += "${faction.description}\n\n"
                message += "Ideology: ${faction.ideology}\n"
                message += "Leader: ${faction.leader}\n"
                message += "Power: ${faction.power}%\n"
                message += "Status: ${if (faction.isLegal) "✓ Legal" else "⚠ Illegal"}\n\n"
                message += "Goals:\n"
                faction.goals.forEach { goal ->
                    message += "- $goal\n"
                }
                
                AlertDialog.Builder(this)
                    .setTitle(faction.name)
                    .setMessage(message)
                    .setPositiveButton("OK", null)
                    .setNeutralButton(if (faction.isLegal) "Ban Faction" else "Legalize Faction") { _, _ ->
                        toggleFactionLegality(faction)
                    }
                    .show()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun toggleFactionLegality(faction: PolicyManager.Faction) {
        if (faction.isLegal) {
            // Ban faction
            faction.isLegal = false
            faction.power = (faction.power - 10).coerceAtLeast(0)
            country.stability = (country.stability + 5).coerceIn(0, 100)
            country.happiness = (country.happiness - 5).coerceIn(0, 100)
            showToast("${faction.name} banned! Stability +5, Happiness -5")
        } else {
            // Legalize faction
            faction.isLegal = true
            country.stability = (country.stability - 5).coerceIn(0, 100)
            country.happiness = (country.happiness + 5).coerceIn(0, 100)
            showToast("${faction.name} legalized! Stability -5, Happiness +5")
        }
    }

    private fun showDiplomacyMenu() {
        val relations = GameWorld.diplomaticRelations
        val nationIds = relations.keys.sorted()
        val nationNames = nationIds.map { id ->
            "${GameWorld.getNationName(id)}: ${relations[id]}%"
        }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Diplomatic Relations")
            .setItems(nationNames) { _, which ->
                val nationId = nationIds[which]
                val relation = relations[nationId] ?: 50
                showDiplomacyOptions(nationId, relation)
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showDiplomacyOptions(nationId: Int, currentRelation: Int) {
        val nationName = GameWorld.getNationName(nationId)
        val relationStatus = when {
            currentRelation >= 80 -> "Ally"
            currentRelation >= 60 -> "Friendly"
            currentRelation >= 40 -> "Neutral"
            currentRelation >= 20 -> "Tense"
            else -> "Hostile"
        }
        
        val options = arrayOf(
            "Send Aid (-$10M, +10 Relations)",
            "Trade Deal (-$5M, +5 Relations, +$20M GDP)",
            "Military Exercise (-$15M, +5 Military, -5 Relations)",
            "Cancel"
        )
        
        AlertDialog.Builder(this)
            .setTitle("$nationName ($relationStatus - $currentRelation%)")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> sendForeignAid(nationId)
                    1 -> proposeTradeDeal(nationId)
                    2 -> conductMilitaryExercise(nationId)
                    3 -> {} // Cancel
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun sendForeignAid(nationId: Int) {
        if (country.treasury >= 10000000) {
            country.treasury -= 10000000
            GameWorld.setDiplomaticRelation(nationId, GameWorld.getDiplomaticRelation(nationId) + 10)
            showToast("Foreign aid sent! Relations improved.")
        } else {
            showToast("Not enough treasury! Need $10M")
        }
    }

    private fun proposeTradeDeal(nationId: Int) {
        if (country.treasury >= 5000000) {
            country.treasury -= 5000000
            country.gdp = (country.gdp + 20000000).coerceAtLeast(0.0)
            GameWorld.setDiplomaticRelation(nationId, GameWorld.getDiplomaticRelation(nationId) + 5)
            showToast("Trade deal signed! GDP +$20M, Relations +5")
        } else {
            showToast("Not enough treasury! Need $5M")
        }
    }

    private fun conductMilitaryExercise(nationId: Int) {
        if (country.treasury >= 15000000) {
            country.treasury -= 15000000
            country.military = (country.military + 5).coerceIn(0, 100)
            GameWorld.setDiplomaticRelation(nationId, GameWorld.getDiplomaticRelation(nationId) - 5)
            showToast("Military exercise conducted! Military +5, Relations -5")
        } else {
            showToast("Not enough treasury! Need $15M")
        }
    }

    private fun showThreatLevelMenu() {
        var message = "=== NATIONAL SECURITY ===\n\n"
        message += "Threat Level: ${GameWorld.currentThreatLevel}\n"
        message += "${GameWorld.getThreatLevelDescription()}\n\n"
        message += "World Tension: ${GameWorld.worldTension}%\n"
        message += "Global Economy: ${GameWorld.globalEconomyState}\n\n"
        message += "Active Crises: ${GameWorld.activeCrises}\n"
        
        val activeEvents = GameWorld.getActiveEvents()
        if (activeEvents.isNotEmpty()) {
            message += "\nActive Events:\n"
            activeEvents.forEach { event ->
                message += "- ${event.title} (${event.turnsRemaining} turns)\n"
            }
        } else {
            message += "\nNo active crises.\n"
        }
        
        message += "\n=== RECOMMENDATIONS ===\n"
        when (GameWorld.currentThreatLevel) {
            GameWorld.ThreatLevel.LOW -> message += "• Maintain current policies\n• Focus on economic growth"
            GameWorld.ThreatLevel.MEDIUM -> message += "• Monitor situation closely\n• Consider military investment"
            GameWorld.ThreatLevel.HIGH -> message += "• Increase military readiness\n• Seek international alliances\n• Prepare emergency measures"
            GameWorld.ThreatLevel.CRITICAL -> message += "• EMERGENCY: Consider emergency powers\n• Mobilize military\n• Seek immediate international support"
        }
        
        AlertDialog.Builder(this)
            .setTitle("National Security")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .setNeutralButton("Increase Security") { _, _ ->
                increaseSecurityMeasures()
            }
            .show()
    }

    private fun increaseSecurityMeasures() {
        if (country.treasury >= 20000000) {
            country.treasury -= 20000000
            country.military = (country.military + 10).coerceIn(0, 100)
            country.stability = (country.stability + 5).coerceIn(0, 100)
            GameWorld.worldTension = (GameWorld.worldTension - 5).coerceIn(0, 100)
            showToast("Security measures increased! Military +10, Stability +5")
        } else {
            showToast("Not enough treasury! Need $20M")
        }
    }

    private fun showRegionsMenu() {
        val unlockedRegions = GameWorld.getUnlockedRegions()
        val allRegions = GameWorld.getAllRegions()

        val regionNames: Array<String> = allRegions.map { region ->
            if (region.isUnlocked) {
                "✓ ${region.name} (Dev: ${region.development}%, Loyalty: ${region.loyalty}%)"
            } else {
                "🔒 ${region.name} - Locked"
            }
        }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Regions")
            .setItems(regionNames) { _: android.content.DialogInterface, which: Int ->
                val selectedRegion = allRegions[which]
                if (selectedRegion.isUnlocked) {
                    showRegionDetail(selectedRegion)
                } else {
                    // Show unlock requirements
                    val canUnlock = GameWorld.canUnlockRegion(selectedRegion, country)
                    var message = "${selectedRegion.name}\n\n"
                    message += "${selectedRegion.description}\n\n"
                    message += "Status: LOCKED\n\n"
                    message += "Unlock Requirements:\n"
                    
                    when (selectedRegion.id) {
                        1 -> message += "- Infrastructure: 50%+ (Current: ${country.infrastructure}%)\n- GDP: $1.2B+ (Current: ${currencyFormat.format(country.gdp)})"
                        2 -> message += "- Stability: 60%+ (Current: ${country.stability}%)\n- Population: 2M+ (Current: ${decimalFormat.format(country.population)})"
                        3 -> message += "- Infrastructure: 40%+ (Current: ${country.infrastructure}%)\n- Treasury: $50M+ (Current: ${currencyFormat.format(country.treasury)})"
                        4 -> message += "- Military: 50%+ (Current: ${country.military}%)\n- Stability: 50%+ (Current: ${country.stability}%)"
                        5 -> message += "- Military: 60%+ (Current: ${country.military}%)\n- Int'l Relations: 40%+ (Current: ${country.internationalRelations}%)"
                        else -> message += "- Meeting specific requirements"
                    }
                    
                    if (canUnlock) {
                        message += "\n\n✓ Requirements met! Click OK to unlock."
                        AlertDialog.Builder(this)
                            .setTitle("Region Locked")
                            .setMessage(message)
                            .setPositiveButton("Unlock") { _, _ ->
                                GameWorld.unlockRegion(selectedRegion.id)
                                showToast("${selectedRegion.name} unlocked!")
                            }
                            .setNegativeButton("Cancel", null)
                            .show()
                    } else {
                        AlertDialog.Builder(this)
                            .setTitle("Region Locked")
                            .setMessage(message)
                            .setPositiveButton("OK", null)
                            .show()
                    }
                }
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showRegionDetail(region: Region) {
        val bonus = GameWorld.getRegionBonus(region)
        var message = "${region.name}\n\n"
        message += "${region.description}\n\n"
        message += "Population: ${decimalFormat.format(region.population)}\n"
        message += "Development: ${region.development}%\n"
        message += "Stability: ${region.stability}%\n"
        message += "Loyalty: ${region.loyalty}%\n\n"
        message += "Resources:\n"
        region.resources.forEach { resource ->
            message += "- $resource\n"
        }
        message += "\nBonuses:\n"
        bonus.forEach { (key, value) ->
            message += "- $key: x${value}\n"
        }

        AlertDialog.Builder(this)
            .setTitle("Region Details")
            .setMessage(message.toString())
            .setPositiveButton("OK", null)
            .setNeutralButton("Invest ($10M)") { _, _ ->
                investInRegion(region)
            }
            .setNegativeButton("Special Action") { _, _ ->
                performRegionAction(region)
            }
            .show()
    }

    private fun investInRegion(region: Region) {
        if (country.treasury >= 10000000) {
            country.treasury -= 10000000
            region.development = (region.development + 5).coerceIn(0, 100)
            region.loyalty = (region.loyalty + 3).coerceIn(0, 100)
            country.gdp = (country.gdp + 5000000).coerceAtLeast(0.0)
            showToast("${region.name} developed! Development +5, Loyalty +3")
        } else {
            showToast("Not enough treasury! Need $10M")
        }
    }

    private fun performRegionAction(region: Region) {
        val actions = when (region.id) {
            0 -> arrayOf("Capital Investment (-$20M, +10 Dev)", "Government Reform (+5 Stability)")
            1 -> arrayOf("Industrial Subsidy (-$15M, +10 GDP)", "Worker Program (-$10M, +5 Happiness)")
            2 -> arrayOf("Agricultural Support (-$10M, +5 GDP)", "Rural Development (-$8M, +5 Loyalty)")
            3 -> arrayOf("Port Expansion (-$25M, +15 GDP)", "Tourism Campaign (-$12M, +10 GDP)")
            4 -> arrayOf("Resource Extraction (-$20M, +20 GDP, -5 Env)", "Military Base (+10 Military)")
            5 -> arrayOf("Border Security (-$15M, +10 Stability)", "Diplomatic Outreach (-$10M, +10 Int'l)")
            else -> arrayOf("Development Project (-$10M, +5 Dev)")
        }
        
        AlertDialog.Builder(this)
            .setTitle("${region.name} - Special Actions")
            .setItems(actions) { _, which ->
                when (region.id) {
                    0 -> performCapitalAction(which)
                    1 -> performIndustrialAction(which)
                    2 -> performAgriculturalAction(which)
                    3 -> performCoastalAction(which)
                    4 -> performNorthernAction(which)
                    5 -> performBorderAction(which)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun performCapitalAction(which: Int) {
        when (which) {
            0 -> {
                if (country.treasury >= 20000000) {
                    country.treasury -= 20000000
                    GameWorld.getRegionById(0)?.development = (GameWorld.getRegionById(0)?.development?.plus(10) ?: 0).coerceIn(0, 100)
                    showToast("Capital invested! Development +10")
                } else showToast("Need $20M")
            }
            1 -> {
                country.stability = (country.stability + 5).coerceIn(0, 100)
                showToast("Government reform! Stability +5")
            }
        }
    }

    private fun performIndustrialAction(which: Int) {
        when (which) {
            0 -> {
                if (country.treasury >= 15000000) {
                    country.treasury -= 15000000
                    country.gdp = (country.gdp + 10000000).coerceAtLeast(0.0)
                    showToast("Industrial subsidy! GDP +$10M")
                } else showToast("Need $15M")
            }
            1 -> {
                if (country.treasury >= 10000000) {
                    country.treasury -= 10000000
                    country.happiness = (country.happiness + 5).coerceIn(0, 100)
                    showToast("Worker program! Happiness +5")
                } else showToast("Need $10M")
            }
        }
    }

    private fun performAgriculturalAction(which: Int) {
        when (which) {
            0 -> {
                if (country.treasury >= 10000000) {
                    country.treasury -= 10000000
                    country.gdp = (country.gdp + 5000000).coerceAtLeast(0.0)
                    showToast("Agricultural support! GDP +$5M")
                } else showToast("Need $10M")
            }
            1 -> {
                if (country.treasury >= 8000000) {
                    country.treasury -= 8000000
                    GameWorld.getRegionById(2)?.loyalty = (GameWorld.getRegionById(2)?.loyalty?.plus(5) ?: 0).coerceIn(0, 100)
                    showToast("Rural development! Loyalty +5")
                } else showToast("Need $8M")
            }
        }
    }

    private fun performCoastalAction(which: Int) {
        when (which) {
            0 -> {
                if (country.treasury >= 25000000) {
                    country.treasury -= 25000000
                    country.gdp = (country.gdp + 15000000).coerceAtLeast(0.0)
                    showToast("Port expansion! GDP +$15M")
                } else showToast("Need $25M")
            }
            1 -> {
                if (country.treasury >= 12000000) {
                    country.treasury -= 12000000
                    country.gdp = (country.gdp + 10000000).coerceAtLeast(0.0)
                    showToast("Tourism campaign! GDP +$10M")
                } else showToast("Need $12M")
            }
        }
    }

    private fun performNorthernAction(which: Int) {
        when (which) {
            0 -> {
                if (country.treasury >= 20000000) {
                    country.treasury -= 20000000
                    country.gdp = (country.gdp + 20000000).coerceAtLeast(0.0)
                    country.environment = (country.environment - 5).coerceIn(0, 100)
                    showToast("Resource extraction! GDP +$20M, Environment -5")
                } else showToast("Need $20M")
            }
            1 -> {
                country.military = (country.military + 10).coerceIn(0, 100)
                showToast("Military base! Military +10")
            }
        }
    }

    private fun performBorderAction(which: Int) {
        when (which) {
            0 -> {
                if (country.treasury >= 15000000) {
                    country.treasury -= 15000000
                    country.stability = (country.stability + 10).coerceIn(0, 100)
                    showToast("Border security! Stability +10")
                } else showToast("Need $15M")
            }
            1 -> {
                if (country.treasury >= 10000000) {
                    country.treasury -= 10000000
                    country.internationalRelations = (country.internationalRelations + 10).coerceIn(0, 100)
                    showToast("Diplomatic outreach! Int'l Relations +10")
                } else showToast("Need $10M")
            }
        }
    }

    private fun showGameOver() {
        layoutEvent.visibility = android.view.View.VISIBLE
        txtEventTitle.text = "GAME OVER"
        txtEventDescription.text = country.getGameOverReason() +
            "\n\nYou survived for ${country.turn} turns until year ${country.year}."

        btnChoice1.text = "New Game"
        btnChoice1.visibility = android.view.View.VISIBLE
        btnChoice1.setOnClickListener {
            startNewGame()
            layoutEvent.visibility = android.view.View.GONE
            layoutActions.visibility = android.view.View.VISIBLE
        }

        btnChoice2.visibility = android.view.View.GONE
        btnChoice3.visibility = android.view.View.GONE

        layoutActions.visibility = android.view.View.GONE
    }

    private fun showNewGameConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("New Game")
            .setMessage("Are you sure you want to start a new game? Current progress will be lost.")
            .setPositiveButton("Yes") { _, _ ->
                startNewGame()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun showEconomyMenu() {
        val items = arrayOf(
            "Economic Summary",
            "Manage Industries",
            "Economic Policies",
            "Budget Allocation",
            "Trade & Resources",
            "Market Conditions"
        )
        
        AlertDialog.Builder(this)
            .setTitle("Economy Management")
            .setItems(items) { _, which ->
                when (which) {
                    0 -> showEconomicSummary()
                    1 -> showIndustriesMenu()
                    2 -> showEconomicPoliciesMenu()
                    3 -> showBudgetMenu()
                    4 -> showTradeMenu()
                    5 -> showMarketConditionsMenu()
                }
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showEconomicSummary() {
        val summary = EconomyManager.getEconomicSummary(country)
        AlertDialog.Builder(this)
            .setTitle("Economic Summary")
            .setMessage(summary)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showIndustriesMenu() {
        val industryNames = EconomyManager.industries.map { ind ->
            "${ind.name} (Lvl ${ind.level}) - ${ind.sector}\nEfficiency: ${(ind.efficiency * 100).toInt()}%, Employees: ${ind.employees}/${ind.employmentCapacity * ind.level}"
        }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Industries")
            .setItems(industryNames) { _, which ->
                val industry = EconomyManager.industries[which]
                val upgradeCost = industry.baseOutput * industry.level * 0.1
                AlertDialog.Builder(this)
                    .setTitle(industry.name)
                    .setMessage("${industry.description}\n\nSector: ${industry.sector}\nLevel: ${industry.level}\nEfficiency: ${(industry.efficiency * 100).toInt()}%\nEmployees: ${industry.employees}\nOutput: $${String.format("%,d", (industry.baseOutput * industry.level * industry.efficiency).toLong())}\n\nUpgrade Cost: $${String.format("%,d", upgradeCost.toLong())}")
                    .setPositiveButton("Upgrade") { _, _ ->
                        if (country.treasury >= upgradeCost) {
                            country.treasury -= upgradeCost
                            industry.level++
                            industry.efficiency = (industry.efficiency + 0.1).coerceIn(0.5, 2.0)
                            showToast("${industry.name} upgraded to level ${industry.level}!")
                        } else {
                            showToast("Not enough treasury!")
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showEconomicPoliciesMenu() {
        val policies = EconomyManager.getAvailablePolicies()
        val policyNames = policies.map { p ->
            "${p.name} (${p.policyType})\nCost: $${String.format("%,d", p.cost.toLong())} | Duration: ${p.duration} turns"
        }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Economic Policies")
            .setItems(policyNames) { _, which ->
                val policy = policies[which]
                var message = "${policy.name}\n\n${policy.description}\n\n"
                message += "Type: ${policy.policyType}\n"
                message += "Cost: $${String.format("%,d", policy.cost.toLong())}\n"
                message += "Duration: ${policy.duration} turns\n\n"
                message += "Effects:\n"
                policy.effects.forEach { (stat, value) ->
                    message += "- $stat: $value\n"
                }
                if (policy.requirements.isNotEmpty()) {
                    message += "\nRequirements:\n"
                    policy.requirements.forEach { (req, value) ->
                        message += "- $req: $value\n"
                    }
                }
                
                val isActive = EconomyManager.activePolicies.any { it.id == policy.id && it.isActive }
                
                AlertDialog.Builder(this)
                    .setTitle("Policy Details")
                    .setMessage(message.toString())
                    .setPositiveButton(if (isActive) "Already Active" else "Activate") { _, _ ->
                        if (!isActive && EconomyManager.activatePolicy(policy, country)) {
                            showToast("Policy '${policy.name}' activated!")
                        } else if (!isActive) {
                            showToast("Cannot activate policy - requirements not met!")
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showBudgetMenu() {
        val budget = EconomyManager.budget
        val message = StringBuilder()
        message.append("=== BUDGET ALLOCATION ===\n\n")
        message.append("Total: ${budget.getTotal()}% / 100%\n\n")
        message.append("Defense: ${budget.defense}%\n")
        message.append("Education: ${budget.education}%\n")
        message.append("Healthcare: ${budget.healthcare}%\n")
        message.append("Infrastructure: ${budget.infrastructure}%\n")
        message.append("Social Welfare: ${budget.socialWelfare}%\n")
        message.append("Research: ${budget.research}%\n")
        message.append("Environment: ${budget.environment}%\n")
        message.append("Debt Service: ${budget.debtService}%\n")
        message.append("Other: ${budget.getTotal() - budget.defense - budget.education - budget.healthcare - budget.infrastructure - budget.socialWelfare - budget.research - budget.environment - budget.debtService}%\n")
        
        AlertDialog.Builder(this)
            .setTitle("Budget Allocation")
            .setMessage(message.toString())
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showTradeMenu() {
        val resources = EconomyManager.resources
        val resourceInfo = resources.map { r ->
            "${r.name}: $${r.currentPrice.toString().take(6)} | Stock: ${r.stockpile.toLong()}\nProd: ${r.production}/Cons: ${r.consumption} | Import: ${r.importAmount}/Export: ${r.exportAmount}"
        }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Resources & Trade")
            .setItems(resourceInfo) { _, which ->
                val resource = resources[which]
                val message = "${resource.name}\n\nType: ${resource.type}\nPrice: $${resource.currentPrice}\nStockpile: ${resource.stockpile}\nProduction: ${resource.production}\nConsumption: ${resource.consumption}\nImports: ${resource.importAmount}\nExports: ${resource.exportAmount}\n\nTrade Balance: $${String.format("%,d", ((resource.exportAmount - resource.importAmount) * resource.currentPrice).toLong())}"
                AlertDialog.Builder(this)
                    .setTitle(resource.name)
                    .setMessage(message)
                    .setPositiveButton("OK", null)
                    .show()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showMarketConditionsMenu() {
        val conditions = EconomyManager.marketConditions
        if (conditions.isEmpty()) {
            showToast("No active market conditions. Economy is stable.")
            return
        }
        
        val conditionInfo = conditions.map { c ->
            "${c.conditionType} (Severity: ${c.severity}) - ${c.turnsRemaining} turns remaining"
        }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Market Conditions")
            .setItems(conditionInfo) { _, which ->
                val condition = conditions[which]
                var message = "${condition.conditionType}\n\nSeverity: ${condition.severity}\nTurns Remaining: ${condition.turnsRemaining}\n\nEffects:\n"
                condition.effects.forEach { (stat, value) ->
                    message += "- $stat: $value\n"
                }
                AlertDialog.Builder(this)
                    .setTitle(condition.conditionType.toString())
                    .setMessage(message)
                    .setPositiveButton("OK", null)
                    .show()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showPoliciesMenu() {
        val items = arrayOf(
            "Political Summary",
            "Propose New Law",
            "Active Laws",
            "Government Ministries",
            "Lobby Groups",
            "Opinion Polls"
        )
        
        AlertDialog.Builder(this)
            .setTitle("Policy & Government")
            .setItems(items) { _, which ->
                when (which) {
                    0 -> showPoliticalSummary()
                    1 -> showProposeLawMenu()
                    2 -> showActiveLawsMenu()
                    3 -> showMinistriesMenu()
                    4 -> showLobbyGroupsMenu()
                    5 -> showOpinionPollsMenu()
                }
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showPoliticalSummary() {
        val summary = PolicyManager.getPoliticalSummary()
        AlertDialog.Builder(this)
            .setTitle("Political Summary")
            .setMessage(summary)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showProposeLawMenu() {
        val laws = PolicyManager.getAvailableLaws()
        val lawNames = laws.map { l -> "${l.name} (${l.lawType})\nCost: $${String.format("%,d", l.cost.toLong())}" }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Propose New Law")
            .setItems(lawNames) { _, which ->
                val law = laws[which]
                var message = "${law.name}\n\n${law.description}\n\n"
                message += "Type: ${law.lawType}\n"
                message += "Cost: $${String.format("%,d", law.cost.toLong())}\n"
                message += "Support Required: ${law.supportRequired}%\n\n"
                message += "Effects:\n"
                law.effects.forEach { (stat, value) ->
                    message += "- $stat: $value\n"
                }
                
                AlertDialog.Builder(this)
                    .setTitle("Law Details")
                    .setMessage(message.toString())
                    .setPositiveButton("Propose") { _, _ ->
                        if (PolicyManager.proposeLaw(law, country)) {
                            law.status = LawStatus.IN_COMMITTEE
                            showToast("Law '${law.name}' proposed!")
                        } else {
                            showToast("Cannot propose law - insufficient funds!")
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showActiveLawsMenu() {
        val activeLaws = PolicyManager.laws.filter { it.status == LawStatus.ACTIVE }
        if (activeLaws.isEmpty()) {
            showToast("No active laws.")
            return
        }
        
        val lawInfo = activeLaws.map { l -> "${l.name} - ${l.lawType}" }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Active Laws")
            .setItems(lawInfo) { _, which ->
                val law = activeLaws[which]
                AlertDialog.Builder(this)
                    .setTitle(law.name)
                    .setMessage("${law.description}\n\nType: ${law.lawType}\nProposed by: ${law.proposer}")
                    .setPositiveButton("OK", null)
                    .show()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showMinistriesMenu() {
        val ministryNames = PolicyManager.ministries.map { m -> "${m.name}\nEfficiency: ${m.efficiency.toInt()}% | Budget: $${String.format("%,d", (m.budget / 1000000).toLong())}M | Projects: ${m.projects.size}" }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Government Ministries")
            .setItems(ministryNames) { _, which ->
                val ministry = PolicyManager.ministries[which]
                var message = "${ministry.name}\n\n${ministry.description}\n\n"
                message += "Minister: ${ministry.minister.name}\n"
                message += "Efficiency: ${ministry.efficiency.toInt()}%\n"
                message += "Budget: $${String.format("%,d", ministry.budget.toLong())}\n"
                message += "Employees: ${ministry.employees}\n"
                if (ministry.projects.isNotEmpty()) {
                    message += "\nActive Projects:\n"
                    ministry.projects.forEach { p ->
                        message += "- ${p.name} (${p.progress}/${p.duration})\n"
                    }
                }
                AlertDialog.Builder(this)
                    .setTitle(ministry.name)
                    .setMessage(message)
                    .setPositiveButton("OK", null)
                    .show()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showLobbyGroupsMenu() {
        val lobbyNames = PolicyManager.lobbyGroups.map { l -> "${l.name} (${l.interest})\nInfluence: ${l.influence.toInt()}%" }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Lobby Groups")
            .setItems(lobbyNames) { _, which ->
                val lobby = PolicyManager.lobbyGroups[which]
                val message = "${lobby.name}\n\nInterest: ${lobby.interest}\nBudget: $${String.format("%,d", lobby.budget.toLong())}\nInfluence: ${lobby.influence.toInt()}%\n\nAllied Parties: ${lobby.alliedParties.joinToString()}\nOpposed Parties: ${lobby.opposedParties.joinToString()}"
                AlertDialog.Builder(this)
                    .setTitle(lobby.name)
                    .setMessage(message)
                    .setPositiveButton("OK", null)
                    .show()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showOpinionPollsMenu() {
        val ratings = PolicyManager.approvalRatings
        val message = "=== APPROVAL RATINGS ===\n\n" +
            "President: ${ratings.presidentApproval.toInt()}%\n" +
            "Congress: ${ratings.congressApproval.toInt()}%\n" +
            "Supreme Court: ${ratings.supremeCourtApproval.toInt()}%\n" +
            "Media: ${ratings.mediaApproval.toInt()}%\n" +
            "International: ${ratings.internationalApproval.toInt()}%"
        
        AlertDialog.Builder(this)
            .setTitle("Opinion Polls")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showTechnologyMenu() {
        val items = arrayOf(
            "Technology Summary",
            "Research Technologies",
            "Research Projects",
            "Research Facilities"
        )
        
        AlertDialog.Builder(this)
            .setTitle("Technology & Research")
            .setItems(items) { _, which ->
                when (which) {
                    0 -> showTechnologySummary()
                    1 -> showResearchTechnologiesMenu()
                    2 -> showResearchProjectsMenu()
                    3 -> showResearchFacilitiesMenu()
                }
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showTechnologySummary() {
        val summary = TechnologyManager.getTechnologySummary()
        AlertDialog.Builder(this)
            .setTitle("Technology Summary")
            .setMessage(summary)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showResearchTechnologiesMenu() {
        val techs = TechnologyManager.getResearchableTechnologies()
        if (techs.isEmpty()) {
            showToast("No technologies available to research. Complete prerequisites first.")
            return
        }
        
        val techNames = techs.map { t -> "${t.name} (Tier ${t.tier.name.replace("TIER_", "")})\nCost: $${t.researchCost}M | ${t.category}" }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Research Technologies")
            .setItems(techNames) { _, which ->
                val tech = techs[which]
                var message = "${tech.name}\n\n${tech.description}\n\n"
                message += "Category: ${tech.category}\n"
                message += "Tier: ${tech.tier}\n"
                message += "Research Cost: $${tech.researchCost}M\n"
                message += "Progress: ${tech.researchProgress}/${tech.researchCost * 10}\n\n"
                message += "Effects:\n"
                tech.effects.forEach { (stat, value) ->
                    message += "- $stat: $value\n"
                }
                if (tech.unlockFeatures.isNotEmpty()) {
                    message += "\nUnlocks: ${tech.unlockFeatures.joinToString()}"
                }
                
                AlertDialog.Builder(this)
                    .setTitle("Technology Details")
                    .setMessage(message.toString())
                    .setPositiveButton("Start Research") { _, _ ->
                        if (TechnologyManager.startResearch(tech.id, country)) {
                            showToast("Research started: ${tech.name}")
                        } else {
                            showToast("Cannot start research - insufficient funds or prerequisites!")
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showResearchProjectsMenu() {
        val projects = TechnologyManager.getAvailableProjects()
        val projectNames = projects.map { p -> "${p.name}\nCost: $${String.format("%,d", (p.cost / 1000000).toLong())}M | ${p.duration} turns" }.toTypedArray()
        
 AlertDialog.Builder(this)
            .setTitle("Research Projects")
            .setItems(projectNames) { _, which ->
                val project = projects[which]
                var message = "${project.name}\n\n${project.description}\n\n"
                message += "Cost: $${String.format("%,d", project.cost.toLong())}\n"
                message += "Duration: ${project.duration} turns\n\n"
                message += "Requirements:\n"
                project.requirements.forEach { (req, value) ->
                    message += "- $req: $value\n"
                }
                message += "\nRewards:\n"
                project.rewards.forEach { (stat, value) ->
                    message += "- $stat: $value\n"
                }
                
                AlertDialog.Builder(this)
                    .setTitle("Project Details")
                    .setMessage(message.toString())
                    .setPositiveButton("Fund Project") { _, _ ->
                        if (TechnologyManager.startResearchProject(project, country)) {
                            showToast("Project '${project.name}' funded!")
                        } else {
                            showToast("Cannot fund project - requirements not met!")
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showResearchFacilitiesMenu() {
        val facilityNames = TechnologyManager.facilities.map { f -> "${f.name} (Lvl ${f.level})\nEfficiency: ${(f.efficiency * 100).toInt()}% | Capacity: ${f.capacity}" }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Research Facilities")
            .setItems(facilityNames) { _, which ->
                val facility = TechnologyManager.facilities[which]
                val upgradeCost = facility.maintenanceCost * 10 * facility.level
                var message = "${facility.name}\n\nType: ${facility.type}\nLevel: ${facility.level}\n"
                message += "Capacity: ${facility.capacity}\n"
                message += "Efficiency: ${(facility.efficiency * 100).toInt()}%\n"
                message += "Current Projects: ${facility.currentProjects}/${facility.maxProjects}\n"
                message += "Maintenance: $${String.format("%,d", (facility.maintenanceCost / 1000000).toLong())}M/year\n\n"
                message += "Upgrade Cost: $${String.format("%,d", (upgradeCost / 1000000).toLong())}M"
                
                AlertDialog.Builder(this)
                    .setTitle(facility.name)
                    .setMessage(message)
                    .setPositiveButton("Upgrade") { _, _ ->
                        if (TechnologyManager.upgradeFacility(facility.id, country)) {
                            showToast("${facility.name} upgraded to level ${facility.level}!")
                        } else {
                            showToast("Cannot upgrade - insufficient funds!")
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
