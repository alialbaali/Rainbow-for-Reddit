package com.rainbow.domain.models

enum class Theme {
    Dark, Light, System,
}

enum class PostLayout {
    Compact, Card, Large;

    companion object {
        val Default = Compact
    }
}

enum class MarkPostAsRead {
    OnClick, OnScroll;

    companion object {
        val Default = OnClick
    }
}

enum class Vote {
    Up, Down, None,
}

enum class MessagesSorting {
    Inbox, Unread, Sent;

    companion object {
        val Default = Inbox
    }
}

enum class UserSort {
    Hot, New, Top, Controversial,
}

typealias ProfileSort = UserSort

enum class MoreChildren {
    confidence, top, new, controversial, old, random, qa, live
}

enum class SetSuggestedSort {
    confidence, top, new, controversial, old, random, qa, live, blank
}

enum class Article {
    confidence, top, new, controversial, old, random, qa, live
}

enum class DuplicatedArticle {
    num_comments, new
}

enum class ModConversations {
    recent, mod, user, unread
}

enum class SubredditSearch {
    relevance, hot, top, new, comments
}

enum class SiteAdminSuggestedCommentsSort {
    confidence, top, new, controversial, old, random, qa, live
}

enum class UsersSearch {
    relevance, activity
}


sealed class UsersWhere {
    object Popular : UsersWhere()
    object New : UsersWhere()
}

sealed class UserWhere {
    object OverView
    object Submitted
    object Upvoted
    object Downvoted
    object Comments
    object Hidden
    object Glided
    object Saved
}

