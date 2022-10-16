package com.rainbow.domain.repository

import com.rainbow.domain.models.Karma
import com.rainbow.domain.models.Trophy
import com.rainbow.domain.models.User
import kotlinx.coroutines.flow.Flow
import java.util.*

interface UserRepository {

    val isUserLoggedIn: Flow<Boolean>

    val currentUser: Flow<User>

    val searchUsers: Flow<List<User>>

    fun createAuthenticationUrl(uuid: UUID): String

    suspend fun loginUser(uuid: UUID): Result<Unit>

    suspend fun logoutUser()

    suspend fun getCurrentUser(): Result<Unit>

    fun getUser(userName: String): Flow<Result<User>>

    suspend fun checkUserName(userName: String): Result<Boolean>

    suspend fun blockUser(userName: String): Result<Unit>

    suspend fun searchUsers(searchTerm: String, lastUserId: String?): Result<Unit>

    suspend fun getProfileKarma(): Result<List<Karma>>

    suspend fun getProfileTrophies(): Result<List<Trophy>>

    suspend fun getUserTrophies(userName: String): Result<List<Trophy>>

}