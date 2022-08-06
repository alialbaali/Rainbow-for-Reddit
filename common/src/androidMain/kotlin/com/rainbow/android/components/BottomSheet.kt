package com.rainbow.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.OpenInBrowser
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.common.award.AwardItem
import com.rainbow.common.post.PostActionsModel
import com.rainbow.common.settings.SettingsModel
import com.rainbow.common.utils.*
import com.rainbow.domain.models.*

@Composable
fun BottomSheet(title: String, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier.verticalScroll(rememberScrollState()).defaultPadding(), Arrangement.spacedBy(16.dp)) {
        Box(
            Modifier
                .fillMaxWidth(0.2f)
                .height(5.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        content()
    }
}

@Composable
inline fun ColumnScope.BottomSheetItem(
    text: String,
    imageVector: ImageVector?,
    crossinline onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .defaultPadding(),
        Arrangement.spacedBy(16.dp),
        Alignment.CenterVertically,
    ) {
        if (imageVector != null)
            Icon(imageVector, text)
        Text(text)
    }
}

@Composable
fun HomePostSortingBottomSheet(onSortingItemClick: (HomePostSorting) -> Unit, modifier: Modifier = Modifier) {
    BottomSheet(RainbowStrings.PostSorting, modifier) {
        HomePostSorting.values().forEach { sorting ->
            BottomSheetItem(sorting.text, sorting.icon, { onSortingItemClick(sorting) })
        }
    }
}

@Composable
fun UserPostSortingBottomSheet(onSortingItemClick: (UserPostSorting) -> Unit, modifier: Modifier = Modifier) {
    BottomSheet(RainbowStrings.PostSorting, modifier) {
        UserPostSorting.values().forEach { sorting ->
            BottomSheetItem(sorting.text, sorting.icon, { onSortingItemClick(sorting) })
        }
    }
}

@Composable
fun PostCommentSortingBottomSheet(onSortingItemClick: (PostCommentSorting) -> Unit, modifier: Modifier = Modifier) {
    BottomSheet(RainbowStrings.CommentSorting, modifier) {
        PostCommentSorting.values().forEach { sorting ->
            BottomSheetItem(sorting.text, sorting.icon, { onSortingItemClick(sorting) })
        }
    }
}

@Composable
fun SearchPostSortingBottomSheet(onSortingItemClick: (SearchPostSorting) -> Unit, modifier: Modifier = Modifier) {
    BottomSheet(RainbowStrings.PostSorting, modifier) {
        SearchPostSorting.values().forEach { sorting ->
            BottomSheetItem(sorting.text, sorting.icon, { onSortingItemClick(sorting) })
        }
    }
}

@Composable
fun SubredditPostSortingBottomSheet(onSortingItemClick: (SubredditPostSorting) -> Unit, modifier: Modifier = Modifier) {
    BottomSheet(RainbowStrings.PostSorting, modifier) {
        SubredditPostSorting.values().forEach { sorting ->
            BottomSheetItem(sorting.text, sorting.icon, { onSortingItemClick(sorting) })
        }
    }
}

@Composable
fun ThemeBottomSheet(modifier: Modifier = Modifier) {
    BottomSheet(RainbowStrings.Theme, modifier) {
        Theme.values().forEach { theme ->
            BottomSheetItem(theme.text, null, { SettingsModel.setTheme(theme) })
        }
    }
}

@Composable
fun PostMenuBottomSheet(
    post: Post,
    onPostUpdate: (Post) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    val clipboardManager = LocalClipboardManager.current
    BottomSheet(RainbowStrings.PostMenu, modifier) {
        BottomSheetItem(
            RainbowStrings.OpenInBrowser,
            RainbowIcons.OpenInBrowser,
            onClick = { uriHandler.openUri(post.url) }
        )

        BottomSheetItem(
            RainbowStrings.CopyLink,
            RainbowIcons.ContentCopy,
            onClick = {
                clipboardManager.setText(AnnotatedString(post.url))
                onShowSnackbar(RainbowStrings.LinkIsCopied)
            }
        )

        if (post.isHidden)
            BottomSheetItem(
                RainbowStrings.UnHide,
                RainbowIcons.Visibility,
                onClick = {
                    PostActionsModel.unHidePost(post, onPostUpdate)
                    onShowSnackbar(RainbowStrings.PostIsUnHidden)
                }
            )
        else
            BottomSheetItem(
                RainbowStrings.Hide,
                RainbowIcons.VisibilityOff,
                onClick = {
                    PostActionsModel.hidePost(post, onPostUpdate)
                    onShowSnackbar(RainbowStrings.PostIsHidden)
                }
            )
    }
}

@Composable
fun AwardsBottomSheet(postId: String, modifier: Modifier = Modifier) {
    BottomSheet(RainbowStrings.Awards, modifier) {
        val awards = findAwardsByPostId(postId)
        awards?.forEach { award ->
            Row {
                AwardItem(award, Modifier.fillMaxWidth())
            }
        }
    }
}