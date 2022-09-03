package com.rainbow.desktop.profile

import com.rainbow.data.Repos
import com.rainbow.desktop.post.PostsStateHolder
import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.utils.UIState
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.ProfilePostSorting
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.models.User
import com.rainbow.domain.repository.CommentRepository
import com.rainbow.domain.repository.ItemRepository
import com.rainbow.domain.repository.PostRepository
import com.rainbow.domain.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileScreenStateHolder private constructor(
    private val userRepository: UserRepository = Repos.User,
    private val itemRepository: ItemRepository = Repos.Item,
    private val postRepository: PostRepository = Repos.Post,
    private val commentRepository: CommentRepository = Repos.Comment,
) : StateHolder() {

    val currentUser
        get() = userRepository.currentUser
            .map { UIState.Success(it) as UIState<User> }
            .catch { emit(UIState.Failure(null, it)) }
            .stateIn(scope, SharingStarted.Eagerly, UIState.Empty)

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
    val submittedPostsStateHolder = object : PostsStateHolder<ProfilePostSorting>(
        ProfilePostSorting.Default,
        postRepository.profileSubmittedPosts
    ) {
        override suspend fun getItems(
            sorting: ProfilePostSorting,
            timeSorting: TimeSorting,
            lastItem: Post?
        ): Result<Unit> = postRepository.getProfileSubmittedPosts(sorting, timeSorting, lastItem?.id)
    }

    val upvotedPostsStateHolder = object : PostsStateHolder<ProfilePostSorting>(
        ProfilePostSorting.Default,
        postRepository.profileUpvotedPosts
    ) {
        override suspend fun getItems(
            sorting: ProfilePostSorting,
            timeSorting: TimeSorting,
            lastItem: Post?
        ): Result<Unit> = postRepository.getProfileUpvotedPosts(sorting, timeSorting, lastItem?.id)
    }

    val downvotedPostsStateHolder = object : PostsStateHolder<ProfilePostSorting>(
        ProfilePostSorting.Default,
        postRepository.profileDownvotedPosts
    ) {
        override suspend fun getItems(
            sorting: ProfilePostSorting,
            timeSorting: TimeSorting,
            lastItem: Post?
        ): Result<Unit> = postRepository.getProfileDownvotedPosts(sorting, timeSorting, lastItem?.id)
    }

    val hiddenPostsStateHolder = object : PostsStateHolder<ProfilePostSorting>(
        ProfilePostSorting.Default,
        postRepository.profileHiddenPosts
    ) {
        override suspend fun getItems(
            sorting: ProfilePostSorting,
            timeSorting: TimeSorting,
            lastItem: Post?
        ): Result<Unit> = postRepository.getProfileHiddenPosts(sorting, timeSorting, lastItem?.id)
    }

//    val commentListModel = CommentListStateHolder(initialPostSorting) { commentSorting, timeSorting, lastCommentId ->
//        commentRepository.getCurrentUserComments(commentSorting, timeSorting, lastCommentId)
//    }

    init {
        loadUser()

        selectedTab
            .onEach {
                when (it) {
//                    ProfileTab.Overview -> if (overViewItemListModel.items.value.isLoading) overViewItemListModel.loadItems()
                    ProfileTab.Submitted -> if (submittedPostsStateHolder.items.value.isEmpty) submittedPostsStateHolder.loadItems()
//                    ProfileTab.Saved -> if (savedItemListModel.items.value.isLoading) savedItemListModel.loadItems()
                    ProfileTab.Hidden -> if (hiddenPostsStateHolder.items.value.isEmpty) hiddenPostsStateHolder.loadItems()
                    ProfileTab.Upvoted -> if (upvotedPostsStateHolder.items.value.isEmpty) upvotedPostsStateHolder.loadItems()
                    ProfileTab.Downvoted -> if (downvotedPostsStateHolder.items.value.isEmpty) downvotedPostsStateHolder.loadItems()
//                    ProfileTab.Comments -> if (commentListModel.items.value.isLoading) commentListModel.loadItems()
                    else -> {}
                }
            }
            .launchIn(scope)
    }

    companion object {
        private var stateHolder: ProfileScreenStateHolder? = null

        fun getInstance(): ProfileScreenStateHolder {
            return stateHolder ?: ProfileScreenStateHolder().also { stateHolder = it }
        }
    }

    private fun loadUser() = scope.launch {
        userRepository.getCurrentUser()
    }

    fun selectTab(tab: ProfileTab) {
        mutableSelectedTab.value = tab
    }
}