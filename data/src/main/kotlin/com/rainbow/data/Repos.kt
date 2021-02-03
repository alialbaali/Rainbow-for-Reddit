package com.rainbow.data

import com.rainbow.data.repository.*
import com.rainbow.domain.models.PostsSorting
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.repository.*
import com.rainbow.remote.impl.*
import kotlinx.coroutines.Dispatchers

private val DefaultDispatcher = Dispatchers.IO

object Repos {

    object UserRepo : UserRepository by UserRepository(
        RemoteUserDataSource(),
        DefaultDispatcher,
        Mappers.UserMapper,
    )

    object PostRepo : PostRepository by PostRepository(
        RemotePostDataSource(),
        DefaultDispatcher,
        Mappers.PostMapper,
    )

    object CommentRepo : CommentRepository by CommentRepository(
        RemoteCommentDataSource(),
        DefaultDispatcher,
        Mappers.CommentMapper,
    )

    object SubredditRepo : SubredditRepository by SubredditRepository(
        RemoteSubredditDataSource(),
        DefaultDispatcher,
        Mappers.SubredditMapper,
    )

    object RuleRepo : RuleRepository by RuleRepository(
        RemoteRuleDataSource(),
        DefaultDispatcher,
        Mappers.RuleMapper
    )

}