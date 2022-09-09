package com.rainbow.desktop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UserName(userName: String, onClick: (String) -> Unit, modifier: Modifier = Modifier) {
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
    postUserName: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isOP = userName == postUserName
    Text(
        text = userName,
        modifier = modifier
            .then(
                if (isOP)
                    Modifier.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp)
                else
                    Modifier
            )
            .clickable { onClick(userName) },
        color = if (isOP) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.labelLarge,
    )
}