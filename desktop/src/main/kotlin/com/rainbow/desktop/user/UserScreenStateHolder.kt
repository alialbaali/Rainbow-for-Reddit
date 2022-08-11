package com.rainbow.desktop.user

import com.rainbow.data.Repos
import com.rainbow.desktop.comment.CommentListStateHolder
import com.rainbow.desktop.item.ItemListStateHolder
import com.rainbow.desktop.model.StateHolder
import com.rainbow.desktop.post.PostListStateHolder
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.toUIState
import com.rainbow.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private val userScreenModels = mutableSetOf<UserScreenStateHolder>()

class UserScreenStateHolder private constructor(private val userName: String) : StateHolder() {

    private val mutableSelectedTab = MutableStateFlow(UserTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    private val mutableUser = MutableStateFlow<UIState<User>>(UIState.Empty)
    val user get() = mutableUser.asStateFlow()

    private val initialPostSorting = Repos.Settings.getUserPostSorting()

    val itemListModel = ItemListStateHolder(initialPostSorting) { postSorting, timeSorting, lastItemId ->
        Repos.Item.getUserOverviewItems(userName, postSorting, timeSorting, lastItemId)
    }

    val postListModel = PostListStateHolder(initialPostSorting) { postSorting, timeSorting, lastPostId ->
        Repos.Post.getUserSubmittedPosts(userName, postSorting, timeSorting, lastPostId)
    }

    val commentListModel = CommentListStateHolder(initialPostSorting) { postSorting, timeSorting, lastCommentId ->
        Repos.Comment.getUserComments(userName, postSorting, timeSorting, lastCommentId)
    }

    init {
        loadUser()
        selectedTab
            .onEach {
                when (it) {
                    UserTab.Overview -> if (itemListModel.items.value.isLoading) itemListModel.loadItems()
                    UserTab.Submitted -> if (postListModel.items.value.isLoading) postListModel.loadItems()
                    UserTab.Comments -> if (commentListModel.items.value.isLoading) commentListModel.loadItems()
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

    fun loadUser() = scope.launch {
        mutableUser.value = Repos.User.getUser(userName).toUIState()
    }

    fun selectTab(tab: UserTab) {
        mutableSelectedTab.value = tab
    }
}