package com.rainbow.app.award

import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.domain.models.Award

@Composable
fun Awards(awards: List<Award>) {
    val interactionSource = MutableInteractionSource()
    val isHovered by interactionSource.collectIsHoveredAsState()
    Column(Modifier.wrapContentSize()) {
        Row(
            modifier = Modifier
                .hoverable(interactionSource),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            awards.onEach { award ->
                Award(
                    award,
                    modifier = Modifier.size(20.dp),
                )
            }
            Text(awards.count().toString())
        }
        DropdownMenu(isHovered, onDismissRequest = {}, focusable = false) {
            awards.forEach { award ->
                DropdownMenuItem(
                    onClick = {},
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                ) {
                    Award(award, Modifier.size(24.dp))
                    Spacer(Modifier.width(16.dp))
                    Text(award.name)
                    Spacer(Modifier.width(16.dp))
                    Text(award.count.toString())
                }
            }
        }
    }
}