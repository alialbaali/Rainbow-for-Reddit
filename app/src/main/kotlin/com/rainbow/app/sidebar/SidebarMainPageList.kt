package com.rainbow.app.sidebar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import com.rainbow.app.ui.dimensions
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.domain.models.MainPage


@Composable
fun SidebarMainPageList(
    onClick: (MainPage) -> Unit,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    itemModifier: (MainPage) -> Modifier = { Modifier },
) {
    Column(modifier) {

        MainPage.values().forEach { page ->
            MainPageItem(
                page = page,
                onClick = onClick,
                modifier = itemModifier(page)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                isExpanded = isExpanded
            )
        }
    }

}

@Composable
private fun MainPageItem(
    page: MainPage,
    onClick: (MainPage) -> Unit,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier
            .clickable { onClick(page) }
            .padding(MaterialTheme.dimensions.medium)
            .layoutId(page.ordinal),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.medium),
    ) {

        Icon(
            imageVector = page.imageVector,
            tint = Color.Red,
            contentDescription = page.name,
        )
        if (isExpanded)
            Text(
                text = page.toString(),
                style = MaterialTheme.typography.subtitle2
            )

    }
}

private val MainPage.imageVector
    get() = when (this) {
        MainPage.Best -> RainbowIcons.StarBorder
        MainPage.Hot -> RainbowIcons.LocalFireDepartment
        MainPage.Top -> RainbowIcons.BarChart
        MainPage.New -> RainbowIcons.FiberNew
        MainPage.Rising -> RainbowIcons.TrendingUp
        MainPage.Controversial -> RainbowIcons.TrendingDown
        else -> RainbowIcons.Person
    }

