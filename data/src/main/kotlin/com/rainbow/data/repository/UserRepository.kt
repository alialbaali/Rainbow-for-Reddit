package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.domain.models.User
import com.rainbow.domain.repository.UserRepository
import com.rainbow.remote.dto.RemoteUser
import com.rainbow.remote.source.RemoteUserDataSource
import com.rainbow.sql.LocalUser
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*

private const val IsUserLoggedInKey = "is_user_logged_in"

@OptIn(ExperimentalSettingsApi::class)
internal fun UserRepository(
    remoteDataSource: RemoteUserDataSource,
    dispatcher: CoroutineDispatcher,
    remoteMapper: Mapper<RemoteUser, LocalUser>,
    localMapper: Mapper<LocalUser, User>,
    settings: FlowSettings,
): UserRepository = UserRepositoryImpl(remoteDataSource, dispatcher, remoteMapper, localMapper, settings)

@OptIn(ExperimentalSettingsApi::class)
private class UserRepositoryImpl(
    private val remoteDataSource: RemoteUserDataSource,
    private val dispatcher: CoroutineDispatcher,
    private val remoteMapper: Mapper<RemoteUser, LocalUser>,
    private val localMapper: Mapper<LocalUser, User>,
    private val settings: FlowSettings,
) : UserRepository {

    override val isUserLoggedIn: Flow<Boolean> = settings.getBooleanFlow(IsUserLoggedInKey)

    override suspend fun loginUser(uuid: UUID): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.loginUser(uuid)
            .onSuccess { settings.putBoolean(IsUserLoggedInKey, true) }
    }

    override suspend fun logoutUser() = withContext(dispatcher) {
        settings.clear()
    }

    override suspend fun getCurrentUser(): Result<User> {
        return remoteDataSource.getCurrentUser()
            .map { remoteMapper.map(it) }
            .map { localMapper.map(it) }
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



