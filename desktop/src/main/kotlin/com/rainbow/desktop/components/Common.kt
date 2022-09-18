package com.rainbow.desktop.components

import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.OpenInBrowser
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings

@Composable
fun OpenInBrowserDropdownMenuItem(
    url: String,
    handler: DropdownMenuHandler,
    modifier: Modifier = Modifier,
) {
    val urlHandler = LocalUriHandler.current
    RainbowDropdownMenuItem(
        onClick = {
            urlHandler.openUri(url)
            handler.hideMenu()
        },
        modifier,
    ) {
        Icon(RainbowIcons.OpenInBrowser, RainbowStrings.OpenInBrowser)
        Text(RainbowStrings.OpenInBrowser)
    }
}

@Composable
fun CopyLinkDropdownMenuItem(
    url: String,
    handler: DropdownMenuHandler,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current
    RainbowDropdownMenuItem(
        onClick = {
            val annotatedString = AnnotatedString(url)
            clipboardManager.setText(annotatedString)
            handler.hideMenu()
            onShowSnackbar(RainbowStrings.LinkIsCopied)
        }
    ) {
        Icon(RainbowIcons.Link, RainbowStrings.CopyLink)
        Text(RainbowStrings.CopyLink)
    }
}