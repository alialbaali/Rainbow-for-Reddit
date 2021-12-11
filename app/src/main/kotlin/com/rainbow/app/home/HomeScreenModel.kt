package com.rainbow.app.home

import com.rainbow.app.comment.HomeCommentListModel
import com.rainbow.app.model.Model
import com.rainbow.app.post.PostListModel
import com.rainbow.data.Repos
import com.rainbow.domain.models.HomePostSorting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

object HomeScreenModel : Model() {

    private val mutableSelectedTab = MutableStateFlow(HomeTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    val postListModel = PostListModel(HomePostSorting.Default) { postSorting, timeSorting, lastPostId ->
        Repos.Post.getHomePosts(postSorting, timeSorting, lastPostId)
    }

    val commentListModel = HomeCommentListModel { lastCommentId ->
        Repos.Comment.getHomeComments(lastCommentId)
    }

    init {
        selectedTab
            .onEach {
                when (it) {
                    HomeTab.Posts -> if (postListModel.items.value.isLoading) postListModel.loadItems()
                    HomeTab.Comments -> if (commentListModel.items.value.isLoading) commentListModel.loadItems()
                }
            }
            .launchIn(scope)
    }

    fun selectTab(tab: HomeTab) {
        mutableSelectedTab.value = tab
    }
}