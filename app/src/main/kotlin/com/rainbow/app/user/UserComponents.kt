package com.rainbow.app.user

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun UserItemName(userName: String, modifier: Modifier = Modifier) {
    Text(
        text = userName,
        modifier = modifier,
        fontSize = 26.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colors.onBackground
    )
}