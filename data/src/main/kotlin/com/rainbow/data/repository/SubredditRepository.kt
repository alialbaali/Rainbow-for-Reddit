package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.DefaultLimit
import com.rainbow.data.utils.Null
import com.rainbow.domain.models.Subreddit
import com.rainbow.domain.models.SubredditsSearchSorting
import com.rainbow.domain.repository.SubredditRepository
import com.rainbow.remote.dto.RemoteSubreddit
import com.rainbow.remote.source.RemoteSubredditDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal fun SubredditRepository(
    remoteDataSource: RemoteSubredditDataSource,
    dispatcher: CoroutineDispatcher,
    mapper: Mapper<RemoteSubreddit, Subreddit>,
): SubredditRepository = SubredditRepositoryImpl(remoteDataSource, dispatcher, mapper)

private class SubredditRepositoryImpl(
    private val remoteDataSource: RemoteSubredditDataSource,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: Mapper<RemoteSubreddit, Subreddit>,
) : SubredditRepository {

    override suspend fun getMySubreddits(): Result<List<Subreddit>> = withContext(dispatcher) {
        remoteDataSource.getMySubreddits(DefaultLimit, Null)
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getSubreddit(subredditName: String): Result<Subreddit> = withContext(dispatcher) {
        remoteDataSource.getSubreddit(subredditName)
            .mapCatching { mapper.map(it) }
    }

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

    override suspend fun searchSubreddit(
        subredditName: String,
        sorting: SubredditsSearchSorting,
    ): Result<List<Subreddit>> = withContext(dispatcher) {
        remoteDataSource.searchSubreddit(subredditName, sorting.name.toLowerCase(), DefaultLimit, Null)
            .mapCatching { it.quickMap(mapper) }
    }
}
