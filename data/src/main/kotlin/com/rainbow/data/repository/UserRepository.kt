package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.domain.models.User
import com.rainbow.domain.repository.UserRepository
import com.rainbow.remote.dto.RemoteUser
import com.rainbow.remote.source.RemoteUserDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal fun UserRepository(
    remoteDataSource: RemoteUserDataSource,
    dispatcher: CoroutineDispatcher,
    mapper: Mapper<RemoteUser, User>,
): UserRepository = UserRepositoryImpl(remoteDataSource, dispatcher, mapper)

private class UserRepositoryImpl(
    private val remoteUserDataSource: RemoteUserDataSource,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: Mapper<RemoteUser, User>,
) : UserRepository {

    override suspend fun getUser(userName: String): Result<User> = withContext(dispatcher) {
        remoteUserDataSource.getUserAbout(userName)
            .map { mapper.map(it) }
    }

    override suspend fun checkUserName(userName: String): Result<Boolean> = withContext(dispatcher) {
        remoteUserDataSource.checkUserName(userName)
    }

    override suspend fun blockUser(userName: String): Result<Unit> = withContext(dispatcher){
        remoteUserDataSource.blockUser(userName)
    }
}



