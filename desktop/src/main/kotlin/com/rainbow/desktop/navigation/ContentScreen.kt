package com.rainbow.desktop.navigation

sealed interface ContentScreen {

    object None : ContentScreen

    @JvmInline
    value class Post(val postId: String) : ContentScreen

    @JvmInline
    value class Message(val message: com.rainbow.domain.models.Message) : ContentScreen

}