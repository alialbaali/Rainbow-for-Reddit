package com.rainbow.domain.repository

import com.rainbow.domain.models.User

interface UserRepository {

    suspend fun getCurrentUser(): Result<User>

    suspend fun getUser(userName: String): Result<User>

    suspend fun checkUserName(userName: String): Result<Boolean>

    suspend fun blockUser(userName: String): Result<Unit>

}