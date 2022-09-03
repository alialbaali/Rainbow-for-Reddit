package com.rainbow.domain.repository

import com.rainbow.domain.models.User
import kotlinx.coroutines.flow.Flow
import java.util.*

interface UserRepository {

    val isUserLoggedIn: Flow<Boolean>

    val currentUser: Flow<User>

    suspend fun loginUser(uuid: UUID): Result<Unit>

    suspend fun logoutUser()

    suspend fun getCurrentUser(): Result<Unit>

    fun getUser(userName: String): Flow<Result<User>>

    suspend fun checkUserName(userName: String): Result<Boolean>

    suspend fun blockUser(userName: String): Result<Unit>

    suspend fun searchUsers(searchTerm: String, lastUserId: String?): Result<List<User>>

}