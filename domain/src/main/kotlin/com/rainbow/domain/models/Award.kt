package com.rainbow.domain.models

data class Award(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val count: Int,
    val price: Long, // TODO Make sure if we need this or not!
)