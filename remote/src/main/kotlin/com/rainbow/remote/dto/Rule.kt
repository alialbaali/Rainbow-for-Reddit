package com.rainbow.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Rule(
    @SerialName("created_utc")
    val createdUtc: Double? = null, // 1553279015.0
    @SerialName("description")
    val description: String? = null, // Content has to have a substantive connection to the game or its fandom. Submissions of words or real-world objects that bear a resemblance to in-game characters, objects or terminology commonly used in the game are not sufficiently connected to qualify.This is a **shortened** version of the rule. Please visit this [page](https://www.reddit.com/r/AmongUs/wiki/rules) to read the extended version.
    @SerialName("description_html")
    val descriptionHtml: String? = null, // &lt;!-- SC_OFF --&gt;&lt;div class="md"&gt;&lt;p&gt;Content has to have a substantive connection to the game or its fandom. Submissions of words or real-world objects that bear a resemblance to in-game characters, objects or terminology commonly used in the game are not sufficiently connected to qualify.&lt;/p&gt;&lt;p&gt;This is a &lt;strong&gt;shortened&lt;/strong&gt; version of the rule. Please visit this &lt;a href="https://www.reddit.com/r/AmongUs/wiki/rules"&gt;page&lt;/a&gt; to read the extended version.&lt;/p&gt;&lt;/div&gt;&lt;!-- SC_ON --&gt;
    @SerialName("kind")
    val kind: String? = null, // all
    @SerialName("priority")
    val priority: Int? = null, // 0
    @SerialName("short_name")
    val shortName: String? = null, // Relevancy to Among Us
    @SerialName("violation_reason")
    val violationReason: String? = null // Relevancy to Among Us
)