package com.rainbow.desktop.post

import com.rainbow.data.Repos
import com.rainbow.desktop.model.SortedListStateHolder
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostLayout
import com.rainbow.domain.models.PostSorting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

abstract class PostsStateHolder<S : PostSorting>(initialPostSorting: S, items: Flow<List<Post>>) :
    SortedListStateHolder<Post, S>(initialPostSorting, items) {

    val postLayout = Repos.Settings.postLayout.stateIn(scope, SharingStarted.Lazily, PostLayout.Card)
}