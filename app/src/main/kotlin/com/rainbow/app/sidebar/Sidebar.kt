package com.rainbow.app.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.rainbow.app.ui.dimensions
import com.rainbow.domain.models.MainPage

sealed class SidebarState {
    object Profile : SidebarState()
    data class Page(val page: MainPage) : SidebarState()
    data class Subreddit(val subredditName: String) : SidebarState()
}

@Composable
fun Sidebar(
    sidebarState: SidebarState,
    isExpanded: Boolean,
    onClick: (SidebarState) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.large),
    ) {

        val backgroundColor: (Boolean) -> Color = { stateMatch ->
            if (stateMatch)
                Color.Red.copy(0.1F)
            else
                Color.Unspecified
        }

        val layoutModifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()


        val itemModifier = Modifier
            .clip(MaterialTheme.shapes.medium)

        SidebarProfileItem(
            onClick = { onClick(SidebarState.Profile) },
            isExpanded = isExpanded,
            modifier = layoutModifier then itemModifier
                .background(backgroundColor(sidebarState == SidebarState.Profile)),
        )

        SidebarMainPageList(
            onClick = { onClick(SidebarState.Page(it)) },
            isExpanded = isExpanded,
            modifier = layoutModifier,
            itemModifier = {
                val page = (sidebarState as? SidebarState.Page)?.page

                itemModifier
                    .background(backgroundColor(page == it))
            }
        )

        SidebarSubredditList(
            onClick = { onClick(SidebarState.Subreddit(it)) },
            isExpanded = isExpanded,
            modifier = layoutModifier,
            itemModifier = {
                val subredditName = (sidebarState as? SidebarState.Subreddit)?.subredditName

                itemModifier
                    .background(backgroundColor(subredditName == it.name))
            }
        )

    }
}

