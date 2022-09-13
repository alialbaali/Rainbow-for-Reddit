package com.rainbow.desktop.user

import com.rainbow.data.Repos
import com.rainbow.desktop.comment.CommentsStateHolder
import com.rainbow.desktop.item.ItemsStateHolder
import com.rainbow.desktop.post.PostsStateHolder
import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.toUIState
import com.rainbow.domain.models.*
import com.rainbow.domain.repository.CommentRepository
import com.rainbow.domain.repository.ItemRepository
import com.rainbow.domain.repository.PostRepository
import com.rainbow.domain.repository.UserRepository
import kotlinx.coroutines.flow.*

class UserScreenStateHolder private constructor(
    private val userName: String,
    private val userRepository: UserRepository = Repos.User,
    private val postRepository: PostRepository = Repos.Post,
    private val commentRepository: CommentRepository = Repos.Comment,
    private val itemRepository: ItemRepository = Repos.Item,
) : StateHolder() {

    private val mutableSelectedTab = MutableStateFlow(UserTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

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
    }

    companion object {
        private val stateHolders = mutableSetOf<UserScreenStateHolder>()
        var CurrentInstance: UserScreenStateHolder? = null
            private set

        fun getInstance(userName: String): UserScreenStateHolder {
            val stateHolder = stateHolders.find { it.userName == userName }
                ?: UserScreenStateHolder(userName).also(stateHolders::add)
            CurrentInstance = stateHolder
            return stateHolder
        }
    }

    fun selectTab(tab: UserTab) {
        mutableSelectedTab.value = tab
    }
}