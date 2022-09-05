package com.rainbow.desktop.navigation

sealed interface DetailsScreen {

    object None : DetailsScreen

    @JvmInline
    value class Post(val postId: String) : DetailsScreen

    @JvmInline
    value class Message(val messageId: String) : DetailsScreen

}