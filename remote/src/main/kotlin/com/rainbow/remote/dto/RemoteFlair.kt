package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RemoteFlair(
    @SerialName("allowable_content")
    val allowableContent: String? = null, // all
    @SerialName("background_color")
    val backgroundColor: String? = null, // #dadada
    @SerialName("css_class")
    val cssClass: String? = null,
    @SerialName("id")
    val id: String? = null, // 9f4d6096-80b5-11e9-b0f7-0e1079d31c44
    @SerialName("max_emojis")
    val maxEmojis: Int? = null, // 10
    @SerialName("mod_only")
    val modOnly: Boolean? = null, // false
    @SerialName("richtext")
    val richtext: List<FlairRichText>? = null,
    @SerialName("text")
    val text: String? = null, // Question
    @SerialName("text_color")
    val textColor: String? = null, // dark
    @SerialName("text_editable")
    val textEditable: Boolean? = null, // false
    @SerialName("type")
    val type: String? = null // text
)