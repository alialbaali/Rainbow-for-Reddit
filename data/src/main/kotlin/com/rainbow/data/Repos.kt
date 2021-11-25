package com.rainbow.data

import com.rainbow.data.LocalMappers.LocalPostMapper
import com.rainbow.data.RemoteMappers.RemoteCommentMapper
import com.rainbow.data.RemoteMappers.RemotePostMapper
import com.rainbow.data.RemoteMappers.WikiPageMapper
import com.rainbow.data.repository.*
import com.rainbow.domain.repository.*
import com.rainbow.local.RainbowDatabase
import com.rainbow.remote.impl.*
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.JvmPreferencesSettings
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

private val DefaultDispatcher = Dispatchers.IO

object Repos {

    @OptIn(
        ExperimentalSettingsImplementation::class,
        ExperimentalSettingsApi::class,
        ExperimentalCoroutinesApi::class,
    )
    private val settings = JvmPreferencesSettings.Factory()
        .create(null)
        .let { it as ObservableSettings }
        .toFlowSettings(DefaultDispatcher)

    @OptIn(ExperimentalSettingsApi::class)
    object Settings : SettingsRepository by SettingsRepository(settings, DefaultDispatcher)

    @OptIn(ExperimentalSettingsApi::class)
    object User : UserRepository by UserRepository(
        RemoteUserDataSource(),
        DefaultDispatcher,
        RemoteMappers.UserMapper,
        LocalMappers.UserMapper,
        settings,
    )

    object Post : PostRepository by PostRepositoryImpl(
        RemotePostDataSource(),
        RainbowDatabase,
        DefaultDispatcher,
        RainbowDatabase.RemotePostMapper,
        RainbowDatabase.LocalPostMapper,
    )

    object Subreddit : SubredditRepository by SubredditRepositoryImpl(
        RemoteSubredditDataSource(),
        RemoteModeratorDataSource(),
        RemoteWikiDataSourceImpl(),
        RainbowDatabase.localSubredditQueries,
        DefaultDispatcher,
        RemoteMappers.SubredditMapper,
        LocalMappers.SubredditMapper,
        RemoteMappers.ModeratorMapper,
        RainbowDatabase.WikiPageMapper,
    )

    @OptIn(ExperimentalSettingsApi::class)
    object Comment : CommentRepository by CommentRepositoryImpl(
        RemoteCommentDataSource(),
        RainbowDatabase.localCommentQueries,
        settings,
        DefaultDispatcher,
        RainbowDatabase.RemoteCommentMapper,
        LocalMappers.CommentMapper(RainbowDatabase),
    )

    object Rule : RuleRepository by RuleRepository(
        RemoteRuleDataSource(),
        DefaultDispatcher,
        RemoteMappers.RuleMapper
    )

    object Message : MessageRepository by MessageRepositoryImpl(
        RemoteMessageDataSource(),
        DefaultDispatcher,
        RemoteMappers.MessageMapper
    )

}