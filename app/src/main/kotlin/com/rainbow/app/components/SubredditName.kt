package com.rainbow.app.components

import androidx.compose.foundation.clickable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
inline fun SubredditName(
    subredditName: String,
    crossinline onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = subredditName,
        modifier
            .clickable { onClick(subredditName) },
        color = MaterialTheme.colors.secondary,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    )
}