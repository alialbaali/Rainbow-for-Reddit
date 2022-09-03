package com.rainbow.local

import com.rainbow.domain.models.User
import kotlinx.coroutines.flow.Flow

interface LocalUserDataSource {

    val currentUser: Flow<User>

    val users: Flow<List<User>>

    fun setCurrentUser(user: User)

    fun insertUser(user: User)

}