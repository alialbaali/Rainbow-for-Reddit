package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.DefaultLimit
import com.rainbow.data.utils.SettingsKeys
import com.rainbow.domain.models.User
import com.rainbow.domain.repository.UserRepository
import com.rainbow.local.LocalUserDataSource
import com.rainbow.remote.dto.RemoteUser
import com.rainbow.remote.source.RemoteUserDataSource
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.*

private const val IsUserLoggedInKey = "is_user_logged_in"

@OptIn(ExperimentalSettingsApi::class)
internal class UserRepositoryImpl(
    private val remoteDataSource: RemoteUserDataSource,
    private val localDataSource: LocalUserDataSource,
    private val settings: FlowSettings,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: Mapper<RemoteUser, User>,
) : UserRepository {

    override val isUserLoggedIn: Flow<Boolean> = settings.getBooleanFlow(IsUserLoggedInKey)
    override val currentUser: Flow<User> = localDataSource.currentUser
    override val searchUsers: Flow<List<User>> = localDataSource.searchUsers

    override suspend fun loginUser(uuid: UUID): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.loginUser(uuid)
            .onSuccess { settings.putBoolean(IsUserLoggedInKey, true) }
    }

    override suspend fun logoutUser() = withContext(dispatcher) {
        settings.clear()
    }

    override suspend fun getCurrentUser(): Result<Unit> = runCatching {
        withContext(dispatcher) {
            remoteDataSource.getCurrentUser()
                .let(mapper::map)
                .also { settings.putString(SettingsKeys.UserName, it.name) }
                .also(localDataSource::setCurrentUser)
        }
    }

    override fun getUser(userName: String): Flow<Result<User>> {
        return localDataSource.users
            .map {
                it.find { user -> user.name == userName }
                    ?: remoteDataSource.getUserAbout(userName)
                        .let(mapper::map)
                        .also(localDataSource::insertUser)
            }
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
            .flowOn(dispatcher)
    }

    override suspend fun checkUserName(userName: String): Result<Boolean> = withContext(dispatcher) {
        remoteDataSource.checkUserName(userName)
    }

    override suspend fun blockUser(userName: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.blockUser(userName)
    }

    override suspend fun searchUsers(
        searchTerm: String,
        lastUserId: String?,
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastUserId == null) localDataSource.clearSearchUsers()

            remoteDataSource.searchUsers(searchTerm, DefaultLimit, lastUserId)
                .quickMap(mapper)
                .forEach(localDataSource::insertSearchUser)
        }
    }
}