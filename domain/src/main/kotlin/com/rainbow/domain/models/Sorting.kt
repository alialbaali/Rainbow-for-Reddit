package com.rainbow.domain.models

sealed interface ItemSorting

sealed interface PostSorting : ItemSorting {
    val isTimeSorting: Boolean
}

enum class TimeSorting {
    Hour, Day, Week,
    Month, Year, All;

    companion object {
        val Default = Week
    }
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

enum class PostCommentSorting : ItemSorting {
    Confidence, Top, Best, New, Old, Controversial, QA;

    companion object {
        val Default = Top
    }
}
