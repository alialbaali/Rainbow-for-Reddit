package com.rainbow.desktop.subreddit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.desktop.components.ItemHeader
import com.rainbow.desktop.utils.*
import com.rainbow.domain.models.Subreddit

@Composable
fun SearchSubredditItem(
    subreddit: Subreddit,
    onSubredditUpdate: (Subreddit) -> Unit,
    onClick: (Subreddit) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.clickable { onClick(subreddit) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ItemHeader(subreddit.bannerImageUrl.toString(), subreddit.imageUrl.toString(), subreddit.name)
        SubredditItemName(subreddit.name, Modifier.padding(horizontal = 16.dp))
        SubredditItemDescription(subreddit.shortDescription, Modifier.padding(horizontal = 16.dp))
        SubredditInfoItems(subreddit, Modifier.padding(horizontal = 16.dp))
        SubredditItemOptions(
            subreddit,
            onShowSnackbar,
            Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )
    }
}

@Composable
private fun SubredditItemDescription(subredditDescription: String, modifier: Modifier = Modifier) {
    Text(
        subredditDescription,
        modifier,
        overflow = TextOverflow.Ellipsis,
        maxLines = 3
    )
}

@Composable
private fun SubredditItemOptions(
    subreddit: Subreddit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SubscribeButton(subreddit, onShowSnackbar)
//        Column {
//            MenuIconButton(onClick = { isMenuExpanded = true })
//            RainbowMenu(isMenuExpanded, onDismissRequest = { isMenuExpanded = false }) {
//                RainbowMenuItem(
//                    RainbowStrings.OpenInBrowser,
//                    RainbowIcons.OpenInBrowser,
//                    onclick = {
//                        uriHandler.openUri(subreddit.fullUrl)
//                        isMenuExpanded = false
//                    }
//                )
//                RainbowMenuItem(
//                    RainbowStrings.CreatePost,
//                    RainbowIcons.PostAdd,
//                    onclick = { onShowSnackbar(RainbowStrings.Todo) },
//                )
//            }
//        }
    }
}

@Composable
private fun SubredditInfoItems(subreddit: Subreddit, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        subreddit.infoItems.forEach { item ->
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(item.imageVector, item.key, Modifier.size(20.dp), tint = Color.Gray)
                    Text(item.key, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
                }
                Text(item.value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
            }
        }
    }
}


private data class SubredditInfoItem(val key: String, val value: String, val imageVector: ImageVector)

private val Subreddit.infoItems
    get() = listOf(
        SubredditInfoItem(
            RainbowStrings.Subscribers,
            subscribersCount.format(),
            RainbowIcons.People
        ),
        SubredditInfoItem(
            RainbowStrings.ActiveSubscribers,
            activeSubscribersCount.format(),
            RainbowIcons.EmojiPeople
        ),
        SubredditInfoItem(
            RainbowStrings.Crosspost,
            RainbowStrings.Enabled,
            RainbowIcons.PostAdd
        ),
        SubredditInfoItem(
            RainbowStrings.Created,
            creationDate.displayTime,
            RainbowIcons.CalendarToday
        ),
        SubredditInfoItem(
            RainbowStrings.Type,
            type.name,
            RainbowIcons.Public
        ),
        SubredditInfoItem(
            RainbowStrings.NSFW,
            isNSFW.toString().replaceFirstChar { it.uppercase() },
            RainbowIcons.HPlusMobiledata
        ),
        SubredditInfoItem(
            RainbowStrings.Language,
            RainbowStrings.English,
            RainbowIcons.Language
        ),
    )