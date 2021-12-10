package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.DefaultLimit
import com.rainbow.data.utils.SettingsKeys
import com.rainbow.domain.models.User
import com.rainbow.domain.repository.UserRepository
import com.rainbow.remote.dto.RemoteUser
import com.rainbow.remote.source.RemoteUserDataSource
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*

private const val IsUserLoggedInKey = "is_user_logged_in"

@OptIn(ExperimentalSettingsApi::class)
internal class UserRepositoryImpl(
    private val remoteDataSource: RemoteUserDataSource,
    private val settings: FlowSettings,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: Mapper<RemoteUser, User>,
) : UserRepository {

    override val isUserLoggedIn: Flow<Boolean> = settings.getBooleanFlow(IsUserLoggedInKey)

    override suspend fun loginUser(uuid: UUID): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.loginUser(uuid)
            .onSuccess { settings.putBoolean(IsUserLoggedInKey, true) }
    }

    override suspend fun logoutUser() = withContext(dispatcher) {
        settings.clear()
    }

    override suspend fun getCurrentUser(): Result<User> = withContext(dispatcher) {
        remoteDataSource.getCurrentUser()
            .map { mapper.map(it) }
            .onSuccess { settings.putString(SettingsKeys.UserName, it.name) }
    }

    override suspend fun getUser(userName: String): Result<User> = withContext(dispatcher) {
        remoteDataSource.getUserAbout(userName)
            .map { mapper.map(it) }
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
    ): Result<List<User>> = withContext(dispatcher) {
        remoteDataSource.searchUsers(searchTerm, DefaultLimit, lastUserId)
            .mapCatching { it.quickMap(mapper) }
    }
}