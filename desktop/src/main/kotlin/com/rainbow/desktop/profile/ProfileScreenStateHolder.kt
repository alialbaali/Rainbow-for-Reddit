package com.rainbow.desktop.profile

import com.rainbow.data.Repos
import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.toUIState
import com.rainbow.domain.models.Item
import com.rainbow.domain.models.ProfilePostSorting
import com.rainbow.domain.models.User
import com.rainbow.domain.repository.CommentRepository
import com.rainbow.domain.repository.ItemRepository
import com.rainbow.domain.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileScreenStateHolder(
    private val itemRepository: ItemRepository = Repos.Item,
    private val postRepository: PostRepository = Repos.Post,
    private val commentRepository: CommentRepository = Repos.Comment,
) : StateHolder() {

    private val mutableOverviewItems = MutableStateFlow(emptyList<Item>())
    val overviewItems get() = mutableOverviewItems.asStateFlow()

    private val mutableOverviewItemsState = MutableStateFlow<UIState<Unit>>(UIState.Empty)
    val overviewItemsState get() = mutableOverviewItemsState.asStateFlow()


    private val mutableCurrentUser = MutableStateFlow<UIState<User>>(UIState.Empty)
    val currentUser get() = mutableCurrentUser.asStateFlow()

    private val mutableSelectedTab = MutableStateFlow(ProfileTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    private val initialPostSorting = MutableStateFlow(ProfilePostSorting.Default)

//    val overViewItemListModel = ItemListStateHolder(initialPostSorting) { itemSorting, timeSorting, lastItemId ->
//        itemRepository.getCurrentUserOverviewItems(itemSorting, timeSorting, lastItemId)
//    }
//
//    val savedItemListModel = ItemListStateHolder(initialPostSorting) { itemSorting, timeSorting, lastItemId ->
//        itemRepository.getCurrentUserSavedItems(itemSorting, timeSorting, lastItemId)
//    }
//
//    val submittedPostListModel = PostListStateHolder(initialPostSorting) { postSorting, timeSorting, lastPostId ->
//        postRepository.getCurrentUserSubmittedPosts(postSorting, timeSorting, lastPostId)
//    }
//
//    val hiddenPostListModel = PostListStateHolder(initialPostSorting) { postSorting, timeSorting, lastPostId ->
//        postRepository.getCurrentUserHiddenPosts(postSorting, timeSorting, lastPostId)
//    }
//
//    val upvotedPostListModel = PostListStateHolder(initialPostSorting) { postSorting, timeSorting, lastPostId ->
//        postRepository.getCurrentUserUpvotedPosts(postSorting, timeSorting, lastPostId)
//    }
//
//    val downvotedPostListModel = PostListStateHolder(initialPostSorting) { postSorting, timeSorting, lastPostId ->
//        postRepository.getCurrentUserDownvotedPosts(postSorting, timeSorting, lastPostId)
//    }
//
//    val commentListModel = CommentListStateHolder(initialPostSorting) { commentSorting, timeSorting, lastCommentId ->
//        commentRepository.getCurrentUserComments(commentSorting, timeSorting, lastCommentId)
//    }

    init {

//        loadUser()
//        selectedTab
//            .onEach {
//                when (it) {
//                    ProfileTab.Overview -> if (overViewItemListModel.items.value.isLoading) overViewItemListModel.loadItems()
//                    ProfileTab.Submitted -> if (submittedPostListModel.items.value.isLoading) submittedPostListModel.loadItems()
//                    ProfileTab.Saved -> if (savedItemListModel.items.value.isLoading) savedItemListModel.loadItems()
//                    ProfileTab.Hidden -> if (hiddenPostListModel.items.value.isLoading) hiddenPostListModel.loadItems()
//                    ProfileTab.Upvoted -> if (upvotedPostListModel.items.value.isLoading) upvotedPostListModel.loadItems()
//                    ProfileTab.Downvoted -> if (downvotedPostListModel.items.value.isLoading) downvotedPostListModel.loadItems()
//                    ProfileTab.Comments -> if (commentListModel.items.value.isLoading) commentListModel.loadItems()
//                }
//            }
//            .launchIn(scope)
    }

    private fun loadUser() = scope.launch {
        mutableCurrentUser.value = Repos.User.getCurrentUser().toUIState()
    }

    fun selectTab(tab: ProfileTab) {
        mutableSelectedTab.value = tab
    }
}