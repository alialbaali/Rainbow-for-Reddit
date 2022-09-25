package com.rainbow.domain.models

import kotlinx.datetime.Instant

data class Moderator(
    val id: String,
    val name: String,
    val flair: Flair,
    val permissions: List<Permission>,
    val since: Instant,
) {
    enum class Permission {
        Wiki, Posts, Mail, Config,
        Flair, Access, All, ChatOperator,
        ChatConfig,
    }
}