package com.rainbow.common.profile

import com.rainbow.common.comment.CommentListModel
import com.rainbow.common.item.ItemListModel
import com.rainbow.common.model.Model
import com.rainbow.common.post.PostListModel
import com.rainbow.common.utils.UIState
import com.rainbow.common.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.PostLayout
import com.rainbow.domain.models.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

object ProfileScreenModel : Model() {

    private val mutableCurrentUser = MutableStateFlow<UIState<User>>(UIState.Loading)
    val currentUser get() = mutableCurrentUser.asStateFlow()

    private val mutableSelectedTab = MutableStateFlow(ProfileTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    val postLayout = Repos.Settings.postLayout.stateIn(scope, SharingStarted.Lazily, PostLayout.Card)

    private val initialPostSorting = Repos.Settings.getProfilePostSorting()

    val overViewItemListModel = ItemListModel(initialPostSorting) { itemSorting, timeSorting, lastItemId ->
        Repos.Item.getCurrentUserOverviewItems(itemSorting, timeSorting, lastItemId)
    }

    val savedItemListModel = ItemListModel(initialPostSorting) { itemSorting, timeSorting, lastItemId ->
        Repos.Item.getCurrentUserSavedItems(itemSorting, timeSorting, lastItemId)
    }

    val submittedPostListModel = PostListModel(initialPostSorting) { postSorting, timeSorting, lastPostId ->
        Repos.Post.getCurrentUserSubmittedPosts(postSorting, timeSorting, lastPostId)
    }

    val hiddenPostListModel = PostListModel(initialPostSorting) { postSorting, timeSorting, lastPostId ->
        Repos.Post.getCurrentUserHiddenPosts(postSorting, timeSorting, lastPostId)
    }

    val upvotedPostListModel = PostListModel(initialPostSorting) { postSorting, timeSorting, lastPostId ->
        Repos.Post.getCurrentUserUpvotedPosts(postSorting, timeSorting, lastPostId)
    }

    val downvotedPostListModel = PostListModel(initialPostSorting) { postSorting, timeSorting, lastPostId ->
        Repos.Post.getCurrentUserDownvotedPosts(postSorting, timeSorting, lastPostId)
    }

    val commentListModel = CommentListModel(initialPostSorting) { commentSorting, timeSorting, lastCommentId ->
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