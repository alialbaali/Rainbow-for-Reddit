package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.domain.models.Subreddit
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
        remoteDataSource.getMySubreddits()
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getSubreddit(subredditName: String): Result<Subreddit> = withContext(dispatcher) {
        remoteDataSource.getSubredditAbout(subredditName)
            .mapCatching { mapper.map(it) }
    }

}
