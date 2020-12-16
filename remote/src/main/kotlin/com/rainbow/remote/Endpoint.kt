package com.rainbow.remote

import kotlin.reflect.KProperty

sealed class Endpoint(val path: String) {

    private companion object {
        const val BaseActionPath = "api/"
        const val BaseUserPath = "user/"
        const val BaseSubredditPath = "r/"
        const val BaseCommentPath = "comment/"
        const val BaseMessagePath = "message/"
    }

    object Users {

        class About(userName: String) : Endpoint("$BaseUserPath$userName/about")

        object CheckUserName : Endpoint(BaseActionPath + "username_available")

    }

    object Subreddits {

        class About(subredditName: String) : Endpoint("$BaseSubredditPath$subredditName/about")

        object Mine : Endpoint("subreddits/mine/subscriber")

        object Subscribe : Endpoint(BaseActionPath + "subscribe")

        object UnSubscribe : Endpoint(BaseActionPath + "subscribe")

        object Favorite : Endpoint(BaseActionPath + "favorite")

        object UnFavorite : Endpoint(BaseActionPath + "favorite")

    }

    object Posts {

        class SubredditPosts(subredditName: String, postsSorting: String) :
            Endpoint("$BaseSubredditPath$subredditName/$postsSorting")

        class UserPosts(userName: String, postsSorting: String) :
            Endpoint("$BaseUserPath$userName/submitted/$postsSorting")

        object UpVote : Endpoint(BaseActionPath + "vote")

        object DownVote : Endpoint(BaseActionPath + "vote")

        object UnVote : Endpoint(BaseActionPath + "vote")

        object Save : Endpoint(BaseActionPath + "save")

        object UnSave : Endpoint(BaseActionPath + "unsave")

        object Hide : Endpoint(BaseActionPath + "hide")

        object UnHide : Endpoint(BaseActionPath + "unhide")

        object Delete : Endpoint(BaseActionPath + "del")

        object Submit : Endpoint(BaseActionPath + "submit")

    }

    object Comments {

        class Get(postIdPrefixed: String) : Endpoint("$BaseCommentPath$postIdPrefixed")

        object Save : Endpoint(BaseActionPath + "save")

        object UnSave : Endpoint(BaseActionPath + "unsave")

    }

    object Messages {

        class Get(messagesSorting: String) : Endpoint("$BaseMessagePath$messagesSorting")

        object Compose : Endpoint(BaseActionPath + "compose")

        object Read : Endpoint(BaseActionPath + "read_message")

        object UnRead : Endpoint(BaseActionPath + "unread_message")

        object ReadAll : Endpoint(BaseActionPath + "read_all_messages")

        object Delete : Endpoint(BaseActionPath + "del_message")

    }

    object Mods {

        class Get(subredditName: String) : Endpoint("$BaseSubredditPath$subredditName/about/moderators")

    }

    object Rules {

        class Get(subredditName: String) : Endpoint("$BaseSubredditPath$subredditName/about/rules")

    }

}


operator fun Endpoint.getValue(thisObj: Any?, property: KProperty<*>): String = path