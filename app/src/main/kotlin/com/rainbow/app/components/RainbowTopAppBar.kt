package com.rainbow.app.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.PostAdd
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.app.search.Search
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.defaultPadding


@Composable
fun RainbowTopAppBar(
    title: String,
    onSidebarClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Surface(elevation = 2.dp) {

        Row(
            modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {

            val contentModifier = Modifier
                .wrapContentHeight()

            Row(verticalAlignment = Alignment.CenterVertically) {

                IconButton(
                    onClick = onSidebarClick,
                    modifier = contentModifier
                        .defaultPadding()
                ) {
                    Icon(RainbowIcons.Menu, RainbowStrings.ShowMenu)
                }

                Text(
                    title,
                    modifier = contentModifier
                        .defaultPadding(),
                    style = MaterialTheme.typography.h6
                )

            }

//            Search(
//                modifier = contentModifier
//                    .fillMaxWidth(0.5F)
//            )

            Button(
                onClick = {},
                modifier = Modifier
                    .wrapContentSize()
                    .defaultPadding(),
            ) {
                Icon(RainbowIcons.PostAdd, RainbowStrings.CreatePost)

                Spacer(Modifier.width(8.dp))

                Text(RainbowStrings.CreatePost)
            }

        }

    }

}