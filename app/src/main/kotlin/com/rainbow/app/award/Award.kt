package com.rainbow.app.award

import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.domain.models.Award
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

@Composable
fun AwardMenuItem(award: Award, onClick: () -> Unit, modifier: Modifier = Modifier) {
    DropdownMenuItem(
        onClick,
        modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        AwardImage(award, Modifier.size(24.dp))
        Spacer(Modifier.width(16.dp))
        Text(award.name, Modifier.weight(1F))
        Spacer(Modifier.width(16.dp))
        Text(award.count.toString())
    }
}

@Composable
fun AwardImage(award: Award, modifier: Modifier = Modifier) {
    val painterResource = lazyPainterResource(award.imageUrl)
    KamelImage(
        resource = painterResource,
        contentDescription = award.name,
        modifier = modifier,
        onFailure = {
            throw it
        }
    )
}
