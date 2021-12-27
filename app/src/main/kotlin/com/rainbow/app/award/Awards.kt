package com.rainbow.app.award

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.app.utils.defaultBackgroundShape
import com.rainbow.domain.models.Award

@Composable
fun Awards(awards: List<Award>, modifier: Modifier = Modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    var isDialogVisible by remember { mutableStateOf(false) }
    var selectedAward by remember { mutableStateOf(awards.firstOrNull()) }
    Column(modifier.defaultBackgroundShape(shape = CircleShape).padding(8.dp)) {
        Row(
            modifier = Modifier
                .hoverable(interactionSource),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            awards.forEach { award ->
                AwardImage(
                    award,
                    modifier = Modifier.size(20.dp),
                )
            }
            Text(awards.sumOf { it.count }.toString())
        }
        DropdownMenu(
            isHovered,
            onDismissRequest = {},
            focusable = false,
            modifier = Modifier
                .hoverable(interactionSource)
                .heightIn(max = 300.dp)
        ) {
            awards.forEach { award ->
                AwardMenuItem(
                    award,
                    onClick = {
                        selectedAward = award
                        isDialogVisible = true
                    },
                )
            }
        }
    }
    AnimatedVisibility(isDialogVisible) {
        selectedAward?.let { award ->
            AwardDialog(award, onCloseRequest = { isDialogVisible = false })
        }
    }
}