package com.rainbow.desktop.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.domain.models.Award
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

private val AwardSmallImageSize = 20.dp
private val AwardMediumImageSize = 50.dp
private val AwardLargeImageSize = 150.dp

@Composable
fun Awards(awards: List<Award>, modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    val count = remember(awards) { awards.sumOf { it.count } }
    var isMenuVisible by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = modifier
                .clip(MaterialTheme.shapes.small)
                .clickable { isMenuVisible = !isMenuVisible }
                .horizontalScroll(scrollState)
                .padding(horizontal = RainbowTheme.dimensions.small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.small),
        ) {
            Text(
                count.toString(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            awards.forEach { award ->
                AwardImage(
                    award,
                    modifier = Modifier.size(AwardSmallImageSize),
                )
            }
        }

        RainbowDropdownMenu(
            isMenuVisible,
            onDismissRequest = { isMenuVisible = false },
        ) {
            Text(
                text = RainbowStrings.Awards(count),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
            )
            awards.forEach { award ->
                AwardItem(award)
            }
        }
    }
}

@Composable
fun AwardItem(award: Award, modifier: Modifier = Modifier) {
    var isDialogVisible by remember { mutableStateOf(false) }
    RainbowDropdownMenuItem(
        onClick = {
            isDialogVisible = !isDialogVisible

        },
        modifier,
    ) {
        AwardImage(award, Modifier.size(AwardMediumImageSize))
        Column(Modifier.weight(1F)) {
            Text(
                award.name,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                award.description,
                color = MaterialTheme.colorScheme.surfaceVariant,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        Text(award.count.toString())
    }

    AwardDialog(
        award,
        isDialogVisible,
        onCloseRequest = { isDialogVisible = false }
    )
}

@Composable
fun AwardImage(award: Award, modifier: Modifier = Modifier) {
    val painterResource = lazyPainterResource(award.imageUrl)
    KamelImage(
        resource = painterResource,
        contentDescription = award.name,
        modifier = modifier,
    )
}

@Composable
fun AwardDialog(
    award: Award,
    visible: Boolean,
    onCloseRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val imageResource = lazyPainterResource(award.imageUrl)
    LayerDialog(onCloseRequest, "${award.name} (${award.count})", modifier, visible = visible) {
        KamelImage(imageResource, award.name, Modifier.size(AwardLargeImageSize))
        Text(
            award.description,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}