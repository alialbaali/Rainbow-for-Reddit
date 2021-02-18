package com.rainbow.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentFlair(
    @SerialName("flair_css_class")
    val flairCssClass: String? = null, // pink
    @SerialName("flair_position")
    val flairPosition: String? = null, // right
    @SerialName("flair_template_id")
    val flairTemplateId: String? = null, // 9dcdc658-cc86-11e9-a971-0ee06084fb1e
    @SerialName("flair_text")
    val flairText: String? = null // Pink
)