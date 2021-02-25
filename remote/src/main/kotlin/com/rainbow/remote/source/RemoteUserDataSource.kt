package com.rainbow.remote.source

import com.rainbow.remote.dto.RemoteUser

interface RemoteUserDataSource {

    suspend fun getUserAbout(userName: String): Result<RemoteUser>

    suspend fun checkUserName(userName: String): Result<Boolean>

    suspend fun blockUser(userName: String): Result<Unit>

    suspend fun reportUser(userName: String, reason: String): Result<Unit>

}