package com.rainbow.app.profile

import com.rainbow.app.utils.Model
import com.rainbow.app.post.PostModel
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.User
import com.rainbow.domain.models.UserPostSorting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object ProfileModel : Model() {

    private val mutableCurrentUser = MutableStateFlow<UIState<User>>(UIState.Loading)
    val currentUser get() = mutableCurrentUser.asStateFlow()

    private val mutableTab = MutableStateFlow(ProfileTab.Default)
    val tab get() = mutableTab.asStateFlow()

    val postModel = PostModel(UserPostSorting.Default) { postSorting, timeSorting, lastPostId ->
        when (tab.value) {
            ProfileTab.Overview -> Result.success(emptyList())
            ProfileTab.Submitted -> Repos.Post.getCurrentUserSubmittedPosts(
                postSorting,
                timeSorting,
                lastPostId
            )
            ProfileTab.Saved -> Repos.Post.getCurrentUserSavedPosts(
                postSorting,
                timeSorting,
                lastPostId
            ).onFailure { throw it  }
            ProfileTab.Hidden -> Repos.Post.getCurrentUserHiddenPosts(
                postSorting,
                timeSorting,
                lastPostId
            )
            ProfileTab.Upvoted -> Repos.Post.getCurrentUserUpvotedPosts(
                postSorting,
                timeSorting,
                lastPostId
            )
            ProfileTab.Downvoted -> Repos.Post.getCurrentUserDownvotedPosts(
                postSorting,
                timeSorting,
                lastPostId
            )
            ProfileTab.Comments -> Result.success(emptyList())
        }
    }

    init {
        loadUser()
    }

    private fun loadUser() = scope.launch {
        mutableCurrentUser.value = Repos.User.getCurrentUser()
            .onSuccess { postModel.loadPosts() }
            .toUIState()
    }

    fun setTab(tab: ProfileTab) {
        mutableTab.value = tab
        postModel.loadPosts()
    }
}