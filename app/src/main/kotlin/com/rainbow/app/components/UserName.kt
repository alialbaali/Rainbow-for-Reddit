package com.rainbow.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
inline fun UserName(userName: String, crossinline onClick: (String) -> Unit, modifier: Modifier = Modifier) {
    Text(
        text = userName,
        modifier = modifier.clickable { onClick(userName) },
        color = MaterialTheme.colors.primary,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    )
}

@Composable
inline fun CommentUserName(
    userName: String,
    postUserName: String,
    crossinline onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isOP = userName == postUserName
    Text(
        text = userName,
        modifier = modifier
            .then(
                if (isOP)
                    Modifier.background(MaterialTheme.colors.primary, MaterialTheme.shapes.small)
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                else
                    Modifier
            )
            .clickable { onClick(userName) },
        color = if (isOP) MaterialTheme.colors.background else MaterialTheme.colors.primary,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    )
}