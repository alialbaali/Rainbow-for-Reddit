package com.rainbow.app.search

import com.rainbow.app.post.PostModel
import com.rainbow.data.Repos
import com.rainbow.domain.models.MainPostSorting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object SearchModel {

    private val mutableSearchTerm = MutableStateFlow("")
    val searchTerm get() = mutableSearchTerm.asStateFlow()

    val postModel = PostModel(MainPostSorting.Default) { postSorting, timeSorting, lastPostId ->
        Repos.Post.searchPosts(searchTerm.value)
    }

    fun setSearchTerm(searchTerm: String) {
        mutableSearchTerm.value = searchTerm
    }
}