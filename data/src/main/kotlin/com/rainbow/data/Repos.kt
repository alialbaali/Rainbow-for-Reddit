package com.rainbow.data

import com.rainbow.data.repository.*
import com.rainbow.domain.repository.*
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
    object User : UserRepository by UserRepositoryImpl(
        RemoteUserDataSource(),
        settings,
        DefaultDispatcher,
        Mappers.UserMapper,
    )

    object Post : PostRepository by PostRepositoryImpl(
        RemotePostDataSource(),
        settings,
        DefaultDispatcher,
        Mappers.PostMapper,
    )

    @OptIn(ExperimentalSettingsApi::class)
    object Subreddit : SubredditRepository by SubredditRepositoryImpl(
        RemoteSubredditDataSource(),
        RemoteModeratorDataSource(),
        RemoteWikiDataSourceImpl(),
        RemoteSubredditFlairDataSource(),
        RemoteRuleDataSource(),
        settings,
        DefaultDispatcher,
        Mappers.SubredditMapper,
        Mappers.ModeratorMapper,
        Mappers.WikiPageMapper,
        Mappers.RuleMapper,
    )

    @OptIn(ExperimentalSettingsApi::class)
    object Comment : CommentRepository by CommentRepositoryImpl(
        RemoteCommentDataSource(),
        settings,
        DefaultDispatcher,
        Mappers.CommentMapper,
    )

    object Message : MessageRepository by MessageRepositoryImpl(
        RemoteMessageDataSource(),
        DefaultDispatcher,
        Mappers.MessageMapper
    )

}