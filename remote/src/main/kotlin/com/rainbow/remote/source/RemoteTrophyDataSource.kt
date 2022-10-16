package com.rainbow.remote.source

import com.rainbow.remote.dto.RemoteTrophy

interface RemoteTrophyDataSource {

    suspend fun getUserTrophies(userName: String): List<RemoteTrophy>

}