package com.rainbow.desktop.navigation

import com.rainbow.domain.models.Message
import com.rainbow.domain.models.Post

sealed interface ContentScreen {

    object None : ContentScreen

    @JvmInline
    value class CommentEntity(val postId: String) : ContentScreen

    @JvmInline
    value class PostEntity(val post: Post) : ContentScreen

    @JvmInline
    value class MessageEntity(val message: Message) : ContentScreen

}