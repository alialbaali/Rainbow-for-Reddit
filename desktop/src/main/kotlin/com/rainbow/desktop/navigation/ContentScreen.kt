package com.rainbow.desktop.navigation

import com.rainbow.domain.models.Message

sealed interface ContentScreen {

    object None : ContentScreen

    @JvmInline
    value class PostEntity(val postId: String) : ContentScreen

    @JvmInline
    value class MessageEntity(val message: Message) : ContentScreen

}