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

    override fun setCurrentUser(user: User) {
        mutableCurrentUser.value = user
    }

    override fun insertUser(user: User) {
        mutableUsers.value = mutableUsers.value + user
    }

}