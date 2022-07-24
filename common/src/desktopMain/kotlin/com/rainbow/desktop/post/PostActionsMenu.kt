package com.rainbow.desktop.post

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.OpenInBrowser
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import com.rainbow.common.components.MenuIconButton
import com.rainbow.common.post.PostActionsModel
import com.rainbow.desktop.components.RainbowMenu
import com.rainbow.desktop.components.RainbowMenuItem
import com.rainbow.common.utils.RainbowIcons
import com.rainbow.common.utils.RainbowStrings
import com.rainbow.domain.models.Post

@Composable
inline fun PostActionsMenu(
    post: Post,
    noinline onUpdate: (Post) -> Unit,
    crossinline onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    val clipboardManager = LocalClipboardManager.current

    Column(modifier) {
        MenuIconButton(onClick = { isMenuExpanded = true })

        RainbowMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false },
        ) {
            RainbowMenuItem(
                RainbowStrings.OpenInBrowser,
                RainbowIcons.OpenInBrowser,
                onClick = {
                    uriHandler.openUri(post.url)
                    isMenuExpanded = false
                }
            )

            RainbowMenuItem(
                RainbowStrings.CopyLink,
                RainbowIcons.ContentCopy,
                onClick = {
                    clipboardManager.setText(AnnotatedString(post.url))
                    isMenuExpanded = false
                    onShowSnackbar(RainbowStrings.LinkIsCopied)
                }
            )

            if (post.isHidden)
                RainbowMenuItem(
                    RainbowStrings.UnHide,
                    RainbowIcons.Visibility,
                    onClick = {
                        PostActionsModel.unHidePost(post, onUpdate)
                        isMenuExpanded = false
                        onShowSnackbar(RainbowStrings.PostIsUnHidden)
                    }
                )
            else
                RainbowMenuItem(
                    RainbowStrings.Hide,
                    RainbowIcons.VisibilityOff,
                    onClick = {
                        PostActionsModel.hidePost(post, onUpdate)
                        isMenuExpanded = false
                        onShowSnackbar(RainbowStrings.PostIsHidden)
                    }
                )
        }
    }
}