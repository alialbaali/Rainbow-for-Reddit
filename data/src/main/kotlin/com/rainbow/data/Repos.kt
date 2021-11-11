package com.rainbow.data

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

    object Post : PostRepository by PostRepository(
        RemotePostDataSource(),
        RainbowDatabase.localPostQueries,
        RainbowDatabase.localLinkPostQueries,
        settings,
        DefaultDispatcher,
        RemoteMappers.PostMapper,
        LocalMappers.PostMapper,
    )

    object Subreddit : SubredditRepository by SubredditRepository(
        RemoteSubredditDataSource(),
        RemoteModeratorDataSource(),
        RainbowDatabase.localSubredditQueries,
        DefaultDispatcher,
        RemoteMappers.SubredditMapper,
        LocalMappers.SubredditMapper,
        RemoteMappers.ModeratorMapper,
    )

    @OptIn(ExperimentalSettingsApi::class)
    object Comment : CommentRepository by CommentRepository(
        RemoteCommentDataSource(),
        RainbowDatabase.localCommentQueries,
        settings,
        DefaultDispatcher,
        RemoteMappers.CommentMapper,
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