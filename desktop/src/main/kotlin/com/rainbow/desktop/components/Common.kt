package com.rainbow.desktop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.OpenInBrowser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.TextUnit
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.formatDisplayTime
import kotlinx.datetime.Instant

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

@Composable
fun SubredditName(
    subredditName: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = subredditName,
        modifier.clickable { onClick(subredditName) },
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.labelLarge,
    )
}

@Composable
fun UserName(
    userName: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = userName,
        modifier = modifier.clickable { onClick(userName) },
        color = MaterialTheme.colorScheme.surfaceVariant,
        style = MaterialTheme.typography.labelLarge,
    )
}


@Composable
fun CommentUserName(
    userName: String,
    isOP: Boolean,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
) {
    val color = if (isOP) MaterialTheme.colorScheme.primary else color
    Text(
        text = userName,
        modifier = modifier.clickable { onClick(userName) },
        color = color,
        style = MaterialTheme.typography.labelLarge,
    )
}

@Composable
fun CreationDate(date: Instant, modifier: Modifier = Modifier) {
    val displayTime = remember(date) { date.formatDisplayTime() }
    Text(
        displayTime,
        modifier,
        color = MaterialTheme.colorScheme.surfaceVariant,
        style = MaterialTheme.typography.labelLarge,
    )
}

@Composable
fun MarkdownText(text: String, modifier: Modifier = Modifier) {
    RichText(modifier) {
        Markdown(text)
    }
}

@Composable
fun NSFWBox(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxSize()
            .background(Color.Red)
    )
}

@Composable
fun TextBox(
    text: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.inverseOnSurface,
) {
    Box(modifier, Alignment.Center) {
        Text(
            text.first().toString().uppercase(),
            color = color,
            fontSize = fontSize,
        )
    }
}