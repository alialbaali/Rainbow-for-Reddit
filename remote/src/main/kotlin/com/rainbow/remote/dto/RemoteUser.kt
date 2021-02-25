package com.rainbow.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteUser(
    @SerialName("accept_chats")
    val acceptChats: Boolean? = null, // false
    @SerialName("accept_pms")
    val acceptPms: Boolean? = null, // true
    @SerialName("awardee_karma")
    val awardeeKarma: Int? = null, // 104
    @SerialName("awarder_karma")
    val awarderKarma: Int? = null, // 0
    @SerialName("can_create_subreddit")
    val canCreateSubreddit: Boolean? = null, // true
    @SerialName("can_edit_name")
    val canEditName: Boolean? = null, // false
    @SerialName("coins")
    val coins: Int? = null, // 0
    @SerialName("comment_karma")
    val commentKarma: Int? = null, // 377
    @SerialName("created")
    val created: Double? = null, // 1572355536.0
    @SerialName("created_utc")
    val createdUtc: Double? = null, // 1572326736.0
    @SerialName("force_password_reset")
    val forcePasswordReset: Boolean? = null, // false
    @SerialName("gold_creddits")
    val goldCreddits: Int? = null, // 0
//    @SerialName("gold_expiration")
//    val goldExpiration: Any? = null, // null
    @SerialName("has_android_subscription")
    val hasAndroidSubscription: Boolean? = null, // false
    @SerialName("has_external_account")
    val hasExternalAccount: Boolean? = null, // false
    @SerialName("has_gold_subscription")
    val hasGoldSubscription: Boolean? = null, // false
    @SerialName("has_ios_subscription")
    val hasIosSubscription: Boolean? = null, // false
    @SerialName("has_mail")
    val hasMail: Boolean? = null, // false
    @SerialName("has_mod_mail")
    val hasModMail: Boolean? = null, // false
    @SerialName("has_paypal_subscription")
    val hasPaypalSubscription: Boolean? = null, // false
    @SerialName("has_stripe_subscription")
    val hasStripeSubscription: Boolean? = null, // false
    @SerialName("has_subscribed")
    val hasSubscribed: Boolean? = null, // true
    @SerialName("has_subscribed_to_premium")
    val hasSubscribedToPremium: Boolean? = null, // false
    @SerialName("has_verified_email")
    val hasVerifiedEmail: Boolean? = null, // true
    @SerialName("has_visited_new_profile")
    val hasVisitedNewProfile: Boolean? = null, // false
    @SerialName("hide_from_robots")
    val hideFromRobots: Boolean? = null, // false
    @SerialName("icon_img")
    val iconImg: String? = null, // https://www.redditstatic.com/avatars/avatar_default_01_4856A3.png
    @SerialName("id")
    val id: String? = null, // 4wefty6g
    @SerialName("in_beta")
    val inBeta: Boolean? = null, // false
    @SerialName("in_chat")
    val inChat: Boolean? = null, // true
    @SerialName("in_redesign_beta")
    val inRedesignBeta: Boolean? = null, // true
    @SerialName("inbox_count")
    val inboxCount: Int? = null, // 0
    @SerialName("is_employee")
    val isEmployee: Boolean? = null, // false
    @SerialName("is_friend")
    val isFriend: Boolean? = null, // false
    @SerialName("is_gold")
    val isGold: Boolean? = null, // false
    @SerialName("is_mod")
    val isMod: Boolean? = null, // true
    @SerialName("is_sponsor")
    val isSponsor: Boolean? = null, // false
    @SerialName("is_suspended")
    val isSuspended: Boolean? = null, // false
    @SerialName("link_karma")
    val linkKarma: Int? = null, // 2339
//    @SerialName("modhash")
//    val modhash: Any? = null, // null
    @SerialName("name")
    val name: String? = null, // LoneWalker20
    @SerialName("new_modmail_exists")
    val newModmailExists: Boolean? = null, // false
    @SerialName("num_friends")
    val numFriends: Int? = null, // 0
    @SerialName("over_18")
    val over18: Boolean? = null, // true
    @SerialName("password_set")
    val passwordSet: Boolean? = null, // true
    @SerialName("pref_autoplay")
    val prefAutoplay: Boolean? = null, // true
    @SerialName("pref_clickgadget")
    val prefClickgadget: Int? = null, // 5
    @SerialName("pref_geopopular")
    val prefGeopopular: String? = null,
    @SerialName("pref_nightmode")
    val prefNightmode: Boolean? = null, // false
    @SerialName("pref_no_profanity")
    val prefNoProfanity: Boolean? = null, // true
    @SerialName("pref_show_presence")
    val prefShowPresence: Boolean? = null, // true
    @SerialName("pref_show_snoovatar")
    val prefShowSnoovatar: Boolean? = null, // false
    @SerialName("pref_show_trending")
    val prefShowTrending: Boolean? = null, // true
    @SerialName("pref_show_twitter")
    val prefShowTwitter: Boolean? = null, // false
    @SerialName("pref_top_karma_subreddits")
    val prefTopKarmaSubreddits: Boolean? = null, // true
    @SerialName("pref_video_autoplay")
    val prefVideoAutoplay: Boolean? = null, // true
    @SerialName("snoovatar_img")
    val snoovatarImg: String? = null,
//    @SerialName("snoovatar_size")
//    val snoovatarSize: Any? = null, // null
    @SerialName("subreddit")
    val subreddit: RemoteSubreddit? = null, // subreddit
//    @SerialName("suspension_expiration_utc")
//    val suspensionExpirationUtc: Any? = null, // null
    @SerialName("total_karma")
    val totalKarma: Int? = null, // 2820
    @SerialName("verified")
    val verified: Boolean? = null // true
) : Thing