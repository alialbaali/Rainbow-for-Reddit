package com.rainbow.domain.repository

import com.rainbow.domain.models.User

interface UserRepository {

    suspend fun getUser(userName: String): Result<User>

}