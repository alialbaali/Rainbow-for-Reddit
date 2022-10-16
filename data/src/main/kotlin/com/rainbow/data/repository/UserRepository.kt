package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.DefaultLimit
import com.rainbow.data.utils.SettingsKeys
import com.rainbow.domain.models.Karma
import com.rainbow.domain.models.Trophy
import com.rainbow.domain.models.User
import com.rainbow.domain.repository.SubredditRepository
import com.rainbow.domain.repository.UserRepository
import com.rainbow.local.LocalUserDataSource
import com.rainbow.remote.dto.RemoteKarma
import com.rainbow.remote.dto.RemoteTrophy
import com.rainbow.remote.dto.RemoteUser
import com.rainbow.remote.source.RemoteKarmaDataSource
import com.rainbow.remote.source.RemoteTrophyDataSource
import com.rainbow.remote.source.RemoteUserDataSource
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.util.*

private const val IsUserLoggedInKey = "is_user_logged_in"

@OptIn(ExperimentalSettingsApi::class)
internal class UserRepositoryImpl(
    private val subredditRepository: SubredditRepository,
    private val remoteUserDataSource: RemoteUserDataSource,
    private val localUserDataSource: LocalUserDataSource,
    private val remoteKarmaDataSource: RemoteKarmaDataSource,
    private val remoteTrophyDataSource: RemoteTrophyDataSource,
    private val settings: FlowSettings,
    private val dispatcher: CoroutineDispatcher,
    private val userMapper: Mapper<RemoteUser, User>,
    private val karmaMapper: Mapper<RemoteKarma, Karma>,
    private val trophyMapper: Mapper<RemoteTrophy, Trophy>,
) : UserRepository {

    override val isUserLoggedIn: Flow<Boolean> = settings.getBooleanFlow(IsUserLoggedInKey)
    override val currentUser: Flow<User> = localUserDataSource.currentUser
    override val searchUsers: Flow<List<User>> = localUserDataSource.searchUsers

    override fun createAuthenticationUrl(uuid: UUID): String {
        return URLBuilder(
            protocol = URLProtocol.HTTPS,
            host = "www.reddit.com/api/v1/authorize",
            parameters = ParametersBuilder().apply {
                append("client_id", "cpKMrRbh8b06TQ")
                append("response_type", "code")
                append("state", uuid.toString())
                append("redirect_uri", "https://rainbowforreddit.herokuapp.com/")
                append("duration", "permanent")
                append(
                    "scope",
                    "submit, vote, mysubreddits, privatemessages, subscribe, history, wikiread, flair, identity, edit, read, report, save, submit"
                )
            }.build()
        ).buildString()
    }

    override suspend fun loginUser(uuid: UUID): Result<Unit> = withContext(dispatcher) {
        remoteUserDataSource.loginUser(uuid)
            .onSuccess { settings.putBoolean(IsUserLoggedInKey, true) }
    }

    override suspend fun logoutUser() = withContext(dispatcher) {
        settings.clear()
    }

    override suspend fun getCurrentUser(): Result<Unit> = runCatching {
        withContext(dispatcher) {
            remoteUserDataSource.getCurrentUser()
                .let(userMapper::map)
                .also { settings.putString(SettingsKeys.UserName, it.name) }
                .also(localUserDataSource::setCurrentUser)
        }
    }

    override fun getUser(userName: String): Flow<Result<User>> {
        return localUserDataSource.users
            .map {
                it.find { user -> user.name == userName }
                    ?: remoteUserDataSource.getUserAbout(userName)
                        .let(userMapper::map)
                        .also(localUserDataSource::insertUser)
            }
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
            .flowOn(dispatcher)
    }

    override suspend fun checkUserName(userName: String): Result<Boolean> = withContext(dispatcher) {
        remoteUserDataSource.checkUserName(userName)
    }

    override suspend fun blockUser(userName: String): Result<Unit> = withContext(dispatcher) {
        remoteUserDataSource.blockUser(userName)
    }

    override suspend fun searchUsers(
        searchTerm: String,
        lastUserId: String?,
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastUserId == null) localUserDataSource.clearSearchUsers()

            remoteUserDataSource.searchUsers(searchTerm, DefaultLimit, lastUserId)
                .quickMap(userMapper)
                .forEach(localUserDataSource::insertSearchUser)
        }
    }

    override suspend fun getProfileKarma(): Result<List<Karma>> = runCatching {
        withContext(dispatcher) {
            remoteKarmaDataSource.getProfileKarma().quickMap(karmaMapper)
        }
    }

    override suspend fun getProfileTrophies(): Result<List<Trophy>> = runCatching {
        withContext(dispatcher) {
            remoteTrophyDataSource.getUserTrophies(settings.getString(SettingsKeys.UserName))
                .quickMap(trophyMapper)
        }
    }

    override suspend fun getUserTrophies(userName: String): Result<List<Trophy>> = runCatching {
        withContext(dispatcher) {
            remoteTrophyDataSource.getUserTrophies(userName)
                .quickMap(trophyMapper)
        }
    }

}