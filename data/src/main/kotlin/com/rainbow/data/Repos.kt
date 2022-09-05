package com.rainbow.data

import com.rainbow.data.repository.*
import com.rainbow.domain.repository.*
import com.rainbow.local.*
import com.rainbow.remote.impl.*
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

object Repos {

    private val DefaultDispatcher = Dispatchers.IO

    @OptIn(
        ExperimentalSettingsImplementation::class,
        ExperimentalSettingsApi::class,
        ExperimentalCoroutinesApi::class,
    )
    private val settings = Settings()
        .let { it as ObservableSettings }

    @OptIn(
        ExperimentalSettingsImplementation::class,
        ExperimentalSettingsApi::class,
        ExperimentalCoroutinesApi::class,
    )
    private val flowSettings = settings.toFlowSettings(DefaultDispatcher)

    @OptIn(ExperimentalSettingsApi::class)
    object Settings : SettingsRepository by SettingsRepositoryImpl(settings, flowSettings, DefaultDispatcher)

    @OptIn(ExperimentalSettingsApi::class)
    object User : UserRepository by UserRepositoryImpl(
        RemoteUserDataSource(),
        LocalUserDataSourceImpl(),
        flowSettings,
        DefaultDispatcher,
        Mappers.UserMapper,
    )

    object Post : PostRepository by PostRepositoryImpl(
        RemotePostDataSource(),
        RemoteSubredditDataSourceImpl(),
        LocalPostDataSourceImpl(),
        flowSettings,
        DefaultDispatcher,
        Mappers.PostMapper,
        Mappers.SubredditMapper,
    )

    @OptIn(ExperimentalSettingsApi::class)
    object Subreddit : SubredditRepository by SubredditRepositoryImpl(
        RemoteSubredditDataSourceImpl(),
        RemoteModeratorDataSource(),
        RemoteWikiDataSourceImpl(),
        RemoteSubredditFlairDataSource(),
        RemoteRuleDataSource(),
        LocalSubredditDataSourceImpl(),
        flowSettings,
        DefaultDispatcher,
        Mappers.SubredditMapper,
        Mappers.ModeratorMapper,
        Mappers.WikiPageMapper,
        Mappers.RuleMapper,
    )

    @OptIn(ExperimentalSettingsApi::class)
    object Comment : CommentRepository by CommentRepositoryImpl(
        RemoteCommentDataSource(),
        LocalCommentDataSourceImpl(),
        flowSettings,
        DefaultDispatcher,
        Mappers.CommentMapper,
    )

    object Message : MessageRepository by MessageRepositoryImpl(
        RemoteMessageDataSource(),
        LocalMessageDataSourceImpl(),
        DefaultDispatcher,
        Mappers.MessageMapper
    )

    object Item : ItemRepository by ItemRepositoryImpl(
        RemoteItemDataSourceImpl(),
        LocalItemDataSourceImpl(),
        flowSettings,
        DefaultDispatcher,
        Mappers.ItemMapper,
    )

}