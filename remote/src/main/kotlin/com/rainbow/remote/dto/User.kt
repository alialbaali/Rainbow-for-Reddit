package com.rainbow.remote.dto

import kotlinx.serialization.SerialName

data class Data(
    @SerialName("is_employee")
    val isEmployee: Boolean,

    @SerialName("has_visited_new_profile")
    val hasVisitedNewProfile: Boolean,

    @SerialName("is_friend")
    val isFriend: Boolean,

    @SerialName("pref_no_profanity")
    val prefNoProfanity: Boolean,

    @SerialName("has_external_account")
    val hasExternalAccount: Boolean,

    @SerialName("pref_geopopular")
    val prefGeopopular: String,

    @SerialName("pref_show_trending")
    val prefShowTrending: Boolean,

    @SerialName("pref_show_presence")
    val prefShowPresence: Boolean,

    @SerialName("snoovatar_img")
    val snoovatarImg: String,

    @SerialName("snoovatar_size")
    val snoovatarSize: Any? = null,

    @SerialName("gold_expiration")
    val goldExpiration: Any? = null,

    @SerialName("has_gold_subscription")
    val hasGoldSubscription: Boolean,

    @SerialName("is_sponsor")
    val isSponsor: Boolean,

    @SerialName("num_friends")
    val numFriends: Long,

    @SerialName("can_edit_name")
    val canEditName: Boolean,

    val verified: Boolean,

    @SerialName("new_modmail_exists")
    val newModmailExists: Any? = null,

    @SerialName("pref_autoplay")
    val prefAutoplay: Boolean,

    val coins: Long,

    @SerialName("has_paypal_subscription")
    val hasPaypalSubscription: Boolean,

    @SerialName("has_subscribed_to_premium")
    val hasSubscribedToPremium: Boolean,

    val id: String,

    @SerialName("has_stripe_subscription")
    val hasStripeSubscription: Boolean,

    @SerialName("can_create_subreddit")
    val canCreateSubreddit: Boolean,

    @SerialName("over_18")
    val over18: Boolean,

    @SerialName("is_gold")
    val isGold: Boolean,

    @SerialName("is_mod")
    val isMod: Boolean,

    @SerialName("awarder_karma")
    val awarderKarma: Long,

    @SerialName("suspension_expiration_utc")
    val suspensionExpirationUTC: Any? = null,

    @SerialName("has_verified_email")
    val hasVerifiedEmail: Boolean,

    @SerialName("is_suspended")
    val isSuspended: Boolean,

    @SerialName("pref_video_autoplay")
    val prefVideoAutoplay: Boolean,

    @SerialName("has_android_subscription")
    val hasAndroidSubscription: Boolean,

    @SerialName("in_redesign_beta")
    val inRedesignBeta: Boolean,

    @SerialName("icon_img")
    val iconImg: String,

    @SerialName("has_mod_mail")
    val hasModMail: Boolean,

    @SerialName("pref_nightmode")
    val prefNightmode: Boolean,

    @SerialName("awardee_karma")
    val awardeeKarma: Long,

    @SerialName("hide_from_robots")
    val hideFromRobots: Boolean,

    @SerialName("password_set")
    val passwordSet: Boolean,

    val modhash: Any? = null,

    @SerialName("link_karma")
    val linkKarma: Long,

    @SerialName("force_password_reset")
    val forcePasswordReset: Boolean,

    @SerialName("total_karma")
    val totalKarma: Long,

    @SerialName("inbox_count")
    val inboxCount: Long,

    @SerialName("pref_top_karma_subreddits")
    val prefTopKarmaSubreddits: Boolean,

    @SerialName("has_mail")
    val hasMail: Boolean,

    @SerialName("pref_show_snoovatar")
    val prefShowSnoovatar: Boolean,

    val name: String,

    @SerialName("pref_clickgadget")
    val prefClickgadget: Long,

    val created: Double,

    @SerialName("gold_creddits")
    val goldCreddits: Long,

    @SerialName("created_utc")
    val createdUTC: Double,

    @SerialName("has_ios_subscription")
    val hasIosSubscription: Boolean,

    @SerialName("pref_show_twitter")
    val prefShowTwitter: Boolean,

    @SerialName("in_beta")
    val inBeta: Boolean,

    @SerialName("comment_karma")
    val commentKarma: Long,

    @SerialName("has_subscribed")
    val hasSubscribed: Boolean
)