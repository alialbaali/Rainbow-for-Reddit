package com.rainbow.app

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.rainbow.app.components.RainbowTopAppBar
import com.rainbow.app.sidebar.Sidebar
import com.rainbow.app.sidebar.SidebarItem

@Composable
fun Rainbow(modifier: Modifier = Modifier) {

    var sidebarItem by remember { mutableStateOf(SidebarItem.Settings) }

    var isExpanded by remember { mutableStateOf(true) }

    Column(Modifier.background(MaterialTheme.colors.background)) {

        RainbowTopAppBar(
            sidebarItem.name,
            onSidebarClick = { isExpanded = !isExpanded },
            Modifier.zIndex(1F),
        )

        Row(
            Modifier.fillMaxSize(),
        ) {

            Sidebar(
                sidebarItem,
                isExpanded,
                onClick = { sidebarItem = it },
                modifier = Modifier
                    .animateContentSize(spring(stiffness = Spring.StiffnessLow))
                    .wrapContentWidth(unbounded = true)
                    .fillMaxHeight(),
            )

            Content(sidebarItem)

        }
    }
}
