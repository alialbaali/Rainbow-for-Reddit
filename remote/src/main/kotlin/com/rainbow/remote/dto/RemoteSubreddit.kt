package com.rainbow.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteSubreddit internal constructor(
    @SerialName("accounts_active")
    val accountsActive: Int? = null, // 1802
    @SerialName("accounts_active_is_fuzzed")
    val accountsActiveIsFuzzed: Boolean? = null, // false
    @SerialName("active_user_count")
    val activeUserCount: Int? = null, // 1802
    @SerialName("advertiser_category")
    val advertiserCategory: String? = null,
    @SerialName("all_original_content")
    val allOriginalContent: Boolean? = null, // false
    @SerialName("allow_chat_post_creation")
    val allowChatPostCreation: Boolean? = null, // false
    @SerialName("allow_discovery")
    val allowDiscovery: Boolean? = null, // true
    @SerialName("allow_galleries")
    val allowGalleries: Boolean? = null, // true
    @SerialName("allow_images")
    val allowImages: Boolean? = null, // true
    @SerialName("allow_polls")
    val allowPolls: Boolean? = null, // true
    @SerialName("allow_predictions")
    val allowPredictions: Boolean? = null, // false
    @SerialName("allow_predictions_tournament")
    val allowPredictionsTournament: Boolean? = null, // false
    @SerialName("allow_videogifs")
    val allowVideogifs: Boolean? = null, // true
    @SerialName("allow_videos")
    val allowVideos: Boolean? = null, // true
    @SerialName("banner_background_color")
    val bannerBackgroundColor: String? = null, // #373c3f
    @SerialName("banner_background_image")
    val bannerBackgroundImage: String? = null, // https://styles.redditmedia.com/t5_yknuq/styles/bannerBackgroundImage_biheh76esmv51.png?width=4000&amp;s=57f71bd91b5bba391977d165e96ba8f2da66e7a8
    @SerialName("banner_img")
    val bannerImg: String? = null,
//    @SerialName("banner_size")
//    val bannerSize: Any? = null, // null
    @SerialName("can_assign_link_flair")
    val canAssignLinkFlair: Boolean? = null, // true
    @SerialName("can_assign_user_flair")
    val canAssignUserFlair: Boolean? = null, // true
    @SerialName("collapse_deleted_comments")
    val collapseDeletedComments: Boolean? = null, // true
    @SerialName("comment_score_hide_mins")
    val commentScoreHideMins: Int? = null, // 0
    @SerialName("community_icon")
    val communityIcon: String? = null, // https://styles.redditmedia.com/t5_yknuq/styles/communityIcon_lnocce2womp51.png?width=256&amp;s=6ada2c527b1122875a0b346b5cf11ec23a24864d
    @SerialName("created")
    val created: Double? = null, // 1553307542.0
    @SerialName("created_utc")
    val createdUtc: Double? = null, // 1553278742.0
    @SerialName("description")
    val description: String? = null, // ## What is Among Us?Among Us is an online multiplayer social deduction game developed and published by American game studio Innersloth. It was released on iOS and Android devices in June 2018 and on Microsoft Windows in November 2018, featuring cross-platform play between these platforms. The game was also released on the Nintendo Switch in December 2020, and has planned releases for the Xbox One and Xbox Series X and Series S in 2021. The game takes place in a space-themed setting, in which players each take on one of two roles, most being Crewmates, and a predetermined number being Impostors. The goal of the Crewmates is to identify the Impostors, eliminate them, and complete tasks around the map; the Impostors' goal is to covertly sabotage and kill the Crewmates before they complete all of their tasks. Players suspected to be Impostors may be eliminated via a plurality vote, which any player may initiate by calling an emergency meeting (except during a crisis) or reporting a dead body. Crewmates win if all Impostors are eliminated or all tasks are completed whereas Impostors win if there is an equal number of Impostors and Crewmates, or if a critical sabotage goes unresolved.[Learn More](https://innersloth.com/gameAmongUs.php)**** Important Announcements* * [New updates to /r/AmongUs.](https://www.reddit.com/r/AmongUs/comments/kuj6au/updates_to_the_sub_strategy_saturday/)# Resources## [**Our Rules**](http://rules.ramong.us)## [**Frequently Asked Questions**](http://faq.ramong.us)## [_r/AmongUs Discord_](http://discord.ramong.us)## [_Related Subreddits_](http://related.ramong.us)## [**Moderator Feedback**](http://feedback.ramong.us/)
    @SerialName("description_html")
    val descriptionHtml: String? = null, // &lt;!-- SC_OFF --&gt;&lt;div class="md"&gt;&lt;h2&gt;What is Among Us?&lt;/h2&gt;&lt;p&gt;Among Us is an online multiplayer social deduction game developed and published by American game studio Innersloth. It was released on iOS and Android devices in June 2018 and on Microsoft Windows in November 2018, featuring cross-platform play between these platforms. The game was also released on the Nintendo Switch in December 2020, and has planned releases for the Xbox One and Xbox Series X and Series S in 2021. The game takes place in a space-themed setting, in which players each take on one of two roles, most being Crewmates, and a predetermined number being Impostors. The goal of the Crewmates is to identify the Impostors, eliminate them, and complete tasks around the map; the Impostors&amp;#39; goal is to covertly sabotage and kill the Crewmates before they complete all of their tasks. Players suspected to be Impostors may be eliminated via a plurality vote, which any player may initiate by calling an emergency meeting (except during a crisis) or reporting a dead body. Crewmates win if all Impostors are eliminated or all tasks are completed whereas Impostors win if there is an equal number of Impostors and Crewmates, or if a critical sabotage goes unresolved.&lt;/p&gt;&lt;p&gt;&lt;a href="https://innersloth.com/gameAmongUs.php"&gt;Learn More&lt;/a&gt;&lt;/p&gt;&lt;hr/&gt;&lt;ul&gt;&lt;li&gt;&lt;p&gt;Important Announcements&lt;/p&gt;&lt;/li&gt;&lt;li&gt;&lt;ul&gt;&lt;li&gt;&lt;a href="https://www.reddit.com/r/AmongUs/comments/kuj6au/updates_to_the_sub_strategy_saturday/"&gt;New updates to /r/AmongUs.&lt;/a&gt;&lt;/li&gt;&lt;/ul&gt;&lt;/li&gt;&lt;/ul&gt;&lt;h1&gt;Resources&lt;/h1&gt;&lt;h2&gt;&lt;a href="http://rules.ramong.us"&gt;&lt;strong&gt;Our Rules&lt;/strong&gt;&lt;/a&gt;&lt;/h2&gt;&lt;h2&gt;&lt;a href="http://faq.ramong.us"&gt;&lt;strong&gt;Frequently Asked Questions&lt;/strong&gt;&lt;/a&gt;&lt;/h2&gt;&lt;h2&gt;&lt;a href="http://discord.ramong.us"&gt;&lt;em&gt;r/AmongUs Discord&lt;/em&gt;&lt;/a&gt;&lt;/h2&gt;&lt;h2&gt;&lt;a href="http://related.ramong.us"&gt;&lt;em&gt;Related Subreddits&lt;/em&gt;&lt;/a&gt;&lt;/h2&gt;&lt;h2&gt;&lt;a href="http://feedback.ramong.us/"&gt;&lt;strong&gt;Moderator Feedback&lt;/strong&gt;&lt;/a&gt;&lt;/h2&gt;&lt;/div&gt;&lt;!-- SC_ON --&gt;
    @SerialName("disable_contributor_requests")
    val disableContributorRequests: Boolean? = null, // false
    @SerialName("display_name")
    val displayName: String, // AmongUs
    @SerialName("display_name_prefixed")
    val displayNamePrefixed: String? = null, // r/AmongUs
//    @SerialName("emojis_custom_size")
//    val emojisCustomSize: Any? = null, // null
    @SerialName("emojis_enabled")
    val emojisEnabled: Boolean? = null, // false
    @SerialName("free_form_reports")
    val freeFormReports: Boolean? = null, // true
    @SerialName("has_menu_widget")
    val hasMenuWidget: Boolean? = null, // true
    @SerialName("header_img")
    val headerImg: String? = null, // https://b.thumbs.redditmedia.com/JQgfgZVmHIlgdo2CxcGI8hSgFlFTJxXPYiXS2kUT7PY.png
    @SerialName("header_size")
    val headerSize: List<Int>? = null,
    @SerialName("header_title")
    val headerTitle: String? = null,
    @SerialName("hide_ads")
    val hideAds: Boolean? = null, // false
    @SerialName("icon_img")
    val iconImg: String? = null,
//    @SerialName("icon_size")
//    val iconSize: Any? = null, // null
    @SerialName("id")
    val id: String, // yknuq
    @SerialName("is_chat_post_feature_enabled")
    val isChatPostFeatureEnabled: Boolean? = null, // true
    @SerialName("is_crosspostable_subreddit")
    val isCrosspostableSubreddit: Boolean? = null, // true
//    @SerialName("is_enrolled_in_new_modmail")
//    val isEnrolledInNewModmail: Any? = null, // null
    @SerialName("key_color")
    val keyColor: String? = null, // #46d160
    @SerialName("lang")
    val lang: String? = null, // en
    @SerialName("link_flair_enabled")
    val linkFlairEnabled: Boolean? = null, // true
    @SerialName("link_flair_position")
    val linkFlairPosition: String? = null, // right
    @SerialName("mobile_banner_image")
    val mobileBannerImage: String? = null,
    @SerialName("name")
    val name: String? = null, // t5_yknuq
    @SerialName("notification_level")
    val notificationLevel: String? = null, // low
    @SerialName("original_content_tag_enabled")
    val originalContentTagEnabled: Boolean? = null, // false
    @SerialName("over18")
    val over18: Boolean? = null, // false
    @SerialName("prediction_leaderboard_entry_type")
    val predictionLeaderboardEntryType: String? = null, // IN_FEED
    @SerialName("primary_color")
    val primaryColor: String? = null, // #141414
    @SerialName("public_description")
    val publicDescription: String, // Unofficial subreddit for the game Among Us by Innersloth. Play online or over local WiFi with 4-10 players as a crewmate or impostor. Crewmates can win by completing all tasks or discovering and voting the impostor off the ship. The Impostor can use sabotage to cause chaos, making for easier kills and better alibis. Choose from 3 maps and 1-3 impostors to better suit your own playstyle!
    @SerialName("public_description_html")
    val publicDescriptionHtml: String? = null, // &lt;!-- SC_OFF --&gt;&lt;div class="md"&gt;&lt;p&gt;Unofficial subreddit for the game Among Us by Innersloth. Play online or over local WiFi with 4-10 players as a crewmate or impostor. Crewmates can win by completing all tasks or discovering and voting the impostor off the ship. The Impostor can use sabotage to cause chaos, making for easier kills and better alibis. Choose from 3 maps and 1-3 impostors to better suit your own playstyle!&lt;/p&gt;&lt;/div&gt;&lt;!-- SC_ON --&gt;
    @SerialName("public_traffic")
    val publicTraffic: Boolean? = null, // false
    @SerialName("quarantine")
    val quarantine: Boolean? = null, // false
    @SerialName("restrict_commenting")
    val restrictCommenting: Boolean? = null, // false
    @SerialName("restrict_posting")
    val restrictPosting: Boolean? = null, // true
    @SerialName("show_media")
    val showMedia: Boolean? = null, // true
    @SerialName("show_media_preview")
    val showMediaPreview: Boolean? = null, // true
    @SerialName("spoilers_enabled")
    val spoilersEnabled: Boolean? = null, // true
    @SerialName("submission_type")
    val submissionType: String? = null, // any
    @SerialName("submit_link_label")
    val submitLinkLabel: String? = null,
    @SerialName("submit_text")
    val submitText: String? = null,
//    @SerialName("submit_text_html")
//    val submitTextHtml: Any? = null, // null
    @SerialName("submit_text_label")
    val submitTextLabel: String? = null,
    @SerialName("subreddit_type")
    val subredditType: String? = null, // public
    @SerialName("subscribers")
    val subscribers: Int? = null, // 756958
    @SerialName("suggested_comment_sort")
    val suggestedCommentSort: String? = null, // top
    @SerialName("title")
    val title: String, // Subreddit for the game Among Us by Innersloth.
    @SerialName("url")
    val url: String? = null, // /r/AmongUs/
    @SerialName("user_can_flair_in_sr")
    val userCanFlairInSr: Boolean? = null, // true
    @SerialName("user_flair_background_color")
//    val userFlairBackgroundColor: Any? = null, // null
//    @SerialName("user_flair_css_class")
//    val userFlairCssClass: Any? = null, // null
//    @SerialName("user_flair_enabled_in_sr")
    val userFlairEnabledInSr: Boolean? = null, // true
    @SerialName("user_flair_position")
    val userFlairPosition: String? = null, // right
//    @SerialName("user_flair_richtext")
//    val userFlairRichtext: List<Any>? = null,
//    @SerialName("user_flair_template_id")
//    val userFlairTemplateId: Any? = null, // null
//    @SerialName("user_flair_text")
//    val userFlairText: Any? = null, // null
//    @SerialName("user_flair_text_color")
//    val userFlairTextColor: Any? = null, // null
    @SerialName("user_flair_type")
    val userFlairType: String? = null, // text
    @SerialName("user_has_favorited")
    val userHasFavorited: Boolean? = null, // false
    @SerialName("user_is_banned")
    val userIsBanned: Boolean? = null, // false
    @SerialName("user_is_contributor")
    val userIsContributor: Boolean? = null, // false
    @SerialName("user_is_moderator")
    val userIsModerator: Boolean? = null, // false
    @SerialName("user_is_muted")
    val userIsMuted: Boolean? = null, // false
    @SerialName("user_is_subscriber")
    val userIsSubscriber: Boolean? = null, // true
    @SerialName("user_sr_flair_enabled")
    val userSrFlairEnabled: Boolean? = null, // true
    @SerialName("user_sr_theme_enabled")
    val userSrThemeEnabled: Boolean? = null, // true
    @SerialName("videostream_links_count")
    val videostreamLinksCount: Int? = null, // 9
    @SerialName("whitelist_status")
    val whitelistStatus: String? = null, // all_ads
    @SerialName("wiki_enabled")
    val wikiEnabled: Boolean? = null, // true
    @SerialName("wls")
    val wls: Int? = null // 6
)