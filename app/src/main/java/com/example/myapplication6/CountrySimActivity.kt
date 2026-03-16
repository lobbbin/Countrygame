package com.example.myapplication6

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class CountrySimActivity : AppCompatActivity() {

    private lateinit var country: Country
    private lateinit var eventManager: EventManager
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

    private lateinit var layoutEvent: LinearLayout
    private lateinit var layoutStats: ScrollView
    private lateinit var layoutActions: LinearLayout

    private val decimalFormat = DecimalFormat("#,###")
    private val currencyFormat = DecimalFormat("$#,###,###,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_sim)

        initializeViews()
        setupClickListeners()

        // Load saved game or start new game
        if (savedInstanceState != null) {
            country = savedInstanceState.getSerializable("country") as Country
        } else {
            startNewGame()
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
    }

    private fun startNewGame() {
        country = Country()
        country.name = "Your Nation"
        country.leaderName = "President"
        country.capitalCity = "Capital City"
        country.governmentType = "Democracy"

        // Initialize game systems
        NPCManager.initializeNPCs()
        GameWorld.initializeWorld()
        eventManager = EventManager

        updateUI()
        showToast("Welcome, President! Lead your nation to prosperity.")
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
        country.turn++
        country.year++

        // Apply passive effects based on stats
        applyPassiveEffects()

        // Update game world
        GameWorld.updateWorldState(country)
        GameWorld.processActiveEvents(country)

        // Update NPCs
        NPCManager.updateAllNPCMoods(country)
        NPCManager.processNPCTurns(country)
        NPCManager.checkNPCTriggerConditions(country)

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
    }

    private fun applyPassiveEffects() {
        // GDP growth based on various factors
        val gdpGrowthRate = ((country.education + country.infrastructure + country.stability) / 300.0) - 0.05
        
        // Apply global economy modifier
        val economyModifier = when (GameWorld.globalEconomyState) {
            EconomyState.BOOMING -> 1.1
            EconomyState.STABLE -> 1.0
            EconomyState.SLOWING -> 0.95
            EconomyState.RECESSION -> 0.9
        }
        
        country.gdp = (country.gdp * (1 + gdpGrowthRate) * economyModifier).toLong()

        // Population growth
        val populationGrowthRate = ((country.healthcare + country.happiness) / 200.0) - 0.02
        country.population = (country.population * (1 + populationGrowthRate)).toLong()

        // Treasury changes
        val taxRevenue = country.gdp * 0.02 // 2% tax per turn
        
        // Apply regional bonuses
        val regionBonus = GameWorld.getUnlockedRegions().sumOf { region ->
            val bonus = GameWorld.getRegionBonus(region)
            (bonus["tax"] ?: 1.0 - 1.0) * country.gdp * 0.001
        }
        
        val expenses = country.population * 10 + country.military * 100000 + country.education * 50000
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
    }

    private fun triggerRandomEvent() {
        currentEvent = eventManager.getRandomEvent()
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
        val report = GameWorld.getWorldStatusReport(country)
        
        AlertDialog.Builder(this)
            .setTitle("World Status Report")
            .setMessage(report)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showRegionsMenu() {
        val unlockedRegions = GameWorld.getUnlockedRegions()
        val allRegions = GameWorld.getAllRegions()
        
        val regionNames = allRegions.map { region ->
            val status = if (region.isUnlocked) {
                "✓ ${region.name} (Dev: ${region.development}%, Loyalty: ${region.loyalty}%)"
            } else {
                "🔒 ${region.name} - Locked"
            }
        }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Regions")
            .setItems(regionNames) { _, which ->
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
            .show()
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
