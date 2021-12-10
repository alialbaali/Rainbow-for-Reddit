package com.rainbow.app.profile

import com.rainbow.app.item.ItemListModel
import com.rainbow.app.model.Model
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.User
import com.rainbow.domain.models.UserPostSorting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object ProfileScreenModel : Model() {

    private val mutableCurrentUser = MutableStateFlow<UIState<User>>(UIState.Loading)
    val currentUser get() = mutableCurrentUser.asStateFlow()

    private val mutableSelectedTab = MutableStateFlow(ProfileTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    val itemListModel = ItemListModel(UserPostSorting.Default) { postSorting, timeSorting, lastItemId ->
        when (selectedTab.value) {
            ProfileTab.Overview -> Repos.Item.getCurrentUserOverviewItems(postSorting, timeSorting, lastItemId)
            ProfileTab.Submitted -> Repos.Post.getCurrentUserSubmittedPosts(postSorting, timeSorting, lastItemId)
            ProfileTab.Saved -> Repos.Item.getCurrentUserSavedItems(postSorting, timeSorting, lastItemId)
            ProfileTab.Hidden -> Repos.Post.getCurrentUserHiddenPosts(postSorting, timeSorting, lastItemId)
            ProfileTab.Upvoted -> Repos.Post.getCurrentUserUpvotedPosts(postSorting, timeSorting, lastItemId)
            ProfileTab.Downvoted -> Repos.Post.getCurrentUserDownvotedPosts(postSorting, timeSorting, lastItemId)
            ProfileTab.Comments -> Repos.Comment.getCurrentUserComments()
        }
    }

    init {
        loadUser()
    }

    private fun loadUser() = scope.launch {
        mutableCurrentUser.value = Repos.User.getCurrentUser()
            .onSuccess { itemListModel.loadItems() }
            .toUIState()
    }

    fun selectTab(tab: ProfileTab) {
        mutableSelectedTab.value = tab
        itemListModel.loadItems()
    }
}