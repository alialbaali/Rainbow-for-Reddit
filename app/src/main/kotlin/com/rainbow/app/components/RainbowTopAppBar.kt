package com.rainbow.app.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rainbow.app.search.SearchTextField
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.defaultPadding

@Composable
fun RainbowTopAppBar(
    title: String,
    onSidebarClick: () -> Unit,
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .defaultPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onSidebarClick,
            ) {
                Icon(RainbowIcons.Menu, RainbowStrings.ShowMenu)
            }

            IconButton(
                onClick = onBackClick,
            ) {
                Icon(RainbowIcons.ArrowBack, RainbowStrings.ShowMenu)
            }

            IconButton(
                onClick = onForwardClick,
            ) {
                Icon(RainbowIcons.ArrowForward, RainbowStrings.ShowMenu)
            }

            Text(
                title,
                style = MaterialTheme.typography.h6
            )

//            SearchTextField()
        }
    }
}