package com.rainbow.common.subreddit

enum class SubredditTab {
    Posts, Description, Wiki, Rules, Moderators;

    companion object {
        val Default = Posts
    }
}