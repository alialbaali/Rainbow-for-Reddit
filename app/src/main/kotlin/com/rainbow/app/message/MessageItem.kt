package com.rainbow.app.message

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.app.components.UserName
import com.rainbow.app.utils.defaultPadding
import com.rainbow.app.utils.defaultSurfaceShape
import com.rainbow.domain.models.Message

@Composable
inline fun MessageItem(
    message: Message,
    crossinline onClick: (Message) -> Unit,
    crossinline onUserNameClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .defaultSurfaceShape()
            .clickable { onClick(message) }
            .defaultPadding()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        UserName(message.userName, onUserNameClick)
        Text(message.subject, fontWeight = FontWeight.Medium, fontSize = 18.sp)
        Text(message.body)
    }
}