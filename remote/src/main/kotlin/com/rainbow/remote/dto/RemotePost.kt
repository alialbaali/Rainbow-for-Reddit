package com.rainbow.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemotePost(
    @SerialName("all_awardings")
    val allAwardings: List<RemoteAward>? = null,
//    @SerialName("allow_live_comments")
//    val allowLiveComments: Boolean? = null, // true
    @SerialName("approved_at_utc")
    val approvedAtUtc: Double? = null, // null
//    @SerialName("approved_by")
//    val approvedBy: Any? = null, // null
//    @SerialName("archived")
//    val archived: Boolean? = null, // false
    @SerialName("author")
    val author: String? = null, // plantshaveleaves
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
    val authorFullname: String? = null, // t2_kx7k6
//    @SerialName("author_patreon_flair")
//    val authorPatreonFlair: Boolean? = null, // false
//    @SerialName("author_premium")
//    val authorPremium: Boolean? = null, // false
//    @SerialName("awarders")
//    val awarders: List<Any>? = null,
//    @SerialName("banned_at_utc")
//    val bannedAtUtc: Any? = null, // null
//    @SerialName("banned_by")
//    val bannedBy: Any? = null, // null
//    @SerialName("can_gild")
//    val canGild: Boolean? = null, // true
//    @SerialName("can_mod_post")
//    val canModPost: Boolean? = null, // false
//    @SerialName("category")
//    val category: Any? = null, // null
//    @SerialName("clicked")
//    val clicked: Boolean? = null, // false
//    @SerialName("content_categories")
//    val contentCategories: Any? = null, // null
//    @SerialName("contest_mode")
//    val contestMode: Boolean? = null, // false
    @SerialName("created")
    val created: Double? = null, // 1613677072.0
    @SerialName("created_utc")
    val createdUtc: Double? = null, // 1613648272.0
//    @SerialName("discussion_type")
//    val discussionType: Any? = null, // null
//    @SerialName("distinguished")
//    val distinguished: Boolean? = null, // null
    @SerialName("domain")
    val domain: String? = null, // self.AskReddit
    @SerialName("downs")
    val downs: Int? = null, // 0
//    @SerialName("edited")
//    val edited: Boolean? = null, // false
    @SerialName("gilded")
    val gilded: Int? = null, // 0
//    @SerialName("gildings")
//    val gildings: Gildings? = null,
//    @SerialName("hidden")
//    val hidden: Boolean? = null, // false
//    @SerialName("hide_score")
//    val hideScore: Boolean? = null, // false
    @SerialName("id")
    val id: String? = null, // lmktus
//    @SerialName("is_crosspostable")
//    val isCrosspostable: Boolean? = null, // true
    @SerialName("is_meta")
    val isMeta: Boolean? = null, // false
    @SerialName("is_original_content")
    val isOriginalContent: Boolean? = null, // false
    @SerialName("is_reddit_media_domain")
    val isRedditMediaDomain: Boolean? = null, // false
    @SerialName("is_robot_indexable")
    val isRobotIndexable: Boolean? = null, // true
    @SerialName("is_self")
    val isSelf: Boolean? = null, // true
    @SerialName("is_video")
    val isVideo: Boolean? = null, // false
    @SerialName("likes")
    val likes: Boolean? = null, // null
    @SerialName("link_flair_background_color")
    val linkFlairBackgroundColor: String? = null,
    @SerialName("link_flair_css_class")
    val linkFlairCssClass: String? = null, // null
//    @SerialName("link_flair_richtext")
//    val linkFlairRichtext: List<String>? = null,
    @SerialName("link_flair_text")
    val linkFlairText: String? = null, // null
    @SerialName("link_flair_text_color")
    val linkFlairTextColor: String? = null, // dark
    @SerialName("link_flair_type")
    val linkFlairType: String? = null, // text
    @SerialName("locked")
    val locked: Boolean? = null, // false
    @SerialName("media")
    val media: Media? = null, // null
//    @SerialName("media_embed")
//    val mediaEmbed: MediaEmbed? = null,
//    @SerialName("media_only")
//    val mediaOnly: Boolean? = null, // false
//    @SerialName("mod_note")
//    val modNote: Any? = null, // null
//    @SerialName("mod_reason_by")
//    val modReasonBy: Any? = null, // null
//    @SerialName("mod_reason_title")
//    val modReasonTitle: Any? = null, // null
//    @SerialName("mod_reports")
//    val modReports: List<Any>? = null,
    @SerialName("name")
    val name: String? = null, // t3_lmktus
//    @SerialName("no_follow")
//    val noFollow: Boolean? = null, // false
    @SerialName("num_comments")
    val numComments: Int? = null, // 5073
    @SerialName("num_crossposts")
    val numCrossposts: Int? = null, // 0
    @SerialName("num_duplicates")
    val numDuplicates: Int? = null, // 1
    @SerialName("num_reports")
    val numReports: Int? = null, // null
//    @SerialName("over_18")
//    val over18: Boolean? = null, // false
    @SerialName("parent_whitelist_status")
    val parentWhitelistStatus: String? = null, // all_ads
    @SerialName("permalink")
    val permalink: String? = null, // /r/AskReddit/comments/lmktus/theres_a_minimum_age_for_certain_political_jobs/
    @SerialName("pinned")
    val pinned: Boolean? = null, // false
    @SerialName("pwls")
    val pwls: Int? = null, // 6
    @SerialName("quarantine")
    val quarantine: Boolean? = null, // false
//    @SerialName("removal_reason")
//    val removalReason: Any? = null, // null
//    @SerialName("removed_by")
//    val removedBy: Any? = null, // null
//    @SerialName("removed_by_category")
//    val removedByCategory: Any? = null, // null
//    @SerialName("report_reasons")
//    val reportReasons: Any? = null, // null
//    @SerialName("saved")
//    val saved: Boolean? = null, // false
    @SerialName("score")
    val score: Int? = null, // 61791
//    @SerialName("secure_media")
//    val secureMedia: Any? = null, // null
    @SerialName("secure_media_embed")
    val secureMediaEmbed: SecureMediaEmbed? = null,
    @SerialName("selftext")
    val selftext: String? = null,
//    @SerialName("selftext_html")
//    val selftextHtml: Any? = null, // null
    @SerialName("send_replies")
    val sendReplies: Boolean? = null, // true
    @SerialName("spoiler")
    val spoiler: Boolean? = null, // false
    @SerialName("stickied")
    val stickied: Boolean? = null, // false
    @SerialName("subreddit")
    val subreddit: String? = null, // AskReddit
    @SerialName("subreddit_id")
    val subredditId: String? = null, // t5_2qh1i
    @SerialName("subreddit_name_prefixed")
    val subredditNamePrefixed: String? = null, // r/AskReddit
    @SerialName("subreddit_subscribers")
    val subredditSubscribers: Int? = null, // 31460508
    @SerialName("subreddit_type")
    val subredditType: String? = null, // public
//    @SerialName("suggested_sort")
//    val suggestedSort: Any? = null, // null
    @SerialName("thumbnail")
    val thumbnail: String? = null, // self
//    @SerialName("thumbnail_height")
//    val thumbnailHeight: Any? = null, // null
//    @SerialName("thumbnail_width")
//    val thumbnailWidth: Any? = null, // null
    @SerialName("title")
    val title: String? = null, // There's a minimum age for certain political jobs. How would you feel if there was a maximum age limit?
//    @SerialName("top_awarded_type")
//    val topAwardedType: Any? = null, // null
    @SerialName("total_awards_received")
    val totalAwardsReceived: Int? = null, // 111
//    @SerialName("treatment_tags")
//    val treatmentTags: List<Any>? = null,
    @SerialName("ups")
    val ups: Int? = null, // 61791
    @SerialName("upvote_ratio")
    val upvoteRatio: Double? = null, // 0.8
    @SerialName("url")
    val url: String? = null, // https://www.reddit.com/r/AskReddit/comments/lmktus/theres_a_minimum_age_for_certain_political_jobs/
//    @SerialName("user_reports")
//    val userReports: List<Any>? = null,
    @SerialName("view_count")
    val viewCount: Int? = null, // null
    @SerialName("visited")
    val visited: Boolean? = null, // false
    @SerialName("whitelist_status")
    val whitelistStatus: String? = null, // all_ads
    @SerialName("wls")
    val wls: Int? = null // 6
)