package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.DefaultLimit
import com.rainbow.domain.models.Moderator
import com.rainbow.domain.models.Subreddit
import com.rainbow.domain.models.SubredditsSearchSorting
import com.rainbow.domain.repository.SubredditRepository
import com.rainbow.remote.dto.RemoteModerator
import com.rainbow.remote.dto.RemoteSubreddit
import com.rainbow.remote.source.RemoteModeratorDataSource
import com.rainbow.remote.source.RemoteSubredditDataSource
import com.rainbow.sql.LocalSubreddit
import com.rainbow.sql.LocalSubredditQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

internal fun SubredditRepository(
    remoteSubredditDataSource: RemoteSubredditDataSource,
    remoteModeratorDataSource: RemoteModeratorDataSource,
    localSubredditQueries: LocalSubredditQueries,
    dispatcher: CoroutineDispatcher,
    remoteSubredditMapper: Mapper<RemoteSubreddit, LocalSubreddit>,
    localSubredditMapper: Mapper<LocalSubreddit, Subreddit>,
    remoteModeratorMapper: Mapper<RemoteModerator, Moderator>,
): SubredditRepository =
    SubredditRepositoryImpl(
        remoteSubredditDataSource,
        remoteModeratorDataSource,
        localSubredditQueries,
        dispatcher,
        remoteSubredditMapper,
        localSubredditMapper,
        remoteModeratorMapper,
    )

private class SubredditRepositoryImpl(
    private val remoteSubredditDataSource: RemoteSubredditDataSource,
    private val remoteModeratorDataSource: RemoteModeratorDataSource,
    private val queries: LocalSubredditQueries,
    private val dispatcher: CoroutineDispatcher,
    private val remoteSubredditMapper: Mapper<RemoteSubreddit, LocalSubreddit>,
    private val localSubredditMapper: Mapper<LocalSubreddit, Subreddit>,
    private val remoteModeratorMapper: Mapper<RemoteModerator, Moderator>,
) : SubredditRepository {

    override fun getMySubreddits(lastSubredditId: String?): Flow<Result<List<Subreddit>>> = flow {
        val result = remoteSubredditDataSource.getMySubreddits(DefaultLimit, lastSubredditId)
            .mapCatching { it.quickMap(remoteSubredditMapper) }
            .mapCatching { it.quickMap(localSubredditMapper) }
            .onFailure { throw it }
//            .onSuccess {
//                queries.transaction {
//                    queries.clear()
//                    it.forEach { queries.insert(it) }
//                }
//            }
//            .onFailure {
//                emit(Result.failure<List<Subreddit>>(it))
//            }
        emit(result)

//        queries.selectAll()
//            .asFlow()
//            .mapToList()
//            .map { it.quickMap(localMapper) }
//            .map { Result.success(it) }
//            .also { emitAll(it) }

    }.flowOn(dispatcher)

    override fun getSubreddit(subredditName: String): Flow<Result<Subreddit>> = flow {
        queries.selectByName(subredditName)
            .executeAsOneOrNull()
            .apply {
                if (this == null) {
                    remoteSubredditDataSource.getSubreddit(subredditName)
                        .mapCatching { remoteSubredditMapper.map(it) }
                        .onSuccess {
                            queries.insert(it)
                        }
                        .onFailure {
                            emit(Result.failure<Subreddit>(it))
                        }
                }
            }

        queries.selectByName(subredditName)
            .executeAsOne()
            .let { localSubredditMapper.map(it) }
            .also { emit(Result.success(it)) }

    }.flowOn(dispatcher)

    override fun getSubredditModerators(subredditName: String): Flow<Result<List<Moderator>>> = flow {
        remoteModeratorDataSource.getSubredditModerators(subredditName)
            .mapCatching { it.quickMap(remoteModeratorMapper) }
            .also { emit(it) }
    }

    override suspend fun subscribeSubreddit(subredditName: String): Result<Unit> = withContext(dispatcher) {
        remoteSubredditDataSource.subscribeSubreddit(subredditName)
    }

    override suspend fun unSubscribeSubreddit(subredditName: String): Result<Unit> = withContext(dispatcher) {
        remoteSubredditDataSource.unSubscribeSubreddit(subredditName)
    }

    override suspend fun favoriteSubreddit(subredditName: String): Result<Unit> = withContext(dispatcher) {
        remoteSubredditDataSource.favoriteSubreddit(subredditName)
    }

    override suspend fun unFavoriteSubreddit(subredditName: String): Result<Unit> = withContext(dispatcher) {
        remoteSubredditDataSource.unFavoriteSubreddit(subredditName)
    }

    override suspend fun getSubredditSubmitText(subredditName: String): Result<String> = withContext(dispatcher) {
        remoteSubredditDataSource.getSubredditSubmitText(subredditName)
    }

    override suspend fun getSubredditPostRequirements(subredditName: String): Result<String> = withContext(dispatcher) {
        remoteSubredditDataSource.getSubredditPostRequirements(subredditName)
            .mapCatching { it.guidelinesText ?: "" }
    }

    override fun searchSubreddit(
        subredditName: String,
        sorting: SubredditsSearchSorting,
    ): Flow<Result<List<Subreddit>>> = flow {
        remoteSubredditDataSource.searchSubreddit(subredditName, sorting.name.lowercase(), DefaultLimit, null)
            .mapCatching { it.quickMap(remoteSubredditMapper) }
            .onSuccess {
                queries.transaction {
                    queries.clear()
                    it.forEach { queries.insert(it) }
                }
            }

        queries.selectAll()
            .asFlow()
            .mapToList()
            .map { it.quickMap(localSubredditMapper) }
            .map { Result.success(it) }
            .also { emitAll(it) }

    }.flowOn(dispatcher)
}
