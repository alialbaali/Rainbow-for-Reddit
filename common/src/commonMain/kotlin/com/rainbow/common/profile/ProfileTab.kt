package com.rainbow.common.profile

enum class ProfileTab {
    Overview, Submitted, Saved, Hidden, Upvoted, Downvoted, Comments;

    companion object {
        val Default = Overview
    }
}