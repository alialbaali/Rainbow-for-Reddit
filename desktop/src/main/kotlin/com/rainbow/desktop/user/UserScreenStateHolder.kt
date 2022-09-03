package com.rainbow.desktop.user

import com.rainbow.data.Repos
import com.rainbow.desktop.comment.CommentsStateHolder
import com.rainbow.desktop.post.PostsStateHolder
import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.toUIState
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.models.UserPostSorting
import com.rainbow.domain.repository.CommentRepository
import com.rainbow.domain.repository.PostRepository
import com.rainbow.domain.repository.UserRepository
import kotlinx.coroutines.flow.*

private val userScreenModels = mutableSetOf<UserScreenStateHolder>()

class UserScreenStateHolder private constructor(
    private val userName: String,
    private val userRepository: UserRepository = Repos.User,
    private val postRepository: PostRepository = Repos.Post,
    private val commentRepository: CommentRepository = Repos.Comment,
) : StateHolder() {

    private val mutableSelectedTab = MutableStateFlow(UserTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    val user = userRepository.getUser(userName)
        .map { it.toUIState() }
        .stateIn(scope, SharingStarted.Eagerly, UIState.Loading())

    private val initialPostSorting = Repos.Settings.getUserPostSorting()

//    val itemListModel = ItemListStateHolder(initialPostSorting) { postSorting, timeSorting, lastItemId ->
//        Repos.Item.getUserOverviewItems(userName, postSorting, timeSorting, lastItemId)
//    }

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
                    UserTab.Overview -> {}
                    UserTab.Submitted -> if (postsStateHolder.items.value.isEmpty) postsStateHolder.loadItems()
                    UserTab.Comments -> if (commentsStateHolder.items.value.isEmpty) commentsStateHolder.loadItems()
                }
            }
            .launchIn(scope)
    }

    companion object {
        fun getOrCreateInstance(userName: String): UserScreenStateHolder {
            return userScreenModels.find { it.userName == userName }
                ?: UserScreenStateHolder(userName).also { userScreenModels += it }
        }
    }

    fun selectTab(tab: UserTab) {
        mutableSelectedTab.value = tab
    }
}