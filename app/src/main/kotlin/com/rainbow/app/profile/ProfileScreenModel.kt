package com.rainbow.app.profile

import com.rainbow.app.comment.CommentListModel
import com.rainbow.app.item.ItemListModel
import com.rainbow.app.model.Model
import com.rainbow.app.post.PostListModel
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.PostLayout
import com.rainbow.domain.models.User
import com.rainbow.domain.models.UserPostSorting
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

object ProfileScreenModel : Model() {

    private val mutableCurrentUser = MutableStateFlow<UIState<User>>(UIState.Loading)
    val currentUser get() = mutableCurrentUser.asStateFlow()

    private val mutableSelectedTab = MutableStateFlow(ProfileTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    val postLayout = Repos.Settings.postLayout.stateIn(scope, SharingStarted.Lazily, PostLayout.Card)

    val overViewItemListModel = ItemListModel(UserPostSorting.Default) { itemSorting, timeSorting, lastItemId ->
        Repos.Item.getCurrentUserOverviewItems(itemSorting, timeSorting, lastItemId)
    }

    val savedItemListModel = ItemListModel(UserPostSorting.Default) { itemSorting, timeSorting, lastItemId ->
        Repos.Item.getCurrentUserSavedItems(itemSorting, timeSorting, lastItemId)
    }

    val submittedPostListModel = PostListModel(UserPostSorting.Default) { postSorting, timeSorting, lastPostId ->
        Repos.Post.getCurrentUserSubmittedPosts(postSorting, timeSorting, lastPostId)
    }

    val hiddenPostListModel = PostListModel(UserPostSorting.Default) { postSorting, timeSorting, lastPostId ->
        Repos.Post.getCurrentUserHiddenPosts(postSorting, timeSorting, lastPostId)
    }

    val upvotedPostListModel = PostListModel(UserPostSorting.Default) { postSorting, timeSorting, lastPostId ->
        Repos.Post.getCurrentUserUpvotedPosts(postSorting, timeSorting, lastPostId)
    }

    val downvotedPostListModel = PostListModel(UserPostSorting.Default) { postSorting, timeSorting, lastPostId ->
        Repos.Post.getCurrentUserDownvotedPosts(postSorting, timeSorting, lastPostId)
    }

    val commentListModel = CommentListModel(UserPostSorting.Default) { commentSorting, timeSorting, lastCommentId ->
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