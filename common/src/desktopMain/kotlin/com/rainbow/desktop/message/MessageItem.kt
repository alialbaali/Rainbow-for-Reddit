package com.rainbow.desktop.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.common.components.CreationDate
import com.rainbow.common.components.Dot
import com.rainbow.common.components.SubredditName
import com.rainbow.common.components.UserName
import com.rainbow.common.utils.defaultPadding
import com.rainbow.common.utils.defaultSurfaceShape
import com.rainbow.domain.models.Message

@Composable
inline fun MessageItem(
    message: Message,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .defaultSurfaceShape()
            .defaultPadding()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            UserName(message.userName, onUserNameClick)
            message.subredditName?.let { subredditName ->
                Dot()
                SubredditName(subredditName, onSubredditNameClick)
            }
            Dot()
            CreationDate(message.creationDate)
        }
        Text(message.subject, fontWeight = FontWeight.Medium, fontSize = 18.sp)
        Text(message.body)
    }
}