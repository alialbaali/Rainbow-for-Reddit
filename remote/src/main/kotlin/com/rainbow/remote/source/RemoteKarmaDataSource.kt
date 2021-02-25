package com.rainbow.remote.source

import com.rainbow.remote.dto.RemoteKarma

interface RemoteKarmaDataSource {

    suspend fun getMyKarma(): Result<List<RemoteKarma>>

}