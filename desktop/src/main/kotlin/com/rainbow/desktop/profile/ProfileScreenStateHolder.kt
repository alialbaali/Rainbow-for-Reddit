package com.rainbow.desktop.profile

import com.rainbow.data.Repos
import com.rainbow.desktop.comment.CommentsStateHolder
import com.rainbow.desktop.item.ItemsStateHolder
import com.rainbow.desktop.post.PostsStateHolder
import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrNull
import com.rainbow.desktop.utils.toUIState
import com.rainbow.domain.models.*
import com.rainbow.domain.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileScreenStateHolder private constructor(
    private val userRepository: UserRepository = Repos.User,
    private val itemRepository: ItemRepository = Repos.Item,
    private val postRepository: PostRepository = Repos.Post,
    private val commentRepository: CommentRepository = Repos.Comment,
    private val settingsRepository: SettingsRepository = Repos.Settings,
) : StateHolder() {

    val currentUser
        get() = userRepository.currentUser
            .map { UIState.Success(it) as UIState<User> }
            .catch { emit(UIState.Failure(null, it)) }
            .stateIn(scope, SharingStarted.Eagerly, UIState.Empty)

    private val mutableSelectedTab = MutableStateFlow(ProfileTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    private val initialPostSorting = MutableStateFlow(ProfilePostSorting.Default)

    val postLayout = settingsRepository.postLayout.stateIn(scope, SharingStarted.Eagerly, PostLayout.Default)

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

    private val mutableKarma = MutableStateFlow<UIState<List<Karma>>>(UIState.Empty)
    val karma get() = mutableKarma.asStateFlow()

    private val mutableTrophies = MutableStateFlow<UIState<List<Trophy>>>(UIState.Empty)
    val trophies get() = mutableTrophies.asStateFlow()

    private val mutableSelectedItemIds = MutableStateFlow(emptyMap<ProfileTab, String>())
    val selectedItemIds get() = mutableSelectedItemIds.asStateFlow()

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
                    ProfileTab.Karma -> if (karma.value.isEmpty) loadKarma()
                    ProfileTab.Trophies -> if (trophies.value.isEmpty) loadTrophies()
                }
            }
            .launchIn(scope)

        scope.launch {
            overviewItemsStateHolder.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { item -> selectItemId(ProfileTab.Overview, item.postId) }
        }

        scope.launch {
            submittedPostsStateHolder.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { post -> selectItemId(ProfileTab.Submitted, post.id) }
        }

        scope.launch {
            savedItemsStateHolder.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { item -> selectItemId(ProfileTab.Saved, item.postId) }
        }

        scope.launch {
            hiddenPostsStateHolder.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { post -> selectItemId(ProfileTab.Hidden, post.id) }
        }

        scope.launch {
            upvotedPostsStateHolder.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { post -> selectItemId(ProfileTab.Upvoted, post.id) }
        }

        scope.launch {
            downvotedPostsStateHolder.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { post -> selectItemId(ProfileTab.Downvoted, post.id) }
        }

        scope.launch {
            commentsStateHolder.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { comment -> selectItemId(ProfileTab.Downvoted, comment.postId) }
        }
    }

    companion object {
        val Instance = ProfileScreenStateHolder()
    }

    private fun loadUser() = scope.launch {
        userRepository.getCurrentUser()
    }

    fun selectTab(tab: ProfileTab) {
        mutableSelectedTab.value = tab
    }

    fun selectItemId(tab: ProfileTab, id: String) {
        mutableSelectedItemIds.value += tab to id
    }

    fun loadKarma() = scope.launch {
        mutableKarma.value = userRepository.getProfileKarma().toUIState()
    }

    fun loadTrophies() = scope.launch {
        mutableTrophies.value = userRepository.getProfileTrophies().toUIState()
    }
}