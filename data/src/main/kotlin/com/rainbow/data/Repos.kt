package com.rainbow.data

import com.rainbow.data.repository.*
import com.rainbow.domain.repository.*
import com.rainbow.local.RainbowDatabase
import com.rainbow.remote.impl.*
import kotlinx.coroutines.Dispatchers

private val DefaultDispatcher = Dispatchers.IO

object Repos {

    object User : UserRepository by UserRepository(
        RemoteUserDataSource(),
        DefaultDispatcher,
        RemoteMappers.UserMapper,
        LocalMappers.UserMapper
    )

    object Post : PostRepository by PostRepository(
        RemotePostDataSource(),
        RainbowDatabase.localPostQueries,
        DefaultDispatcher,
        RemoteMappers.PostMapper,
        LocalMappers.PostMapper,
    )

    object SubredditRepo : SubredditRepository by SubredditRepository(
        RemoteSubredditDataSource(),
        RainbowDatabase.localSubredditQueries,
        DefaultDispatcher,
        RemoteMappers.SubredditMapper,
        LocalMappers.SubredditMapper,
    )

    object Comment : CommentRepository by CommentRepository(
        RemoteCommentDataSource(),
        DefaultDispatcher,
        RemoteMappers.CommentMapper,
    )

    object RuleRepo : RuleRepository by RuleRepository(
        RemoteRuleDataSource(),
        DefaultDispatcher,
        RemoteMappers.RuleMapper
    )

}