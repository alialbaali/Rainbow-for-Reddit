package com.rainbow.desktop.post

import com.rainbow.desktop.state.SortedListStateHolder
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostSorting
import kotlinx.coroutines.flow.Flow

abstract class PostsStateHolder<S : PostSorting>(
    initialPostSorting: S,
    items: Flow<List<Post>>
) : SortedListStateHolder<Post, S>(initialPostSorting, items)