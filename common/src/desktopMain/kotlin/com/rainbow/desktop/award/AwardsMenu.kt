package com.rainbow.desktop.award

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.common.award.AwardItem
import com.rainbow.common.award.ItemAwards
import com.rainbow.common.utils.defaultBackgroundShape
import com.rainbow.domain.models.Award

@Composable
fun AwardsMenu(awards: List<Award>, modifier: Modifier = Modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    var isDialogVisible by remember { mutableStateOf(false) }
    var selectedAward by remember { mutableStateOf(awards.firstOrNull()) }
    Column(modifier.defaultBackgroundShape(shape = CircleShape).padding(8.dp)) {
        ItemAwards(
            awards,
            {},
            modifier = Modifier.hoverable(interactionSource),
        )
        DropdownMenu(
            isHovered,
            onDismissRequest = {},
            focusable = false,
            modifier = Modifier
                .hoverable(interactionSource)
                .heightIn(max = 300.dp)
        ) {
            awards.forEach { award ->
                DropdownMenuItem(
                    onClick = {
                        selectedAward = award
                        isDialogVisible = true
                    },
                ) {
                    AwardItem(award)
                }
            }
        }
    }
    AnimatedVisibility(isDialogVisible) {
        selectedAward?.let { award ->
            AwardDialog(award, onCloseRequest = { isDialogVisible = false })
        }
    }
}