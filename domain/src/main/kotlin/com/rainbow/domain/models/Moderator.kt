package com.rainbow.domain.models

import kotlinx.datetime.LocalDateTime

data class Moderator(
    val id: String,
    val name: String,
    val flair: Flair,
    val permissions: List<Permission>,
    val since: LocalDateTime,
) {
    enum class Permission {
        Wiki, Posts, Mail, Config,
        Flair, Access, All, ChatOperator,
        ChatConfig,
    }
}