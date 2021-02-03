package com.rainbow.app

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.app.sidebar.Sidebar
import com.rainbow.app.sidebar.SidebarState
import com.rainbow.app.ui.RainbowTheme
import com.rainbow.app.utils.defaultPadding
import com.rainbow.domain.models.MainPage

@Composable
fun Rainbow() = RainbowTheme {
    Row {
        var sidebarState by remember { mutableStateOf<SidebarState>(SidebarState.Page(MainPage.Default)) }
        var isExpanded by remember { mutableStateOf(true) }

        Sidebar(
            sidebarState,
            isExpanded,
            onClick = { sidebarState = it },
            modifier = Modifier
                .animateContentSize(spring(stiffness = Spring.StiffnessLow))
                .width(if (isExpanded) 300.dp else 74.dp)
                .fillMaxHeight()
                .defaultPadding(),
        )

        Content(
            sidebarState,
            onSidebarClick = { isExpanded = !isExpanded },
            modifier = Modifier
        )

    }
}

