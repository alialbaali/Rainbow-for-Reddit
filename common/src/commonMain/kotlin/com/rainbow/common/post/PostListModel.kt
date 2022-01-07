package com.rainbow.common.post

import com.rainbow.common.model.SortedListModel
import com.rainbow.data.Repos
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostLayout
import com.rainbow.domain.models.PostSorting
import com.rainbow.domain.models.TimeSorting
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class PostListModel<S : PostSorting>(
    initialPostSorting: S,
    getPosts: suspend (S, TimeSorting, String?) -> Result<List<Post>>,
) : SortedListModel<Post, S>(initialPostSorting, getPosts) {
    override val Post.itemId: String
        get() = this.id

    val postLayout = Repos.Settings.postLayout.stateIn(scope, SharingStarted.Lazily, PostLayout.Card)
}