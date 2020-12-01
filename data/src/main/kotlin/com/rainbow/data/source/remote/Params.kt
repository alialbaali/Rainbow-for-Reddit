package com.rainbow.data.source.remote

internal object Keys {
    const val User = "user"
    const val Id = "id"
    const val Direction = "dir"
    const val After = "after"
    const val Time = "t"
    const val Action = "action"
    const val Subreddit = "sr"
    const val SubredditName = "sr_name"
    const val Favorite = "make_favorite"
    const val Limit = "limit"
}

internal object Values {
    const val Sub = "sub"
    const val unSub = "unsub"
    const val Upvote = 1
    const val Unvote = 0
    const val Downvote = -1
}