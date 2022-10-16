package com.rainbow.domain.models

import kotlinx.datetime.Instant

data class Trophy(
    val name: String,
    val imageUrl: String,
    val grantedAt: Instant?,
)