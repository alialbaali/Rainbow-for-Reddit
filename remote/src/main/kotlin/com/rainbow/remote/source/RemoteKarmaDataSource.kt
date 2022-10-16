package com.rainbow.remote.source

import com.rainbow.remote.dto.RemoteKarma

interface RemoteKarmaDataSource {

    suspend fun getProfileKarma(): List<RemoteKarma>

}