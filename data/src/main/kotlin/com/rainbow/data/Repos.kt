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
    private val LocalItemDataSource = LocalItemDataSourceImpl()

    @OptIn(
        ExperimentalSettingsImplementation::class,
        ExperimentalSettingsApi::class,
    )
    private val settings = Settings()
        .let { it as ObservableSettings }

    @OptIn(
        ExperimentalSettingsApi::class,
        ExperimentalCoroutinesApi::class,
    )
    private val flowSettings = settings.toFlowSettings(DefaultDispatcher)

    @OptIn(ExperimentalSettingsApi::class)
    object Settings : SettingsRepository by SettingsRepositoryImpl(settings, flowSettings, DefaultDispatcher)

    @OptIn(ExperimentalSettingsApi::class)
    object User : UserRepository by UserRepositoryImpl(
        RemoteUserDataSourceImpl(),
        LocalUserDataSourceImpl(),
        flowSettings,
        DefaultDispatcher,
        Mappers.UserMapper,
    )

    @OptIn(ExperimentalSettingsApi::class)
    object Post : PostRepository by PostRepositoryImpl(
        Subreddit,
        RemotePostDataSourceImpl(),
        LocalPostDataSourceImpl(),
        LocalItemDataSource,
        flowSettings,
        DefaultDispatcher,
        Mappers.PostMapper,
    )

    @OptIn(ExperimentalSettingsApi::class)
    object Subreddit : SubredditRepository by SubredditRepositoryImpl(
        RemoteSubredditDataSourceImpl(),
        RemoteModeratorDataSourceImpl(),
        RemoteWikiDataSourceImpl(),
        RemoteSubredditFlairDataSourceImpl(),
        RemoteRuleDataSourceImpl(),
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
        RemoteCommentDataSourceImpl(),
        LocalCommentDataSourceImpl(),
        LocalItemDataSource,
        flowSettings,
        DefaultDispatcher,
        Mappers.CommentMapper,
    )

    object Message : MessageRepository by MessageRepositoryImpl(
        RemoteMessageDataSourceImpl(),
        LocalMessageDataSourceImpl(),
        DefaultDispatcher,
        Mappers.MessageMapper
    )

    @OptIn(ExperimentalSettingsApi::class)
    object Item : ItemRepository by ItemRepositoryImpl(
        RemoteItemDataSourceImpl(),
        LocalItemDataSource,
        flowSettings,
        DefaultDispatcher,
        Mappers.ItemMapper,
    )

}