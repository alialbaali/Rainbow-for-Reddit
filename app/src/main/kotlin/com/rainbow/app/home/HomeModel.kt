package com.rainbow.app.home

import com.rainbow.app.utils.Model
import com.rainbow.app.post.PostModel
import com.rainbow.data.Repos
import com.rainbow.domain.models.MainPostSorting

object HomeModel : Model() {

    val postModel = PostModel(MainPostSorting.Default) { postSorting, timeSorting, lastPostId ->
        Repos.Post.getHomePosts(postSorting, timeSorting, lastPostId)
    }

    init {
        postModel.loadPosts()
    }
}