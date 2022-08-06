package com.rainbow.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
        color = MaterialTheme.colorScheme.primary,
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
                    Modifier.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                else
                    Modifier
            )
            .clickable { onClick(userName) },
        color = if (isOP) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    )
}