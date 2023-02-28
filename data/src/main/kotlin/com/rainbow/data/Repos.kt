package com.rainbow.data

import com.rainbow.data.repository.*
import com.rainbow.domain.repository.*
import com.rainbow.local.*
import com.rainbow.remote.impl.*
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.Dispatchers

object Repos {

    private val DefaultDispatcher = Dispatchers.IO
    private val LocalItemDataSource = LocalItemDataSourceImpl()

    private val settings = Settings()
        .let { it as ObservableSettings }

    @OptIn(ExperimentalSettingsApi::class)
    private val flowSettings = settings.toFlowSettings(DefaultDispatcher)

    @OptIn(ExperimentalSettingsApi::class)
    object Settings : SettingsRepository by SettingsRepositoryImpl(settings, flowSettings, DefaultDispatcher)

    @OptIn(ExperimentalSettingsApi::class)
    object User : UserRepository by UserRepositoryImpl(
        Subreddit,
        RemoteUserDataSourceImpl(),
        LocalUserDataSourceImpl(),
        RemoteKarmaDataSourceImpl(),
        RemoteTrophyDataSourceImpl(),
        flowSettings,
        DefaultDispatcher,
        Mappers.UserMapper,
        Mappers.KarmaMapper,
        Mappers.TrophyMapper,
    )

    @OptIn(ExperimentalSettingsApi::class)
    object Post : PostRepository by PostRepositoryImpl(
        User,
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
        LocalSubredditFlairDataSourceImpl(),
        flowSettings,
        DefaultDispatcher,
        Mappers.SubredditMapper,
        Mappers.ModeratorMapper,
        Mappers.WikiPageMapper,
        Mappers.RuleMapper,
        Mappers.FlairMapper,
    )

    @OptIn(ExperimentalSettingsApi::class)
    object Comment : CommentRepository by CommentRepositoryImpl(
        User,
        Subreddit,
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
        User,
        Subreddit,
        RemoteItemDataSourceImpl(),
        LocalItemDataSource,
        flowSettings,
        DefaultDispatcher,
        Mappers.ItemMapper,
    )

}