package com.rainbow.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RulesResponse(
    @SerialName("rules")
    val rules: List<RemoteRule>? = null,
//    @SerialName("site_rules")
//    val siteRules: List<String>? = null,
//    @SerialName("site_rules_flow")
//    val siteRulesFlow: List<SiteRulesFlow>? = null
)