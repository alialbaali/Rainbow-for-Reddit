package com.rainbow.remote.dto

import com.rainbow.remote.Item
import kotlinx.serialization.Serializable

@Serializable
internal class PostWithComments internal constructor(
    val posts: List<Item<RemotePost>>,
    val comments: List<Item<RemoteComment>>,
)