package com.rainbow.remote.source

import com.rainbow.remote.dto.RemoteUser
import java.util.*

interface RemoteUserDataSource {

    suspend fun loginUser(uuid: UUID): Result<Unit>

    suspend fun getCurrentUser(): Result<RemoteUser>

    suspend fun getUserAbout(userName: String): Result<RemoteUser>

    suspend fun checkUserName(userName: String): Result<Boolean>

    suspend fun blockUser(userName: String): Result<Unit>

    suspend fun reportUser(userName: String, reason: String): Result<Unit>

    suspend fun searchUsers(searchTerm: String, limit: Int, after: String?): Result<List<RemoteUser>>

}