package com.rainbow.desktop.user

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

class UserScreenStateHolder private constructor(
    private val userName: String,
    private val userRepository: UserRepository = Repos.User,
    private val postRepository: PostRepository = Repos.Post,
    private val commentRepository: CommentRepository = Repos.Comment,
    private val itemRepository: ItemRepository = Repos.Item,
    private val settingsRepository: SettingsRepository = Repos.Settings,
) : StateHolder() {

    private val mutableSelectedTab = MutableStateFlow(UserTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    val postLayout = settingsRepository.postLayout.stateIn(scope, SharingStarted.Eagerly, PostLayout.Default)

    val user = userRepository.getUser(userName)
        .map { it.toUIState() }
        .stateIn(scope, SharingStarted.Eagerly, UIState.Loading())

    private val initialPostSorting = Repos.Settings.getUserPostSorting()

    val itemsStateHolder = object : ItemsStateHolder<UserPostSorting>(
        UserPostSorting.Default,
        itemRepository.userOverviewItems
    ) {
        override suspend fun getItems(
            sorting: UserPostSorting,
            timeSorting: TimeSorting,
            lastItem: Item?
        ): Result<Unit> = itemRepository.getUserOverviewItems(userName, sorting, timeSorting, lastItem?.id)
    }

    val postsStateHolder = object : PostsStateHolder<UserPostSorting>(
        initialPostSorting,
        postRepository.userSubmittedPosts,
    ) {
        override suspend fun getItems(
            sorting: UserPostSorting,
            timeSorting: TimeSorting,
            lastItem: Post?
        ): Result<Unit> = postRepository.getUserSubmittedPosts(userName, sorting, timeSorting, lastItem?.id)
    }

    val commentsStateHolder = object : CommentsStateHolder(commentRepository.userComments) {
        override suspend fun getItems(
            sorting: UserPostSorting,
            timeSorting: TimeSorting,
            lastItem: Comment?
        ): Result<Unit> = commentRepository.getUserComments(userName, sorting, timeSorting, lastItem?.id)
    }

    private val mutableSelectedItemIds = MutableStateFlow(emptyMap<UserTab, String>())
    val selectedItemIds get() = mutableSelectedItemIds.asStateFlow()

    init {
        selectedTab
            .onEach {
                when (it) {
                    UserTab.Overview -> if (itemsStateHolder.items.value.isEmpty) itemsStateHolder.loadItems()
                    UserTab.Submitted -> if (postsStateHolder.items.value.isEmpty) postsStateHolder.loadItems()
                    UserTab.Comments -> if (commentsStateHolder.items.value.isEmpty) commentsStateHolder.loadItems()
                }
            }
            .launchIn(scope)

        scope.launch {
            itemsStateHolder.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { item -> selectItemId(UserTab.Overview, item.postId) }
        }

        scope.launch {
            postsStateHolder.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { post -> selectItemId(UserTab.Submitted, post.id) }
        }

        scope.launch {
            commentsStateHolder.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { comment -> selectItemId(UserTab.Comments, comment.postId) }
        }
    }

    companion object {
        private val stateHolders = mutableSetOf<UserScreenStateHolder>()
        var CurrentInstance: UserScreenStateHolder? = null
            private set

        fun getInstance(userName: String): UserScreenStateHolder {
            val stateHolder = stateHolders.find { it.userName == userName }
                ?: UserScreenStateHolder(userName).also(stateHolders::add)
            stateHolder.reloadContentIfNeeded()
            return stateHolder
        }

        private fun UserScreenStateHolder.reloadContentIfNeeded() {
            if (CurrentInstance?.userName != this.userName) {
                val selectedTab = selectedTab.value
                val reloadItems = !itemsStateHolder.items.value.isEmpty || selectedTab == UserTab.Overview
                val reloadPosts = !postsStateHolder.items.value.isEmpty || selectedTab == UserTab.Submitted
                val reloadComments = !commentsStateHolder.items.value.isEmpty || selectedTab == UserTab.Comments
                if (reloadItems) this.itemsStateHolder.loadItems()
                if (reloadPosts) this.postsStateHolder.loadItems()
                if (reloadComments) this.commentsStateHolder.loadItems()
                CurrentInstance = this
            }
        }
    }

    fun selectTab(tab: UserTab) {
        mutableSelectedTab.value = tab
    }

    fun selectItemId(tab: UserTab, id: String) {
        mutableSelectedItemIds.value += tab to id
    }
}