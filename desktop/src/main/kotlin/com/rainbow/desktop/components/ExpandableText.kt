package com.rainbow.desktop.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.rounded.CloseFullscreen
import androidx.compose.material.icons.rounded.OpenInFull
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import com.rainbow.desktop.ui.dimensions
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings

@Composable
fun ExpandableText(
    text: String,
    defaultMaxLines: Int,
    modifier: Modifier = Modifier
) {
    var isButtonVisible by remember { mutableStateOf(false) }
    var maxLines by remember(defaultMaxLines) { mutableStateOf(defaultMaxLines) }
    val isTextExpanded = remember(maxLines) { maxLines == Int.MAX_VALUE }
    val animationSpec = remember {
        spring<IntSize>(
//            dampingRatio = Spring.DampingRatioLowBouncy, // throws an exception.
            stiffness = Spring.StiffnessMediumLow,
        )
    }

    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.medium),
    ) {
        Text(
            text,
            modifier = Modifier.animateContentSize(animationSpec),
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { result -> if (!isTextExpanded) isButtonVisible = result.didOverflowHeight }
        )

        if (isButtonVisible) {
            RainbowButton(
                onClick = { maxLines = if (isTextExpanded) defaultMaxLines else Int.MAX_VALUE },
                modifier = Modifier.align(Alignment.End).animateContentSize(animationSpec),
            ) {
                val buttonText = remember(isTextExpanded) {
                    if (isTextExpanded)
                        RainbowStrings.Collapse
                    else
                        RainbowStrings.Expand
                }
                Icon(
                    imageVector = if (isTextExpanded) RainbowIcons.CloseFullscreen else RainbowIcons.OpenInFull,
                    contentDescription = buttonText
                )
                Text(buttonText)
            }
        }
    }
}