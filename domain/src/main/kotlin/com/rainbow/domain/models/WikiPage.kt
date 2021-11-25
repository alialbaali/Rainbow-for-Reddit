package com.rainbow.domain.models

import kotlinx.datetime.LocalDateTime

data class WikiPage(
    val content: String,
    val revisionBy: User,
    val revisionDate: LocalDateTime,
)