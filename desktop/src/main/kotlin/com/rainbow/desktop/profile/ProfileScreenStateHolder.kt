package com.rainbow.desktop.profile

import com.rainbow.data.Repos
import com.rainbow.desktop.comment.CommentsStateHolder
import com.rainbow.desktop.item.ItemsStateHolder
import com.rainbow.desktop.post.PostsStateHolder
import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.utils.UIState
import com.rainbow.domain.models.*
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

    val overviewItemsStateHolder = object : ItemsStateHolder<UserPostSorting>(
        UserPostSorting.Default,
        itemRepository.profileOverviewItems,
    ) {
        override suspend fun getItems(
            sorting: UserPostSorting,
            timeSorting: TimeSorting,
            lastItem: Item?
        ): Result<Unit> = itemRepository.getProfileOverviewItems(sorting, timeSorting, lastItem?.id)
    }

    val savedItemsStateHolder = object : ItemsStateHolder<UserPostSorting>(
        UserPostSorting.Default,
        itemRepository.profileSavedItems,
    ) {
        override suspend fun getItems(
            sorting: UserPostSorting,
            timeSorting: TimeSorting,
            lastItem: Item?
        ): Result<Unit> = itemRepository.getProfileSavedItems(sorting, timeSorting, lastItem?.id)
    }

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

    val commentsStateHolder = object : CommentsStateHolder(commentRepository.profileComments) {
        override suspend fun getItems(
            sorting: UserPostSorting,
            timeSorting: TimeSorting,
            lastItem: Comment?
        ): Result<Unit> = commentRepository.getProfileComments(sorting, timeSorting, lastItem?.id)
    }

    init {
        loadUser()

        selectedTab
            .onEach {
                when (it) {
                    ProfileTab.Overview -> if (overviewItemsStateHolder.items.value.isEmpty) overviewItemsStateHolder.loadItems()
                    ProfileTab.Submitted -> if (submittedPostsStateHolder.items.value.isEmpty) submittedPostsStateHolder.loadItems()
                    ProfileTab.Saved -> if (savedItemsStateHolder.items.value.isEmpty) savedItemsStateHolder.loadItems()
                    ProfileTab.Hidden -> if (hiddenPostsStateHolder.items.value.isEmpty) hiddenPostsStateHolder.loadItems()
                    ProfileTab.Upvoted -> if (upvotedPostsStateHolder.items.value.isEmpty) upvotedPostsStateHolder.loadItems()
                    ProfileTab.Downvoted -> if (downvotedPostsStateHolder.items.value.isEmpty) downvotedPostsStateHolder.loadItems()
                    ProfileTab.Comments -> if (commentsStateHolder.items.value.isEmpty) commentsStateHolder.loadItems()
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