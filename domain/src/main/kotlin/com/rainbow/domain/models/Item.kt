package com.rainbow.domain.models

sealed interface Item {
    val id: String
    val postId: String
}