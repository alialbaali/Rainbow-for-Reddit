package com.rainbow.desktop.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.*
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.utils.defaultPadding
import com.rainbow.domain.models.Message

@Composable
fun MessageItem(
    message: Message,
    onNavigateMainScreen: (MainScreen) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
            .defaultPadding()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        DisableSelection {
            Row(verticalAlignment = Alignment.CenterVertically) {
                UserName(message.userName, { onNavigateMainScreen(MainScreen.User(it)) })
                message.subredditName?.let { subredditName ->
                    Dot()
                    SubredditName(subredditName, { onNavigateMainScreen(MainScreen.Subreddit(it)) })
                }
                Dot()
                CreationDate(message.creationDate)
            }
        }
        Text(
            message.subject,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        ExpandableText(message.body)
    }
}