package com.rainbow.desktop.profile

enum class ProfileTab {
    Overview, Submitted, Saved, Hidden, Upvoted, Downvoted, Comments, Karma, Trophies;

    companion object {
        val Default = Overview
    }
}