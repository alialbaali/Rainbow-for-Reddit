package com.rainbow.desktop.profile

import com.rainbow.desktop.comment.CommentListStateHolder
import com.rainbow.desktop.item.ItemListStateHolder
import com.rainbow.desktop.model.StateHolder
import com.rainbow.desktop.post.PostListStateHolder
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.PostLayout
import com.rainbow.domain.models.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

object ProfileScreenStateHolder : StateHolder() {

    private val mutableCurrentUser = MutableStateFlow<UIState<User>>(UIState.Loading)
    val currentUser get() = mutableCurrentUser.asStateFlow()

    private val mutableSelectedTab = MutableStateFlow(ProfileTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    val postLayout = Repos.Settings.postLayout.stateIn(scope, SharingStarted.Lazily, PostLayout.Card)

    private val initialPostSorting = Repos.Settings.getProfilePostSorting()

    val overViewItemListModel = ItemListStateHolder(initialPostSorting) { itemSorting, timeSorting, lastItemId ->
        Repos.Item.getCurrentUserOverviewItems(itemSorting, timeSorting, lastItemId)
    }

    val savedItemListModel = ItemListStateHolder(initialPostSorting) { itemSorting, timeSorting, lastItemId ->
        Repos.Item.getCurrentUserSavedItems(itemSorting, timeSorting, lastItemId)
    }

    val submittedPostListModel = PostListStateHolder(initialPostSorting) { postSorting, timeSorting, lastPostId ->
        Repos.Post.getCurrentUserSubmittedPosts(postSorting, timeSorting, lastPostId)
    }

    val hiddenPostListModel = PostListStateHolder(initialPostSorting) { postSorting, timeSorting, lastPostId ->
        Repos.Post.getCurrentUserHiddenPosts(postSorting, timeSorting, lastPostId)
    }

    val upvotedPostListModel = PostListStateHolder(initialPostSorting) { postSorting, timeSorting, lastPostId ->
        Repos.Post.getCurrentUserUpvotedPosts(postSorting, timeSorting, lastPostId)
    }

    val downvotedPostListModel = PostListStateHolder(initialPostSorting) { postSorting, timeSorting, lastPostId ->
        Repos.Post.getCurrentUserDownvotedPosts(postSorting, timeSorting, lastPostId)
    }

    val commentListModel = CommentListStateHolder(initialPostSorting) { commentSorting, timeSorting, lastCommentId ->
        Repos.Comment.getCurrentUserComments(commentSorting, timeSorting, lastCommentId)
    }

    init {
        loadUser()
        selectedTab
            .onEach {
                when (it) {
                    ProfileTab.Overview -> if (overViewItemListModel.items.value.isLoading) overViewItemListModel.loadItems()
                    ProfileTab.Submitted -> if (submittedPostListModel.items.value.isLoading) submittedPostListModel.loadItems()
                    ProfileTab.Saved -> if (savedItemListModel.items.value.isLoading) savedItemListModel.loadItems()
                    ProfileTab.Hidden -> if (hiddenPostListModel.items.value.isLoading) hiddenPostListModel.loadItems()
                    ProfileTab.Upvoted -> if (upvotedPostListModel.items.value.isLoading) upvotedPostListModel.loadItems()
                    ProfileTab.Downvoted -> if (downvotedPostListModel.items.value.isLoading) downvotedPostListModel.loadItems()
                    ProfileTab.Comments -> if (commentListModel.items.value.isLoading) commentListModel.loadItems()
                }
            }
            .launchIn(scope)
    }

    private fun loadUser() = scope.launch {
        mutableCurrentUser.value = Repos.User.getCurrentUser().toUIState()
    }

    fun selectTab(tab: ProfileTab) {
        mutableSelectedTab.value = tab
    }
}