package com.rainbow.desktop.user

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
fun UserItemName(userName: String, modifier: Modifier = Modifier) {
    Text(
        text = userName,
        modifier = modifier,
        fontSize = 26.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun UserItemDescription(userDescription: String, modifier: Modifier = Modifier) {
    Text(
        userDescription,
        modifier,
        overflow = TextOverflow.Ellipsis,
        maxLines = 3
    )
}