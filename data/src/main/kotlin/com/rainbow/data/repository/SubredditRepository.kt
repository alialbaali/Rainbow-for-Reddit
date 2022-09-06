package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.Mappers
import com.rainbow.data.quickMap
import com.rainbow.data.utils.DefaultLimit
import com.rainbow.data.utils.SettingsKeys
import com.rainbow.domain.models.*
import com.rainbow.domain.repository.SubredditRepository
import com.rainbow.local.LocalSubredditDataSource
import com.rainbow.remote.dto.RemoteModerator
import com.rainbow.remote.dto.RemoteRule
import com.rainbow.remote.dto.RemoteSubreddit
import com.rainbow.remote.dto.RemoteWikiPage
import com.rainbow.remote.source.*
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@OptIn(ExperimentalSettingsApi::class)
internal class SubredditRepositoryImpl(
    private val remoteSubredditDataSource: RemoteSubredditDataSource,
    private val remoteModeratorDataSource: RemoteModeratorDataSource,
    private val remoteWikiDataSource: RemoteWikiDataSource,
    private val remoteSubredditFlairDataSource: RemoteSubredditFlairDataSource,
    private val remoteRuleDataSource: RemoteRuleDataSource,
    private val localSubredditDataSource: LocalSubredditDataSource,
    private val settings: FlowSettings,
    private val dispatcher: CoroutineDispatcher,
    private val subredditMapper: Mapper<RemoteSubreddit, Subreddit>,
    private val moderatorMapper: Mapper<RemoteModerator, Moderator>,
    private val wikiPageMapper: Mapper<RemoteWikiPage, WikiPage>,
    private val ruleMapper: Mapper<RemoteRule, Rule>,
) : SubredditRepository {

    override val profileSubreddits: Flow<List<Subreddit>> = localSubredditDataSource.profileSubreddits
    override val searchSubreddits: Flow<List<Subreddit>> = localSubredditDataSource.searchSubreddits

    override suspend fun getProfileSubreddits(lastSubredditId: String?): Result<Unit> = runCatching {
        withContext(dispatcher) {
            remoteSubredditDataSource.getProfileSubreddits(DefaultLimit, lastSubredditId)
                .quickMap(subredditMapper)
                .forEach(localSubredditDataSource::insertProfileSubreddit)
        }
    }

    override fun getSubreddit(subredditName: String): Flow<Result<Subreddit>> {
        return localSubredditDataSource.getSubreddit(subredditName)
            .map { subreddit ->
                subreddit ?: remoteSubredditDataSource.getSubreddit(subredditName)
                    .let(subredditMapper::map)
                    .also(localSubredditDataSource::insertSubreddit)
            }
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
            .flowOn(dispatcher)
    }

    override suspend fun getSubredditModerators(subredditName: String): Result<List<Moderator>> =
        withContext(dispatcher) {
            remoteModeratorDataSource.getSubredditModerators(subredditName)
                .mapCatching { it.quickMap(moderatorMapper) }
        }

    override suspend fun getSubredditRules(subredditName: String): Result<List<Rule>> = withContext(dispatcher) {
        remoteRuleDataSource.getSubredditRules(subredditName)
            .mapCatching { it.quickMap(ruleMapper) }
    }

    override suspend fun subscribeSubreddit(subredditName: String): Result<Unit> = withContext(dispatcher) {
        remoteSubredditDataSource.subscribeSubreddit(subredditName)
            .onSuccess { localSubredditDataSource.subscribeSubreddit(subredditName) }
    }

    override suspend fun unSubscribeSubreddit(subredditName: String): Result<Unit> = withContext(dispatcher) {
        remoteSubredditDataSource.unSubscribeSubreddit(subredditName)
            .onSuccess { localSubredditDataSource.unsubscribeSubreddit(subredditName) }
    }

    override suspend fun favoriteSubreddit(subredditName: String): Result<Unit> = withContext(dispatcher) {
        remoteSubredditDataSource.favoriteSubreddit(subredditName)
            .onSuccess { localSubredditDataSource.favoriteSubreddit(subredditName) }
    }

    override suspend fun unFavoriteSubreddit(subredditName: String): Result<Unit> = withContext(dispatcher) {
        remoteSubredditDataSource.unFavoriteSubreddit(subredditName)
            .onSuccess { localSubredditDataSource.unFavoriteSubreddit(subredditName) }
    }

    override suspend fun getSubredditSubmitText(subredditName: String): Result<String> = withContext(dispatcher) {
        remoteSubredditDataSource.getSubredditSubmitText(subredditName)
    }

    override suspend fun getSubredditPostRequirements(subredditName: String): Result<String> = withContext(dispatcher) {
        remoteSubredditDataSource.getSubredditPostRequirements(subredditName)
            .mapCatching { it.guidelinesText ?: "" }
    }

    override suspend fun getWikiIndex(subredditName: String): Result<WikiPage> = withContext(dispatcher) {
        remoteWikiDataSource.getWikiIndex(subredditName)
            .map { wikiPageMapper.map(it) }
    }

    override suspend fun getWikiPage(subredditName: String, pageName: String): Result<WikiPage> =
        withContext(dispatcher) {
            remoteWikiDataSource.getWikiPage(subredditName, pageName)
                .map { wikiPageMapper.map(it) }
        }

    override suspend fun searchSubreddits(subredditName: String, lastSubredditId: String?): Result<Unit> =
        runCatching {
            withContext(dispatcher) {
                if (lastSubredditId == null) localSubredditDataSource.clearSearchSubreddits()

                remoteSubredditDataSource.searchSubreddit(subredditName, DefaultLimit, lastSubredditId)
                    .quickMap(subredditMapper)
                    .forEach(localSubredditDataSource::insertSearchSubreddit)
            }
        }

    override suspend fun getSubredditFlairs(subredditName: String): Result<List<Flair>> = withContext(dispatcher) {
        remoteSubredditFlairDataSource.getSubredditFlairs(subredditName)
            .map { it.quickMap(Mappers.FlairMapper) }
    }

    override suspend fun getCurrentSubredditFlair(subredditName: String): Result<Flair> = withContext(dispatcher) {
        remoteSubredditFlairDataSource.getCurrentSubredditFlair(subredditName)
            .map { Mappers.FlairMapper.map(it) }
    }

    override suspend fun selectFlair(subredditName: String, flairId: String): Result<Unit> = withContext(dispatcher) {
        val userName = settings.getString(SettingsKeys.UserName)
        remoteSubredditFlairDataSource.selectSubredditFlair(subredditName, userName, flairId)
    }

    override suspend fun unselectFlair(subredditName: String): Result<Unit> = withContext(dispatcher) {
        val userName = settings.getString(SettingsKeys.UserName)
        remoteSubredditFlairDataSource.unSelectSubredditFlair(subredditName, userName)
    }
}
