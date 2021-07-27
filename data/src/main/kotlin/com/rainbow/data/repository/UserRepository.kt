package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.domain.models.User
import com.rainbow.domain.repository.UserRepository
import com.rainbow.remote.dto.RemoteUser
import com.rainbow.remote.source.RemoteUserDataSource
import com.rainbow.sql.LocalUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal fun UserRepository(
    remoteDataSource: RemoteUserDataSource,
    dispatcher: CoroutineDispatcher,
    remoteMapper: Mapper<RemoteUser, LocalUser>,
    localMapper: Mapper<LocalUser, User>,
): UserRepository = UserRepositoryImpl(remoteDataSource, dispatcher, remoteMapper, localMapper)

private class UserRepositoryImpl(
    private val remoteDataSource: RemoteUserDataSource,
    private val dispatcher: CoroutineDispatcher,
    private val remoteMapper: Mapper<RemoteUser, LocalUser>,
    private val localMapper: Mapper<LocalUser, User>
) : UserRepository {

    // Temporary
    lateinit var currentUser: User

    override suspend fun getCurrentUser(): Result<User> {
        return remoteDataSource.getUserAbout("LoneWalker20")
            .map {
                remoteMapper.map(it)
                    .let { localMapper.map(it) }
                    .also { currentUser = it }
            }
    }

    override suspend fun getUser(userName: String): Result<User> = withContext(dispatcher) {
        remoteDataSource.getUserAbout(userName)
            .map { remoteMapper.map(it) }
            .map { localMapper.map(it) }
    }

    override suspend fun checkUserName(userName: String): Result<Boolean> = withContext(dispatcher) {
        remoteDataSource.checkUserName(userName)
    }

    override suspend fun blockUser(userName: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.blockUser(userName)
    }
}



