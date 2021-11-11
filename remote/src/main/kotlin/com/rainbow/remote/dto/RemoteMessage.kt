package com.rainbow.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteMessage(
    @SerialName("associated_awarding_id")
    val associatedAwardingId: String? = null, // null
    @SerialName("author")
    val author: String? = null, // rapsforlife647
    @SerialName("author_fullname")
    val authorFullname: String? = null, // t2_1ftztr81
    @SerialName("body")
    val body: String? = null, // Hi! Post on r/EngineeringResumes. Please read our wiki before posting: https://old.reddit.com/r/EngineeringResumes/comments/m2cc65/new_and_improved_wiki/
    @SerialName("body_html")
    val bodyHtml: String? = null, // &lt;!-- SC_OFF --&gt;&lt;div class="md"&gt;&lt;p&gt;Hi! Post on &lt;a href="/r/EngineeringResumes"&gt;r/EngineeringResumes&lt;/a&gt;. Please read our wiki before posting: &lt;a href="https://old.reddit.com/r/EngineeringResumes/comments/m2cc65/new_and_improved_wiki/"&gt;https://old.reddit.com/r/EngineeringResumes/comments/m2cc65/new_and_improved_wiki/&lt;/a&gt;&lt;/p&gt;&lt;/div&gt;&lt;!-- SC_ON --&gt;
    @SerialName("context")
    val context: String? = null,
    @SerialName("created")
    val created: Double? = null, // 1632719219.0
    @SerialName("created_utc")
    val createdUtc: Double? = null, // 1632719219.0
    @SerialName("dest")
    val dest: String? = null, // LoneWalker20
//    @SerialName("distinguished")
//    val distinguished: Any? = null, // null
//    @SerialName("first_message")
//    val firstMessage: Any? = null, // null
//    @SerialName("first_message_name")
//    val firstMessageName: Any? = null, // null
    @SerialName("id")
    val id: String? = null, // 167ib21
//    @SerialName("likes")
//    val likes: Any? = null, // null
    @SerialName("name")
    val name: String? = null, // t4_167ib21
    @SerialName("new")
    val new: Boolean? = null, // false
    @SerialName("num_comments")
    val numComments: Int? = null, // null
    @SerialName("parent_id")
    val parentId: String? = null, // null
//    @SerialName("replies")
//    val replies: String? = null,
    @SerialName("score")
    val score: Int? = null, // 0
    @SerialName("subject")
    val subject: String? = null, // /r/EngineeringResumes
    @SerialName("subreddit")
    val subreddit: String? = null, // null
    @SerialName("subreddit_name_prefixed")
    val subredditNamePrefixed: String? = null, // null
    @SerialName("type")
    val type: String? = null, // unknown
    @SerialName("was_comment")
    val wasComment: Boolean? = null // false
)