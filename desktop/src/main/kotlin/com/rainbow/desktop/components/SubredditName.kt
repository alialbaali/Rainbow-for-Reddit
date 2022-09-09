package com.rainbow.desktop.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun SubredditName(
    subredditName: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = subredditName,
        modifier
            .clickable { onClick(subredditName) },
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    )
}