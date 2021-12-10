package com.rainbow.app.home

import com.rainbow.app.comment.CommentListModel
import com.rainbow.app.model.Model
import com.rainbow.app.post.PostListModel
import com.rainbow.data.Repos
import com.rainbow.domain.models.MainPostSorting

object HomeScreenModel : Model() {

    val postListModel = PostListModel(MainPostSorting.Default) { postSorting, timeSorting, lastPostId ->
        Repos.Post.getHomePosts(postSorting, timeSorting, lastPostId)
    }

    val commentListModel = CommentListModel(MainPostSorting.Default) { postSorting, timeSorting, lastPostId ->
        Repos.Comment.getHomeComments()
    }

    init {
        postListModel.loadItems()
        commentListModel.loadItems()
    }
}