package com.rainbow.remote.source

import com.rainbow.remote.Listing
import com.rainbow.remote.RedditResponse
import com.rainbow.remote.dto.RemotePost

interface RemotePostDataSource {

    suspend fun getSubredditPosts(
        subredditName: String,
        postsSorting: String,
        timeSorting: String
    ): RedditResponse<Listing<RemotePost>>

    suspend fun getUserPosts(
        username: String,
        postsSorting: String,
        timeSorting: String,
    ): RedditResponse<Listing<RemotePost>>

    suspend fun upvotePost(postId: String)

    suspend fun unvotePost(postId: String)

    suspend fun downvotePost(postId: String)

    suspend fun savePost(postId: String)

    suspend fun unSavePost(postId: String)

    suspend fun hidePost(postId: String)

    suspend fun unHidePost(postId: String)

}