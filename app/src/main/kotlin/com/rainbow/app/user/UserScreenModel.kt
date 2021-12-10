package com.rainbow.app.user

import com.rainbow.app.comment.CommentListModel
import com.rainbow.app.item.ItemListModel
import com.rainbow.app.model.Model
import com.rainbow.app.post.PostListModel
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.User
import com.rainbow.domain.models.UserPostSorting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private val userScreenModels = mutableSetOf<UserScreenModel>()

class UserScreenModel private constructor(private val userName: String) : Model() {

    private val mutableSelectedTab = MutableStateFlow(UserTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    private val mutableUser = MutableStateFlow<UIState<User>>(UIState.Loading)
    val user get() = mutableUser.asStateFlow()

    val itemListModel = ItemListModel(UserPostSorting.Default) { postSorting, timeSorting, lastPostId ->
        Repos.Item.getUserOverviewItems(userName, postSorting, timeSorting, lastPostId)
    }

    val postListModel = PostListModel(UserPostSorting.Default) { postSorting, timeSorting, lastPostId ->
        Repos.Post.getUserSubmittedPosts(userName, postSorting, timeSorting, lastPostId)
    }

    val commentListModel = CommentListModel(UserPostSorting.Default) { postSorting, timeSorting, lastPostId ->
        Repos.Comment.getUserComments(userName)
    }

    init {
        loadUser()
    }

    companion object {
        fun getOrCreateInstance(userName: String): UserScreenModel {
            return userScreenModels.find { it.userName == userName }
                ?: UserScreenModel(userName).also { userScreenModels += it }
        }
    }

    fun loadUser() = scope.launch {
        mutableUser.value = Repos.User.getUser(userName)
            .onSuccess {
                itemListModel.loadItems()
                postListModel.loadItems()
                commentListModel.loadItems()
            }
            .toUIState()
    }

    fun selectTab(tab: UserTab) {
        mutableSelectedTab.value = tab
    }
}