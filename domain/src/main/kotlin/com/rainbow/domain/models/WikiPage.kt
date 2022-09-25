package com.rainbow.domain.models

import kotlinx.datetime.Instant

data class WikiPage(
    val content: String,
    val revisionBy: User,
    val revisionDate: Instant,
)