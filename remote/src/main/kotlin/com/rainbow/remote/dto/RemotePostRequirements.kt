package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemotePostRequirements internal constructor(
    @SerialName("body_blacklisted_strings")
    val bodyBlacklistedStrings: List<String>? = null,
    @SerialName("body_regexes")
    val bodyRegexes: List<String>? = null,
    @SerialName("body_required_strings")
    val bodyRequiredStrings: List<String>? = null,
    @SerialName("body_restriction_policy")
    val bodyRestrictionPolicy: String? = null, // notAllowed
    @SerialName("body_text_max_length")
    val bodyTextMaxLength: Int? = null, // null
    @SerialName("body_text_min_length")
    val bodyTextMinLength: Int? = null, // null
    @SerialName("domain_blacklist")
    val domainBlacklist: List<String>? = null,
    @SerialName("domain_whitelist")
    val domainWhitelist: List<String>? = null,
    @SerialName("gallery_captions_requirement")
    val galleryCaptionsRequirement: String? = null, // none
    @SerialName("gallery_max_items")
    val galleryMaxItems: Int? = null, // null
    @SerialName("gallery_min_items")
    val galleryMinItems: Int? = null, // null
    @SerialName("gallery_urls_requirement")
    val galleryUrlsRequirement: String? = null, // none
    @SerialName("guidelines_display_policy")
    val guidelinesDisplayPolicy: Int? = null, // null
    @SerialName("guidelines_text")
    val guidelinesText: String? = null, // Welcome to r/AskReddit! This subreddit is for open-ended, discussion based questions. Advice questions, questions seeking a definite answer, yes/no questions, polls and loaded questions will be removed. Please make sure that your question ends with a question mark. The exact details of what is/is not allowed can be found in the sidebar.
    @SerialName("is_flair_required")
    val isFlairRequired: Boolean? = null, // false
    @SerialName("link_repost_age")
    val linkRepostAge: Int? = null, // null
    @SerialName("link_restriction_policy")
    val linkRestrictionPolicy: String? = null, // none
    @SerialName("title_blacklisted_strings")
    val titleBlacklistedStrings: List<String>? = null,
    @SerialName("title_regexes")
    val titleRegexes: List<String>? = null,
    @SerialName("title_required_strings")
    val titleRequiredStrings: List<String>? = null,
    @SerialName("title_text_max_length")
    val titleTextMaxLength: Int? = null, // null
    @SerialName("title_text_min_length")
    val titleTextMinLength: Int? = null // 3
)