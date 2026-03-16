package com.example.myapplication6

import java.io.Serializable

/**
 * Represents a random event that can occur in the game
 */
data class GameEvent(
    val id: Int,
    val title: String,
    val description: String,
    val choices: List<EventChoice>
) : Serializable

/**
 * Represents a choice within an event
 */
data class EventChoice(
    val text: String,
    val effects: Map<String, Int>,
    val message: String
) : Serializable

/**
 * Event manager that handles all game events
 */
object EventManager : Serializable {
    
    private val events = listOf(
        GameEvent(
            id = 1,
            title = "Economic Crisis",
            description = "A global economic downturn is affecting your country. Markets are falling and unemployment is rising.",
            choices = listOf(
                EventChoice(
                    text = "Implement stimulus package (-20 treasury, +10 happiness, -5 stability)",
                    effects = mapOf("treasury" to -20000000, "happiness" to 10, "stability" to -5),
                    message = "You implemented a stimulus package. The people appreciate it, but critics worry about debt."
                ),
                EventChoice(
                    text = "Cut government spending (+15 treasury, -15 happiness, -10 stability)",
                    effects = mapOf("treasury" to 15000000, "happiness" to -15, "stability" to -10),
                    message = "Spending cuts have saved money but caused public outrage."
                ),
                EventChoice(
                    text = "Seek international aid (+10 treasury, -10 international relations)",
                    effects = mapOf("treasury" to 10000000, "internationalRelations" to -10),
                    message = "International aid helped, but your country's reputation has suffered."
                )
            )
        ),
        GameEvent(
            id = 2,
            title = "Natural Disaster",
            description = "A massive earthquake has struck a major city. Thousands are homeless and infrastructure is devastated.",
            choices = listOf(
                EventChoice(
                    text = "Launch full relief effort (-30 treasury, +15 happiness, -10 infrastructure)",
                    effects = mapOf("treasury" to -30000000, "happiness" to 15, "infrastructure" to -10),
                    message = "Your relief efforts were praised. The people thank you for your swift action."
                ),
                EventChoice(
                    text = "Limited aid (-10 treasury, -20 happiness, -15 infrastructure)",
                    effects = mapOf("treasury" to -10000000, "happiness" to -20, "infrastructure" to -15),
                    message = "Your limited response has been criticized as inadequate."
                ),
                EventChoice(
                    text = "Request international help (+20 treasury, -5 international relations, -20 infrastructure)",
                    effects = mapOf("treasury" to 20000000, "internationalRelations" to -5, "infrastructure" to -20),
                    message = "International teams helped, but dependency on foreign aid concerns some."
                )
            )
        ),
        GameEvent(
            id = 3,
            title = "Political Scandal",
            description = "A major corruption scandal has been uncovered involving high-ranking officials in your government.",
            choices = listOf(
                EventChoice(
                    text = "Investigate thoroughly (-5 stability, +10 happiness, +5 education)",
                    effects = mapOf("stability" to -5, "happiness" to 10, "education" to 5),
                    message = "Your commitment to transparency has been appreciated by the people."
                ),
                EventChoice(
                    text = "Cover it up (+5 stability, -20 happiness, -10 education)",
                    effects = mapOf("stability" to 5, "happiness" to -20, "education" to -10),
                    message = "The cover-up worked temporarily, but whispers of corruption persist."
                ),
                EventChoice(
                    text = "Blame opposition (+10 stability, -15 happiness, -5 international relations)",
                    effects = mapOf("stability" to 10, "happiness" to -15, "internationalRelations" to -5),
                    message = "Your deflection worked, but trust in government continues to erode."
                )
            )
        ),
        GameEvent(
            id = 4,
            title = "Technological Breakthrough",
            description = "Scientists at a national university have made a groundbreaking discovery that could benefit the economy.",
            choices = listOf(
                EventChoice(
                    text = "Fund commercialization (-15 treasury, +20 GDP, +10 education)",
                    effects = mapOf("treasury" to -15000000, "gdp" to 20000000, "education" to 10),
                    message = "The technology is now being commercialized, boosting the economy."
                ),
                EventChoice(
                    text = "Sell rights to foreign companies (+25 treasury, -5 education)",
                    effects = mapOf("treasury" to 25000000, "education" to -5),
                    message = "The sale brought quick cash but long-term benefits go to other nations."
                ),
                EventChoice(
                    text = "Keep it research only (-5 treasury, +15 education, +5 international relations)",
                    effects = mapOf("treasury" to -5000000, "education" to 15, "internationalRelations" to 5),
                    message = "The discovery advances human knowledge but provides limited economic benefit."
                )
            )
        ),
        GameEvent(
            id = 5,
            title = "Military Tension",
            description = "A neighboring country is massing troops on your border, claiming territorial disputes.",
            choices = listOf(
                EventChoice(
                    text = "Mobilize military (-20 treasury, +15 military, -10 international relations)",
                    effects = mapOf("treasury" to -20000000, "military" to 15, "internationalRelations" to -10),
                    message = "Your show of force has deterred aggression but increased regional tensions."
                ),
                EventChoice(
                    text = "Seek diplomatic solution (+5 treasury, -5 military, +10 international relations)",
                    effects = mapOf("treasury" to 5000000, "military" to -5, "internationalRelations" to 10),
                    message = "Diplomacy worked, but some see you as weak for not standing firm."
                ),
                EventChoice(
                    text = "Preemptive strike (-40 treasury, -20 military, -30 international relations, -15 stability)",
                    effects = mapOf("treasury" to -40000000, "military" to -20, "internationalRelations" to -30, "stability" to -15),
                    message = "The strike was controversial. International condemnation follows."
                )
            )
        ),
        GameEvent(
            id = 6,
            title = "Public Health Crisis",
            description = "A new infectious disease is spreading rapidly through major cities.",
            choices = listOf(
                EventChoice(
                    text = "Nationwide lockdown (-25 treasury, -10 GDP, +10 healthcare, +5 happiness)",
                    effects = mapOf("treasury" to -25000000, "gdp" to -10000000, "healthcare" to 10, "happiness" to 5),
                    message = "The lockdown saved lives but the economic impact is severe."
                ),
                EventChoice(
                    text = "Targeted measures (-10 treasury, -5 GDP, +5 healthcare)",
                    effects = mapOf("treasury" to -10000000, "gdp" to -5000000, "healthcare" to 5),
                    message = "Balanced approach, though some say it wasn't enough."
                ),
                EventChoice(
                    text = "Minimal intervention (+5 treasury, -20 healthcare, -25 happiness)",
                    effects = mapOf("treasury" to 5000000, "healthcare" to -20, "happiness" to -25),
                    message = "Business continues but hospitals are overwhelmed."
                )
            )
        ),
        GameEvent(
            id = 7,
            title = "Energy Crisis",
            description = "Global energy prices have skyrocketed. Citizens are struggling with bills and industries are suffering.",
            choices = listOf(
                EventChoice(
                    text = "Subsidize energy costs (-30 treasury, +15 happiness, -5 environment)",
                    effects = mapOf("treasury" to -30000000, "happiness" to 15, "environment" to -5),
                    message = "Subsidies helped people but increased reliance on fossil fuels."
                ),
                EventChoice(
                    text = "Invest in renewables (-20 treasury, +10 environment, +5 education)",
                    effects = mapOf("treasury" to -20000000, "environment" to 10, "education" to 5),
                    message = "Green investment positions you well for the future."
                ),
                EventChoice(
                    text = "Let market decide (-5 happiness, -10 stability, +5 treasury)",
                    effects = mapOf("happiness" to -5, "stability" to -10, "treasury" to 5000000),
                    message = "Laissez-faire approach saves money but people suffer."
                )
            )
        ),
        GameEvent(
            id = 8,
            title = "Immigration Wave",
            description = "A large number of refugees are arriving at your borders, fleeing conflict in neighboring regions.",
            choices = listOf(
                EventChoice(
                    text = "Accept refugees (-15 treasury, +10 international relations, +5 population)",
                    effects = mapOf("treasury" to -15000000, "internationalRelations" to 10, "population" to 50000),
                    message = "Your humanitarian stance is praised internationally."
                ),
                EventChoice(
                    text = "Limited acceptance (-5 treasury, +5 international relations)",
                    effects = mapOf("treasury" to -5000000, "internationalRelations" to 5),
                    message = "A compromise that satisfies no one completely."
                ),
                EventChoice(
                    text = "Close borders (+5 stability, -20 international relations, -10 happiness)",
                    effects = mapOf("stability" to 5, "internationalRelations" to -20, "happiness" to -10),
                    message = "Border closure is controversial, drawing international criticism."
                )
            )
        ),
        GameEvent(
            id = 9,
            title = "Trade Opportunity",
            description = "A major economic power offers a lucrative trade deal that could boost your economy.",
            choices = listOf(
                EventChoice(
                    text = "Accept the deal (+30 GDP, +10 treasury, -5 international relations with others)",
                    effects = mapOf("gdp" to 30000000, "treasury" to 10000000, "internationalRelations" to -5),
                    message = "The deal boosts economy but some allies feel sidelined."
                ),
                EventChoice(
                    text = "Negotiate better terms (+15 GDP, +5 treasury)",
                    effects = mapOf("gdp" to 15000000, "treasury" to 5000000),
                    message = "Patient negotiation paid off with a fair deal for all."
                ),
                EventChoice(
                    text = "Reject the deal (-5 GDP, +10 international relations)",
                    effects = mapOf("gdp" to -5000000, "internationalRelations" to 10),
                    message = "Standing firm earned respect but missed economic opportunity."
                )
            )
        ),
        GameEvent(
            id = 10,
            title = "Education Reform",
            description = "Teachers and parents are demanding major reforms to the education system.",
            choices = listOf(
                EventChoice(
                    text = "Increase education budget (-25 treasury, +20 education, +10 happiness)",
                    effects = mapOf("treasury" to -25000000, "education" to 20, "happiness" to 10),
                    message = "Education investment is popular and shows promise for the future."
                ),
                EventChoice(
                    text = "Privatize schools (+10 treasury, -15 education, -10 happiness)",
                    effects = mapOf("treasury" to 10000000, "education" to -15, "happiness" to -10),
                    message = "Privatization saves money but quality of education suffers."
                ),
                EventChoice(
                    text = "Moderate reforms (-10 treasury, +10 education)",
                    effects = mapOf("treasury" to -10000000, "education" to 10),
                    message = "Balanced approach brings steady improvement."
                )
            )
        ),
        GameEvent(
            id = 11,
            title = "Infrastructure Collapse",
            description = "A major bridge has collapsed, revealing widespread infrastructure neglect.",
            choices = listOf(
                EventChoice(
                    text = "Emergency repairs (-35 treasury, +15 infrastructure)",
                    effects = mapOf("treasury" to -35000000, "infrastructure" to 15),
                    message = "Swift repairs restore confidence in your leadership."
                ),
                EventChoice(
                    text = "Patchwork fixes (-15 treasury, +5 infrastructure, -5 stability)",
                    effects = mapOf("treasury" to -15000000, "infrastructure" to 5, "stability" to -5),
                    message = "Quick fixes work temporarily but problems persist."
                ),
                EventChoice(
                    text = "Private contractors (-20 treasury, +10 infrastructure, -5 happiness)",
                    effects = mapOf("treasury" to -20000000, "infrastructure" to 10, "happiness" to -5),
                    message = "Private work is efficient but costly and unpopular."
                )
            )
        ),
        GameEvent(
            id = 12,
            title = "Environmental Disaster",
            description = "A major oil spill is devastating coastal ecosystems and fishing industries.",
            choices = listOf(
                EventChoice(
                    text = "Full cleanup (-30 treasury, +20 environment, +10 happiness)",
                    effects = mapOf("treasury" to -30000000, "environment" to 20, "happiness" to 10),
                    message = "Cleanup efforts are praised by environmental groups."
                ),
                EventChoice(
                    text = "Fine the company (+15 treasury, +5 environment)",
                    effects = mapOf("treasury" to 15000000, "environment" to 5),
                    message = "Fines help but environmental damage continues."
                ),
                EventChoice(
                    text = "Minimal response (-15 environment, -20 happiness)",
                    effects = mapOf("environment" to -15, "happiness" to -20),
                    message = "Inaction angers environmentalists and coastal communities."
                )
            )
        ),
        GameEvent(
            id = 13,
            title = "Sports Victory",
            description = "Your national team has won a major international tournament!",
            choices = listOf(
                EventChoice(
                    text = "National celebration (-10 treasury, +20 happiness, +5 stability)",
                    effects = mapOf("treasury" to -10000000, "happiness" to 20, "stability" to 5),
                    message = "The celebration boosts national morale tremendously!"
                ),
                EventChoice(
                    text = "Invest in sports facilities (-20 treasury, +15 happiness, +10 education)",
                    effects = mapOf("treasury" to -20000000, "happiness" to 15, "education" to 10),
                    message = "Sports investment inspires the next generation."
                ),
                EventChoice(
                    text = "Modest recognition (+5 happiness)",
                    effects = mapOf("happiness" to 5),
                    message = "A quiet acknowledgment of the achievement."
                )
            )
        ),
        GameEvent(
            id = 14,
            title = "Cyber Attack",
            description = "Government systems have been hit by a sophisticated cyber attack. Data may be compromised.",
            choices = listOf(
                EventChoice(
                    text = "Invest in cybersecurity (-25 treasury, +15 military, +5 stability)",
                    effects = mapOf("treasury" to -25000000, "military" to 15, "stability" to 5),
                    message = "Cybersecurity investment protects against future attacks."
                ),
                EventChoice(
                    text = "Blame foreign adversary (-5 treasury, -15 international relations, +5 stability)",
                    effects = mapOf("treasury" to -5000000, "internationalRelations" to -15, "stability" to 5),
                    message = "Accusations rally the nation but strain diplomatic ties."
                ),
                EventChoice(
                    text = "Downplay the incident (-10 stability, -5 happiness)",
                    effects = mapOf("stability" to -10, "happiness" to -5),
                    message = "Downplaying erodes trust in government transparency."
                )
            )
        ),
        GameEvent(
            id = 15,
            title = "Cultural Renaissance",
            description = "Your country is experiencing a surge in arts and culture, gaining international recognition.",
            choices = listOf(
                EventChoice(
                    text = "Fund cultural programs (-15 treasury, +15 happiness, +10 international relations)",
                    effects = mapOf("treasury" to -15000000, "happiness" to 15, "internationalRelations" to 10),
                    message = "Cultural investment enriches national identity."
                ),
                EventChoice(
                    text = "Promote tourism (+20 treasury, +5 happiness, -5 environment)",
                    effects = mapOf("treasury" to 20000000, "happiness" to 5, "environment" to -5),
                    message = "Tourism brings revenue but strains local environments."
                ),
                EventChoice(
                    text = "Let it grow naturally (+5 happiness, +5 international relations)",
                    effects = mapOf("happiness" to 5, "internationalRelations" to 5),
                    message = "Organic cultural growth continues steadily."
                )
            )
        ),
        GameEvent(
            id = 16,
            title = "Banking Crisis",
            description = "A major bank is on the verge of collapse, threatening the entire financial system.",
            choices = listOf(
                EventChoice(
                    text = "Bailout (-40 treasury, +20 stability, -10 happiness)",
                    effects = mapOf("treasury" to -40000000, "stability" to 20, "happiness" to -10),
                    message = "The bailout saved the economy but people are angry at Wall Street."
                ),
                EventChoice(
                    text = "Let it fail (-30 stability, -20 GDP, +10 happiness)",
                    effects = mapOf("stability" to -30, "gdp" to -20000000, "happiness" to 10),
                    message = "Letting it fail was popular but caused economic chaos."
                ),
                EventChoice(
                    text = "Nationalize the bank (-20 treasury, +10 stability, -5 international relations)",
                    effects = mapOf("treasury" to -20000000, "stability" to 10, "internationalRelations" to -5),
                    message = "Nationalization stabilizes things but worries investors."
                )
            )
        ),
        GameEvent(
            id = 17,
            title = "Drought",
            description = "Severe drought is affecting agriculture and water supplies across the country.",
            choices = listOf(
                EventChoice(
                    text = "Water rationing (-10 treasury, -5 happiness, +5 environment)",
                    effects = mapOf("treasury" to -10000000, "happiness" to -5, "environment" to 5),
                    message = "Rationing is unpopular but necessary."
                ),
                EventChoice(
                    text = "Build desalination plants (-35 treasury, +15 infrastructure, +10 happiness)",
                    effects = mapOf("treasury" to -35000000, "infrastructure" to 15, "happiness" to 10),
                    message = "Desalination plants provide long-term water security."
                ),
                EventChoice(
                    text = "Import water (-20 treasury, -5 environment)",
                    effects = mapOf("treasury" to -20000000, "environment" to -5),
                    message = "Imported water helps but isn't sustainable."
                )
            )
        ),
        GameEvent(
            id = 18,
            title = "Space Program Success",
            description = "Your national space agency has successfully launched a satellite mission.",
            choices = listOf(
                EventChoice(
                    text = "Expand space program (-30 treasury, +15 education, +10 international relations)",
                    effects = mapOf("treasury" to -30000000, "education" to 15, "internationalRelations" to 10),
                    message = "Space investment inspires national pride and scientific advancement."
                ),
                EventChoice(
                    text = "Commercialize space tech (+20 treasury, +5 education)",
                    effects = mapOf("treasury" to 20000000, "education" to 5),
                    message = "Commercialization brings profit and innovation."
                ),
                EventChoice(
                    text = "Maintain current program (+5 education, +5 international relations)",
                    effects = mapOf("education" to 5, "internationalRelations" to 5),
                    message = "Steady progress continues in space exploration."
                )
            )
        ),
        GameEvent(
            id = 19,
            title = "Labor Strikes",
            description = "Major labor unions are calling for nationwide strikes over wage disputes.",
            choices = listOf(
                EventChoice(
                    text = "Meet union demands (-15 treasury, +10 happiness, -5 GDP)",
                    effects = mapOf("treasury" to -15000000, "happiness" to 10, "gdp" to -5000000),
                    message = "Meeting demands ends strikes but increases labor costs."
                ),
                EventChoice(
                    text = "Hold firm (-10 happiness, -15 stability, +5 treasury)",
                    effects = mapOf("happiness" to -10, "stability" to -15, "treasury" to 5000000),
                    message = "Standing firm saves money but causes prolonged unrest."
                ),
                EventChoice(
                    text = "Negotiate compromise (-5 treasury, +5 happiness)",
                    effects = mapOf("treasury" to -5000000, "happiness" to 5),
                    message = "Compromise satisfies both sides moderately."
                )
            )
        ),
        GameEvent(
            id = 20,
            title = "Foreign Investment",
            description = "A multinational corporation wants to build major facilities in your country.",
            choices = listOf(
                EventChoice(
                    text = "Offer incentives (-10 treasury, +25 GDP, +10 population)",
                    effects = mapOf("treasury" to -10000000, "gdp" to 25000000, "population" to 100000),
                    message = "Incentives attract investment and create jobs."
                ),
                EventChoice(
                    text = "Demand environmental standards (+5 treasury, +5 environment, +15 GDP)",
                    effects = mapOf("treasury" to 5000000, "environment" to 5, "gdp" to 15000000),
                    message = "Environmental standards ensure sustainable development."
                ),
                EventChoice(
                    text = "Reject the offer (-5 GDP, +5 environment)",
                    effects = mapOf("gdp" to -5000000, "environment" to 5),
                    message = "Rejection protects environment but misses economic opportunity."
                )
            )
        ),
        GameEvent(
            id = 21,
            title = "Pandemic Preparedness",
            description = "Health experts warn that your country is unprepared for a potential pandemic.",
            choices = listOf(
                EventChoice(
                    text = "Build stockpiles (-25 treasury, +20 healthcare)",
                    effects = mapOf("treasury" to -25000000, "healthcare" to 20),
                    message = "Stockpiles prepare you for future health emergencies."
                ),
                EventChoice(
                    text = "Train medical staff (-15 treasury, +15 healthcare, +5 education)",
                    effects = mapOf("treasury" to -15000000, "healthcare" to 15, "education" to 5),
                    message = "Training improves healthcare capacity."
                ),
                EventChoice(
                    text = "Wait and see (+5 treasury)",
                    effects = mapOf("treasury" to 5000000),
                    message = "Delaying saves money now but leaves you vulnerable."
                )
            )
        ),
        GameEvent(
            id = 22,
            title = "Housing Crisis",
            description = "Housing prices have skyrocketed, making homes unaffordable for many citizens.",
            choices = listOf(
                EventChoice(
                    text = "Build public housing (-30 treasury, +20 happiness, +5 infrastructure)",
                    effects = mapOf("treasury" to -30000000, "happiness" to 20, "infrastructure" to 5),
                    message = "Public housing helps many afford homes."
                ),
                EventChoice(
                    text = "Subsidize mortgages (-20 treasury, +15 happiness)",
                    effects = mapOf("treasury" to -20000000, "happiness" to 15),
                    message = "Subsidies help but may inflate prices further."
                ),
                EventChoice(
                    text = "Deregulate construction (+10 GDP, -5 environment, +5 happiness)",
                    effects = mapOf("gdp" to 10000000, "environment" to -5, "happiness" to 5),
                    message = "Deregulation increases supply but environmental concerns grow."
                )
            )
        ),
        GameEvent(
            id = 23,
            title = "International Summit",
            description = "Your country has been chosen to host a major international summit.",
            choices = listOf(
                EventChoice(
                    text = "Go all out (-35 treasury, +25 international relations, +10 happiness)",
                    effects = mapOf("treasury" to -35000000, "internationalRelations" to 25, "happiness" to 10),
                    message = "The spectacular summit elevates your country's global standing."
                ),
                EventChoice(
                    text = "Modest event (-15 treasury, +10 international relations)",
                    effects = mapOf("treasury" to -15000000, "internationalRelations" to 10),
                    message = "A modest but effective summit."
                ),
                EventChoice(
                    text = "Decline to host (+5 treasury, -5 international relations)",
                    effects = mapOf("treasury" to 5000000, "internationalRelations" to -5),
                    message = "Declining saves money but disappoints allies."
                )
            )
        ),
        GameEvent(
            id = 24,
            title = "Pension Reform",
            description = "The pension system is facing a funding shortfall as the population ages.",
            choices = listOf(
                EventChoice(
                    text = "Raise retirement age (-20 happiness, +15 treasury, -5 stability)",
                    effects = mapOf("happiness" to -20, "treasury" to 15000000, "stability" to -5),
                    message = "Raising retirement age fixes finances but angers workers."
                ),
                EventChoice(
                    text = "Increase contributions (-15 happiness, +20 treasury)",
                    effects = mapOf("happiness" to -15, "treasury" to 20000000),
                    message = "Higher contributions solve the problem but reduce take-home pay."
                ),
                EventChoice(
                    text = "Find alternative funding (-25 treasury, +5 happiness)",
                    effects = mapOf("treasury" to -25000000, "happiness" to 5),
                    message = "Alternative funding is popular but costly."
                )
            )
        ),
        GameEvent(
            id = 25,
            title = "AI Revolution",
            description = "Artificial intelligence is transforming industries, causing both excitement and job displacement.",
            choices = listOf(
                EventChoice(
                    text = "Embrace AI fully (+20 GDP, -15 happiness, +10 education)",
                    effects = mapOf("gdp" to 20000000, "happiness" to -15, "education" to 10),
                    message = "AI adoption boosts economy but workers fear job loss."
                ),
                EventChoice(
                    text = "Regulate heavily (-10 GDP, +10 happiness, +5 stability)",
                    effects = mapOf("gdp" to -10000000, "happiness" to 10, "stability" to 5),
                    message = "Regulation protects workers but slows innovation."
                ),
                EventChoice(
                    text = "Balanced approach (+10 GDP, +5 happiness, +5 education)",
                    effects = mapOf("gdp" to 10000000, "happiness" to 5, "education" to 5),
                    message = "Balance allows progress while managing disruption."
                )
            )
        )
    )

    fun getRandomEvent(): GameEvent {
        return events.random()
    }
}
