package com.rainbow.remote.dto


import com.rainbow.remote.Item
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteWikiPage internal constructor(
    @SerialName("content_html")
    val contentHtml: String? = null, // &lt;!-- SC_OFF --&gt;&lt;div
    @SerialName("content_md")
    val contentMd: String? = null, // #Wiki Index###[Subreddit Rules](https://www.reddit.com/r/AmongUs/wiki/rules)
    @SerialName("may_revise")
    val mayRevise: Boolean? = null, // false
//    @SerialName("reason")
//    val reason: Any? = null, // null
    @SerialName("revision_by")
    internal val revisionBy: Item<RemoteUser>? = null,
    @SerialName("revision_date")
    val revisionDate: Int? = null, // 1617152334
    @SerialName("revision_id")
    val revisionId: String? = null // 43e22862-91bc-11eb-9f7d-0e37ef86ea9b
)

val RemoteWikiPage.revisionBy
    get() = revisionBy?.data