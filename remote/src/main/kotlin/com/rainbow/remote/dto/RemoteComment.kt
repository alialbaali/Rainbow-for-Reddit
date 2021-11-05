package com.rainbow.remote.dto

import com.rainbow.remote.Item
import com.rainbow.remote.Listing
import com.rainbow.remote.toList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable
data class RemoteComment internal constructor(
    @SerialName("all_awardings")
    val allAwardings: List<RemoteAward>? = null,
//    @SerialName("approved_at_utc")
//    val approvedAtUtc: Any? = null, // null
//    @SerialName("approved_by")
//    val approvedBy: Any? = null, // null
//    @SerialName("archived")
//    val archived: Boolean? = null, // false
//    @SerialName("associated_award")
//    val associatedAward: Any? = null, // null
    @SerialName("author")
    val author: String? = null, // arounet
    @SerialName("author_flair_background_color")
    val authorFlairBackgroundColor: String? = null, // null
    @SerialName("author_flair_css_class")
    val authorFlairCssClass: String? = null, // null
//    @SerialName("author_flair_richtext")
//    val authorFlairRichtext: List<String>? = null,
    @SerialName("author_flair_template_id")
    val authorFlairTemplateId: String? = null, // null
    @SerialName("author_flair_text")
    val authorFlairText: String? = null, // null
    @SerialName("author_flair_text_color")
    val authorFlairTextColor: String? = null, // null
    @SerialName("author_flair_type")
    val authorFlairType: String? = null, // text
    @SerialName("author_fullname")
    val authorFullname: String? = null, // t2_zvxev
//    @SerialName("author_patreon_flair")
//    val authorPatreonFlair: Boolean? = null, // false
//    @SerialName("author_premium")
//    val authorPremium: Boolean? = null, // false
//    @SerialName("awarders")
//    val awarders: List<Unit>? = null,
//    @SerialName("banned_at_utc")
//    val bannedAtUtc: Any? = null, // null
//    @SerialName("banned_by")
//    val bannedBy: Any? = null, // null
    @SerialName("body")
    val body: String? = null, // More generally, you might be interested by this article on [almost integers](https://en.wikipedia.org/wiki/Almost_integer), which includes Ramanujan’s constant exp(pi* sqrt(163)), which is surprisingly close to an integer !
    @SerialName("body_html")
    val bodyHtml: String? = null, // &lt;div class="md"&gt;&lt;p&gt;More generally, you might be interested by this article on &lt;a href="https://en.wikipedia.org/wiki/Almost_integer"&gt;almost integers&lt;/a&gt;, which includes Ramanujan’s constant exp(pi* sqrt(163)), which is surprisingly close to an integer !&lt;/p&gt;&lt;/div&gt;
    @SerialName("can_gild")
    val canGild: Boolean? = null, // true
//    @SerialName("can_mod_post")
//    val canModPost: Boolean? = null, // false
//    @SerialName("collapsed")
//    val collapsed: Boolean? = null, // false
//    @SerialName("collapsed_because_crowd_control")
//    val collapsedBecauseCrowdControl: Any? = null, // null
//    @SerialName("collapsed_reason")
//    val collapsedReason: Any? = null, // null
//    @SerialName("comment_type")
//    val commentType: Any? = null, // null
    @SerialName("controversiality")
    val controversiality: Int? = null, // 0
    @SerialName("created")
    val created: Double? = null, // 1612473573.0
    @SerialName("created_utc")
    val createdUtc: Double? = null, // 1612444773.0
    @SerialName("depth")
    val depth: Int? = null, // 0
//    @SerialName("distinguished")
//    val distinguished: Boolean? = null, // null
    @SerialName("downs")
    val downs: Int? = null, // 0
//    @SerialName("edited")
//    val edited: Boolean? = null, // false
    @SerialName("gilded")
    val gilded: Int? = null, // 0
//    @SerialName("gildings")
//    val gildings: Gildings? = null,
    @SerialName("id")
    val id: String? = null, // glzfuit
    @SerialName("is_submitter")
    val isSubmitter: Boolean? = null, // false
//    @SerialName("likes")
//    val likes: Any? = null, // null
    @SerialName("link_id")
    val linkId: String? = null, // t3_lcarmx
    @SerialName("locked")
    val locked: Boolean? = null, // false
//    @SerialName("mod_note")
//    val modNote: Any? = null, // null
//    @SerialName("mod_reason_by")
//    val modReasonBy: Any? = null, // null
//    @SerialName("mod_reason_title")
//    val modReasonTitle: Any? = null, // null
//    @SerialName("mod_reports")
//    val modReports: List<Any>? = null,
    @SerialName("name")
    val name: String? = null, // t1_glzfuit
    @SerialName("no_follow")
    val noFollow: Boolean? = null, // false
    @SerialName("num_reports")
    val numReports: Int? = null, // null
    @SerialName("parent_id")
    val parentId: String? = null, // t3_lcarmx
    @SerialName("permalink")
    val permalink: String? = null, // /r/math/comments/lcarmx/numbers_whose_powers_are_almost_integers/glzfuit/
//    @SerialName("removal_reason")
    val removalReason: String? = null, // null
    @SerialName("replies")
    @Serializable(RepliesTransformingSerializer::class)
    internal val replies: Item<Listing<RemoteComment>>? = null,
//    @SerialName("report_reasons")
//    val reportReasons: Any? = null, // null
    @SerialName("saved")
    val saved: Boolean? = null, // false
    @SerialName("score")
    val score: Int? = null, // 33
    @SerialName("score_hidden")
    val scoreHidden: Boolean? = null, // false
    @SerialName("send_replies")
    val sendReplies: Boolean? = null, // true
    @SerialName("stickied")
    val stickied: Boolean? = null, // false
    @SerialName("subreddit")
    val subreddit: String? = null, // math
    @SerialName("subreddit_id")
    val subredditId: String? = null, // t5_2qh0n
    @SerialName("subreddit_name_prefixed")
    val subredditNamePrefixed: String? = null, // r/math
    @SerialName("subreddit_type")
    val subredditType: String? = null, // public
//    @SerialName("top_awarded_type")
//    val topAwardedType: Any? = null, // null
    @SerialName("total_awards_received")
    val totalAwardsReceived: Int? = null, // 0
//    @SerialName("treatment_tags")
//    val treatmentTags: List<Any>? = null,
    @SerialName("ups")
    val ups: Int? = null, // 33
//    @SerialName("user_reports")
//    val userReports: List<Any>? = null
)

val RemoteComment.replies: List<RemoteComment>?
    get() = replies?.data?.toList()

private object RepliesTransformingSerializer : JsonTransformingSerializer<Item<Listing<RemoteComment>>>(
    Item.serializer(Listing.serializer(RemoteComment.serializer()))
) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        val result = if (element is JsonPrimitive)
            JsonObject(mapOf("data" to JsonObject(mapOf("children" to JsonArray(emptyList())))))
        else
            element
        return super.transformDeserialize(result)
    }
}