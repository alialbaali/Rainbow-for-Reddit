package com.rainbow.domain.repository

import com.rainbow.domain.models.User
import kotlinx.coroutines.flow.Flow
import java.util.*

interface UserRepository {

    val isUserLoggedIn: Flow<Boolean>

    suspend fun loginUser(uuid: UUID): Result<Unit>

    suspend fun logoutUser()

    suspend fun getCurrentUser(): Result<User>

    suspend fun getUser(userName: String): Result<User>

    suspend fun checkUserName(userName: String): Result<Boolean>

    suspend fun blockUser(userName: String): Result<Unit>

}