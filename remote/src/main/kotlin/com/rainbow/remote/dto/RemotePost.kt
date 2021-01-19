package com.rainbow.remote.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemotePost(
    val id: String,

    @SerialName("author_fullname")
    val authorFullname: String,

    val author: String,

    val title: String,

    @CouldBeEmpty
    val selftext: String,

    val subreddit: String,

    @SerialName("subreddit_id")
    val subredditId: String,

    val score: Long,

    // Always 0
    val downs: Long,

    @SerialName("upvote_ratio")
    val upvoteRatio: Float,

    @SerialName("all_awardings")
    val allAwards: List<RemoteAward>,

    @SerialName("total_awards_received")
    val totalAwardsReceived: Long,

    val spoiler: Boolean,

    @SerialName("num_comments")
    val numComments: Long,

    val pinned: Boolean,

    @Contextual
    internal val edited: Any,

    val created: Double,

    @SerialName("created_utc")
    val createdUtc: Double,

    val over18: Boolean,

    val locked: Boolean,

    @SerialName("hide_score")
    val hideScore: Boolean,

    @SerialName("is_original_content")
    val isOriginalContent: Boolean,

    // post url such as https://www.reddit.com/r/AskReddit/comments/j44ppb/moratorium_on_questions_related_to_us_politics/
    val url: String,
)

val RemotePost.isEdited get() = edited as? Boolean

val RemotePost.editDate get() = edited as? Double