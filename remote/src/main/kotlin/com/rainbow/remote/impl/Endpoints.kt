package com.rainbow.remote.impl

import kotlin.reflect.KProperty

internal sealed class Endpoint(val path: String) {

    private companion object {
        const val ActionPath = "api/"
        const val UserPath = "user/"
        const val SubredditPath = "r/"
        const val CommentPath = "comment/"
        const val MessagePath = "message/"
    }

    object Users {

        // https://oauth.reddit.com/user/{user-name}/about
        class About(userName: String) : Endpoint("$UserPath$userName/about")

        // https://oauth.reddit.com/user/username_available
        object CheckUserName : Endpoint(UserPath + "username_available")

        object BlockUser : Endpoint(ActionPath + "block_user")

    }

    object Subreddits {

        // https://oauth.reddit.com/r/{subreddit-name}/about
        class About(subredditName: String) : Endpoint("$SubredditPath$subredditName/about")

        // https://oauth.reddit.com/subreddits/mine/subscriber
        object Mine : Endpoint("subreddits/mine/subscriber")

        // https://oauth.reddit.com/api/subscribe
        object Subscribe : Endpoint(ActionPath + "subscribe")

        // https://oauth.reddit.com/api/subscribe
        object UnSubscribe : Endpoint(ActionPath + "subscribe")

        // https://oauth.reddit.com/api/favorite
        object Favorite : Endpoint(ActionPath + "favorite")

        // https://oauth.reddit.com/api/favorite
        object UnFavorite : Endpoint(ActionPath + "favorite")

    }

    object Posts {

        // https://oauth.reddit.com/r/{main-page-sorting}
        class MainPagePosts(mainPageSorting: String) : Endpoint(mainPageSorting)

        // https://oauth.reddit.com/r/{subreddit-name}/{post-sorting}
        class SubredditPosts(subredditName: String, postsSorting: String) :
            Endpoint("$SubredditPath$subredditName/$postsSorting")

        // https://oauth.reddit.com/user/{user-name}/submitted/{post-sorting}
        class UserPosts(userName: String, postsSorting: String) :
            Endpoint("$UserPath$userName/submitted/$postsSorting")

        // https://oauth.reddit.com/api/vote
        object Upvote : Endpoint(ActionPath + "vote")

        // https://oauth.reddit.com/api/vote
        object Downvote : Endpoint(ActionPath + "vote")

        // https://oauth.reddit.com/api/vote
        object Unvote : Endpoint(ActionPath + "vote")

        // https://oauth.reddit.com/api/save
        object Save : Endpoint(ActionPath + "save")

        // https://oauth.reddit.com/api/unsave
        object UnSave : Endpoint(ActionPath + "unsave")

        // https://oauth.reddit.com/api/hide
        object Hide : Endpoint(ActionPath + "hide")

        // https://oauth.reddit.com/api/unhide
        object UnHide : Endpoint(ActionPath + "unhide")

        // https://oauth.reddit.com/api/del
        object Delete : Endpoint(ActionPath + "del")

        // https://oauth.reddit.com/api/submit
        object Submit : Endpoint(ActionPath + "submit")

    }

    object Comments {

        // https://oauth.reddit.com/comment/{post-id}
        class PostComments(postIdPrefixed: String) : Endpoint("$CommentPath$postIdPrefixed")

        class UserComments(userName: String) : Endpoint("$UserPath$userName/comments")

        // https://oauth.reddit.com/api/save
        object Save : Endpoint(ActionPath + "save")

        // https://oauth.reddit.com/api/unsave
        object UnSave : Endpoint(ActionPath + "unsave")

        // https://oauth.reddit.com/api/vote
        object Upvote : Endpoint(ActionPath + "vote")

        // https://oauth.reddit.com/api/vote
        object Downvote : Endpoint(ActionPath + "vote")

        // https://oauth.reddit.com/api/vote
        object Unvote : Endpoint(ActionPath + "vote")

    }

    object Messages {

        // https://oauth.reddit.com/message/{message-sorting}
        class Get(messagesSorting: String) : Endpoint("$MessagePath$messagesSorting")

        // https://oauth.reddit.com/api/compose
        object Compose : Endpoint(ActionPath + "compose")

        // https://oauth.reddit.com/api/read_message
        object Read : Endpoint(ActionPath + "read_message")

        // https://oauth.reddit.com/api/unread_message
        object UnRead : Endpoint(ActionPath + "unread_message")

        // https://oauth.reddit.com/api/read_all_messages
        object ReadAll : Endpoint(ActionPath + "read_all_messages")

        // https://oauth.reddit.com/api/del_message
        object Delete : Endpoint(ActionPath + "del_message")

    }

    object Mods {

        // https://oauth.reddit.com/r/{subreddit-name}/about/moderators
        class Get(subredditName: String) : Endpoint("$SubredditPath$subredditName/about/moderators")

    }

    object Rules {

        // https://oauth.reddit.com/r/{subreddit-name}/about/rules
        class Get(subredditName: String) : Endpoint("$SubredditPath$subredditName/about/rules")

    }

}

internal operator fun Endpoint.getValue(thisObj: Any?, property: KProperty<*>): String = path