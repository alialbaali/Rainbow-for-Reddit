package com.rainbow.domain.models

enum class TimeSorting {
    Hour, Day, Week,
    Month, Year, All;

    companion object {
        val Default = Week
    }
}

enum class Theme {
    Dark, Light, System,
}

enum class PostLayout {
    Card, Compact,
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

typealias ProfilePostSorting = UserPostSorting

enum class UserPostSorting : PostSorting {
    Hot, New, Top, Controversial;

    override val isTimeSorting: Boolean
        get() = this == Top || this == Controversial

    companion object {
        val Default = Hot
    }
}

enum class SubredditPostSorting : PostSorting {
    Hot, Top, Controversial, Rising;

    override val isTimeSorting: Boolean
        get() = this == Top || this == Controversial

    companion object {
        val Default = Hot
    }
}

enum class HomePostSorting : PostSorting {
    Best, New, Controversial,
    Top, Hot, Rising;


    override val isTimeSorting: Boolean
        get() = this == Top || this == Controversial

    companion object {
        val Default = Hot
    }
}

enum class SearchPostSorting : PostSorting {
    Relevance, New, Top, Hot, CommentsCount;

    override val isTimeSorting: Boolean
        get() = true

    companion object {
        val Default = Hot
    }
}

sealed interface Sorting {
    val name: String

    companion object
}

sealed interface PostSorting : Sorting {
    val isTimeSorting: Boolean
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


enum class PostCommentSorting : Sorting {
    Confidence, Top, Best, New, Old, Controversial, QA;

    companion object {
        val Default = Top
    }
}

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

