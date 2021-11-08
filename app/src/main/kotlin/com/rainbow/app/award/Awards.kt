package com.rainbow.app.award

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.domain.models.Award

@Composable
fun Awards(awards: List<Award>) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.primaryVariant.copy(0.05F))
            .border(2.dp, MaterialTheme.colors.primaryVariant.copy(0.1F)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        awards
            .distinct()
            .onEach { award ->
                Award(
                    award,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(2.dp),
                )
            }
        Spacer(modifier = Modifier.width(4.dp))
        Text(awards.count().toString())
    }
}