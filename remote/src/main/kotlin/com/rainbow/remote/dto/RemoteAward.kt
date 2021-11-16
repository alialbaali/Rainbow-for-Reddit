package com.rainbow.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteAward(
    @SerialName("award_sub_type")
    val awardSubType: String? = null, // GLOBAL
    @SerialName("award_type")
    val awardType: String? = null, // global
//    @SerialName("awardings_required_to_grant_benefits")
//    val awardingsRequiredToGrantBenefits: Any? = null, // null
    @SerialName("coin_price")
    val coinPrice: Int? = null, // 100
    @SerialName("coin_reward")
    val coinReward: Int? = null, // 0
    @SerialName("count")
    val count: Int? = null, // 10
    @SerialName("days_of_drip_extension")
    val daysOfDripExtension: Int? = null, // 0
    @SerialName("days_of_premium")
    val daysOfPremium: Int? = null, // 0
    @SerialName("description")
    val description: String? = null, // Shows the Silver Award... and that's it.
//    @SerialName("end_date")
//    val endDate: Any? = null, // null
    @SerialName("giver_coin_reward")
    val giverCoinReward: Int? = null, // null
    @SerialName("icon_format")
    val iconFormat: String? = null, // null
    @SerialName("icon_height")
    val iconHeight: Int? = null, // 512
    @SerialName("icon_url")
    val iconUrl: String? = null, // https://www.redditstatic.com/gold/awards/icon/silver_512.png
    @SerialName("icon_width")
    val iconWidth: Int? = null, // 512
    @SerialName("id")
    val id: String? = null, // gid_1
    @SerialName("is_enabled")
    val isEnabled: Boolean? = null, // true
    @SerialName("is_new")
    val isNew: Boolean? = null, // false
    @SerialName("name")
    val name: String? = null, // Silver
    @SerialName("penny_donate")
    val pennyDonate: Int? = null, // null
    @SerialName("penny_price")
    val pennyPrice: Int? = null, // null
//    @SerialName("resized_icons")
//    val resizedIcons: List<Any>? = null,
//    @SerialName("resized_static_icons")
//    val resizedStaticIcons: List<Any>? = null,
//    @SerialName("start_date")
//    val startDate: Any? = null, // null
    @SerialName("static_icon_height")
    val staticIconHeight: Int? = null, // 512
    @SerialName("static_icon_url")
    val staticIconUrl: String? = null, // https://www.redditstatic.com/gold/awards/icon/silver_512.png
    @SerialName("static_icon_width")
    val staticIconWidth: Int? = null, // 512
    @SerialName("subreddit_coin_reward")
    val subredditCoinReward: Int? = null, // 0
//    @SerialName("subreddit_id")
//    val subredditId: Any? = null, // null
//    @SerialName("tiers_by_required_awardings")
//    val tiersByRequiredAwardings: Any? = null // null
)