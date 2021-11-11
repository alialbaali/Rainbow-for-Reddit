package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RemoteRule(
    @SerialName("created_utc")
    val createdUtc: Double? = null, // 1500961660.0
    @SerialName("description")
    val description: String? = null, // Submissions should be directly related to programming.
    @SerialName("description_html")
    val descriptionHtml: String? = null, // &lt;!-- SC_OFF --&gt;&lt;div class="md"&gt;&lt;p&gt;
    @SerialName("kind")
    val kind: String? = null, // link
    @SerialName("priority")
    val priority: Int? = null, // 0
    @SerialName("short_name")
    val shortName: String? = null, // Keep submissions on topic and of high quality
    @SerialName("violation_reason")
    val violationReason: String? = null // Not programming
)