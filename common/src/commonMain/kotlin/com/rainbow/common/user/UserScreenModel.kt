package com.rainbow.common.user

import com.rainbow.common.comment.CommentListModel
import com.rainbow.common.item.ItemListModel
import com.rainbow.common.model.Model
import com.rainbow.common.post.PostListModel
import com.rainbow.common.utils.UIState
import com.rainbow.common.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private val userScreenModels = mutableSetOf<UserScreenModel>()

class UserScreenModel private constructor(private val userName: String) : Model() {

    private val mutableSelectedTab = MutableStateFlow(UserTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    private val mutableUser = MutableStateFlow<UIState<User>>(UIState.Loading)
    val user get() = mutableUser.asStateFlow()

    private val initialPostSorting = Repos.Settings.getUserPostSorting()

    val itemListModel = ItemListModel(initialPostSorting) { postSorting, timeSorting, lastItemId ->
        Repos.Item.getUserOverviewItems(userName, postSorting, timeSorting, lastItemId)
    }

    val postListModel = PostListModel(initialPostSorting) { postSorting, timeSorting, lastPostId ->
        Repos.Post.getUserSubmittedPosts(userName, postSorting, timeSorting, lastPostId)
    }

    val commentListModel = CommentListModel(initialPostSorting) { postSorting, timeSorting, lastCommentId ->
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
        fun getOrCreateInstance(userName: String): UserScreenModel {
            return userScreenModels.find { it.userName == userName }
                ?: UserScreenModel(userName).also { userScreenModels += it }
        }
    }

    fun loadUser() = scope.launch {
        mutableUser.value = Repos.User.getUser(userName).toUIState()
    }

    fun selectTab(tab: UserTab) {
        mutableSelectedTab.value = tab
    }
}