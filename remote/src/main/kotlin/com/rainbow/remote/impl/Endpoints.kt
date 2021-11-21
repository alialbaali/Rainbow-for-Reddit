package com.rainbow.remote.impl

import kotlin.reflect.KProperty

internal sealed class Endpoint(val path: String) {

    private companion object {
        const val ActionPath = "api/"
        const val UserPath = "user/"
        const val SubredditPath = "r/"
        const val CommentPath = "comments/"
        const val MessagePath = "message/"
    }

    object Users {

        object CurrentUser : Endpoint(ActionPath + "v1/me")

        // https://oauth.reddit.com/user/{user-name}/about
        class About(userName: String) : Endpoint("$UserPath$userName/about")

        // https://oauth.reddit.com/user/username_available
        object CheckUserName : Endpoint(UserPath + "username_available")

        object BlockUser : Endpoint(ActionPath + "block_user")

        object ReportUser : Endpoint(ActionPath + "report_user")

        class OverView(userName: String) : Endpoint("$UserPath$userName/overview")

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

        class SubmitText(subredditName: String) : Endpoint("$SubredditPath$subredditName/${ActionPath}submit_text")

        class PostRequirements(subredditName: String) : Endpoint("${ActionPath}v1/$subredditName/post_requirements")

        object Search : Endpoint("subreddits/search")

    }

    object Posts {

        // https://oauth.reddit.com/{main-page-sorting}
        class MainPagePosts(mainPageSorting: String) : Endpoint(mainPageSorting)

        // https://oauth.reddit.com/r/{subreddit-name}/{post-sorting}
        class SubredditPosts(subredditName: String, postsSorting: String) :
            Endpoint("$SubredditPath$subredditName/$postsSorting")

        // https://oauth.reddit.com/user/{user-name}/submitted/{post-sorting}
        class UserSubmittedPosts(userName: String, postsSorting: String) :
            Endpoint("$UserPath$userName/submitted/$postsSorting")

        // https://oauth.reddit.com/user/{user-name}/upvoted/{post-sorting}
        class UserUpvotedPosts(userName: String, postsSorting: String) :
            Endpoint("$UserPath$userName/upvoted/$postsSorting")

        // https://oauth.reddit.com/user/{user-name}/downvoted/{post-sorting}
        class UserDownvotedPosts(userName: String, postsSorting: String) :
            Endpoint("$UserPath$userName/downvoted/$postsSorting")

        // https://oauth.reddit.com/user/{user-name}/hidden/{post-sorting}
        class UserHiddenPosts(userName: String, postsSorting: String) :
            Endpoint("$UserPath$userName/hidden/$postsSorting")

        // https://oauth.reddit.com/user/{user-name}/saved/{post-sorting}
        class UserSavedPosts(userName: String, postsSorting: String) :
            Endpoint("$UserPath$userName/saved/$postsSorting")

        object GetPost : Endpoint(ActionPath + "info")

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

        object Search: Endpoint("search")

    }

    object Comments {

        // https://oauth.reddit.com/comments/{post-id}
        class PostComments(postId: String) : Endpoint("$CommentPath$postId")

        class UserComments(userName: String) : Endpoint("$UserPath$userName/comments")

        object Replies: Endpoint("${ActionPath}morechildren")

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

        // https://oauth.reddit.com/message/inbox
        object Inbox : Endpoint(MessagePath + "inbox")

        object UnreadInbox : Endpoint(MessagePath + "unread")

        object Sent : Endpoint(MessagePath + "sent")

        object Messages : Endpoint(MessagePath + "messages")

        object Mentions : Endpoint(MessagePath + "mentions")

        object PostReplies : Endpoint(MessagePath + "selfreply")

        object CommentReplies : Endpoint(MessagePath + "comments")

        // https://oauth.reddit.com/api/compose
        object Compose : Endpoint(ActionPath + "compose")

        // https://oauth.reddit.com/api/read_message
        object Read : Endpoint(ActionPath + "read_message")

        // https://oauth.reddit.com/api/unread_message
        object Unread : Endpoint(ActionPath + "unread_message")

        // https://oauth.reddit.com/api/read_all_messages
        object ReadAll : Endpoint(ActionPath + "read_all_messages")

        // https://oauth.reddit.com/api/del_message
        object Delete : Endpoint(ActionPath + "del_message")

    }

    object Moderators {

        // https://oauth.reddit.com/r/{subreddit-name}/about/moderators
        class Get(subredditName: String) : Endpoint("$SubredditPath$subredditName/about/moderators")

    }

    object Rules {

        // https://oauth.reddit.com/r/{subreddit-name}/about/rules
        class Get(subredditName: String) : Endpoint("$SubredditPath$subredditName/about/rules")

    }

    object Flairs {

        class GetCurrentSubredditFlair(subredditName: String) :
            Endpoint("$SubredditPath$subredditName/${ActionPath}flairselector")

        class GetSubredditFlairs(subredditName: String) :
            Endpoint("$SubredditPath$subredditName/${ActionPath}user_flair_v2")

        class SelectSubredditFlair(subredditName: String) :
            Endpoint("$SubredditPath$subredditName/${ActionPath}selectflair")

        class UnSelectSubredditFlair(subredditName: String) :
            Endpoint("$SubredditPath$subredditName/${ActionPath}selectflair")

        class EnableSubredditFlair(subredditName: String) :
            Endpoint("$SubredditPath$subredditName/${ActionPath}setflairenabled")

        class DisableSubredditFlair(subredditName: String) :
            Endpoint("$SubredditPath$subredditName/${ActionPath}setflairenabled")

    }

    object Karma {
        object GetMine : Endpoint(ActionPath + "v1/me/karma")
    }

    object Trophies {
        class User(userName: String) : Endpoint("$UserPath$userName/trophies")
    }

}

internal operator fun Endpoint.getValue(thisObj: Any?, property: KProperty<*>): String = path