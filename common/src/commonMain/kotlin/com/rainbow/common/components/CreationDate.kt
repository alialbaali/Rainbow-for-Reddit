package com.rainbow.common.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.rainbow.common.utils.displayTime
import kotlinx.datetime.LocalDateTime

@Composable
fun CreationDate(date: LocalDateTime, modifier: Modifier = Modifier) {
    Text(
        date.displayTime,
        modifier,
        color = MaterialTheme.colorScheme.onBackground.copy(0.5F),
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    )
}