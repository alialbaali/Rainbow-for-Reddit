package com.rainbow.local

import com.rainbow.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull

class LocalUserDataSourceImpl : LocalUserDataSource {

    private val mutableCurrentUser = MutableStateFlow<User?>(null)
    override val currentUser: Flow<User> get() = mutableCurrentUser.asStateFlow().filterNotNull()

    private val mutableUsers = MutableStateFlow(emptyList<User>())
    override val users: Flow<List<User>> get() = mutableUsers.asStateFlow()

    private val mutableSearchUsers = MutableStateFlow(emptyList<User>())
    override val searchUsers get() = mutableSearchUsers.asStateFlow()

    override fun setCurrentUser(user: User) {
        mutableCurrentUser.value = user
    }

    override fun insertUser(user: User) {
        mutableUsers.value = mutableUsers.value + user
    }

    override fun insertSearchUser(user: User) {
        mutableSearchUsers.value += user
    }

    override fun clearSearchUsers() {
        mutableSearchUsers.value = emptyList()
    }

}