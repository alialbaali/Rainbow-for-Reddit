package com.rainbow.app.user

import com.rainbow.app.utils.Model
import com.rainbow.app.post.PostModel
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.User
import com.rainbow.domain.models.UserPostSorting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserModel(val userName: String) : Model() {

    private val mutableUser = MutableStateFlow<UIState<User>>(UIState.Loading)
    val user get() = mutableUser.asStateFlow()

    val postModel = PostModel(UserPostSorting.Default) { postSorting, timeSorting, lastPostId ->
        Repos.Post.getUserSubmittedPosts(userName, postSorting, timeSorting, lastPostId)
    }

    init {
        loadUser()
    }

    fun loadUser() = scope.launch {
        mutableUser.value = Repos.User.getUser(userName)
            .onSuccess { postModel.loadPosts() }
            .toUIState()
    }
}