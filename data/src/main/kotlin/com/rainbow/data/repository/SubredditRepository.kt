package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.DefaultLimit
import com.rainbow.domain.models.Subreddit
import com.rainbow.domain.models.SubredditsSearchSorting
import com.rainbow.domain.repository.SubredditRepository
import com.rainbow.remote.dto.RemoteSubreddit
import com.rainbow.remote.source.RemoteSubredditDataSource
import com.rainbow.sql.LocalSubreddit
import com.rainbow.sql.LocalSubredditQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

internal fun SubredditRepository(
    remoteDataSource: RemoteSubredditDataSource,
    localSubredditQueries: LocalSubredditQueries,
    dispatcher: CoroutineDispatcher,
    remoteMapper: Mapper<RemoteSubreddit, LocalSubreddit>,
    localMapper: Mapper<LocalSubreddit, Subreddit>,
): SubredditRepository =
    SubredditRepositoryImpl(remoteDataSource, localSubredditQueries, dispatcher, remoteMapper, localMapper)

private class SubredditRepositoryImpl(
    private val remoteDataSource: RemoteSubredditDataSource,
    private val queries: LocalSubredditQueries,
    private val dispatcher: CoroutineDispatcher,
    private val remoteMapper: Mapper<RemoteSubreddit, LocalSubreddit>,
    private val localMapper: Mapper<LocalSubreddit, Subreddit>,
) : SubredditRepository {

    override fun getMySubreddits(lastSubredditId: String?): Flow<Result<List<Subreddit>>> = flow {
        remoteDataSource.getMySubreddits(DefaultLimit, lastSubredditId)
            .mapCatching { it.quickMap(remoteMapper) }
            .onSuccess {
                queries.transaction {
                    queries.clear()
                    it.forEach { queries.insert(it) }
                }
            }
            .onFailure {
                emit(Result.failure<List<Subreddit>>(it))
            }

        queries.selectAll()
            .asFlow()
            .mapToList()
            .map { it.quickMap(localMapper) }
            .map { Result.success(it) }
            .also { emitAll(it) }

    }.flowOn(dispatcher)

    override fun getSubreddit(subredditName: String): Flow<Result<Subreddit>> = flow {
        queries.selectByName(subredditName)
            .executeAsOneOrNull()
            .apply {
                if (this == null) {
                    remoteDataSource.getSubreddit(subredditName)
                        .mapCatching { remoteMapper.map(it) }
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
            .let { localMapper.map(it) }
            .also { emit(Result.success(it)) }

    }.flowOn(dispatcher)

    override suspend fun subscribeSubreddit(subredditName: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.subscribeSubreddit(subredditName)
    }

    override suspend fun unSubscribeSubreddit(subredditName: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.unSubscribeSubreddit(subredditName)
    }

    override suspend fun favoriteSubreddit(subredditName: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.favoriteSubreddit(subredditName)
    }

    override suspend fun unFavoriteSubreddit(subredditName: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.unFavoriteSubreddit(subredditName)
    }

    override suspend fun getSubredditSubmitText(subredditName: String): Result<String> = withContext(dispatcher) {
        remoteDataSource.getSubredditSubmitText(subredditName)
    }

    override suspend fun getSubredditPostRequirements(subredditName: String): Result<String> = withContext(dispatcher) {
        remoteDataSource.getSubredditPostRequirements(subredditName)
            .mapCatching { it.guidelinesText ?: "" }
    }

    override fun searchSubreddit(
        subredditName: String,
        sorting: SubredditsSearchSorting,
    ): Flow<Result<List<Subreddit>>> = flow {
        remoteDataSource.searchSubreddit(subredditName, sorting.name.lowercase(), DefaultLimit, null)
            .mapCatching { it.quickMap(remoteMapper) }
            .onSuccess {
                queries.transaction {
                    queries.clear()
                    it.forEach { queries.insert(it) }
                }
            }

        queries.selectAll()
            .asFlow()
            .mapToList()
            .map { it.quickMap(localMapper) }
            .map { Result.success(it) }
            .also { emitAll(it) }

    }.flowOn(dispatcher)
}
