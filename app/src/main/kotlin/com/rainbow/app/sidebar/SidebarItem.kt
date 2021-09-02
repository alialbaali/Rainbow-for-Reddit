package com.rainbow.app.sidebar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.rainbow.app.sidebar.SidebarItem.*
import com.rainbow.app.ui.dpDimensions
import com.rainbow.app.utils.defaultPadding

enum class SidebarItem {
    Profile, Home, Subreddits, Messages, Settings;

    companion object {
        val Default = Home
    }
}

private inline val SidebarItem.icon: ImageBitmap
    @Composable
    get() = when (this) {
        Home -> imageResource("Icons/feather_home.png")
        Subreddits -> imageResource("Icons/feather_grid.png")
        Messages -> imageResource("Icons/feather_mail.png")
        Profile -> imageResource("Icons/feather_user.png")
        Settings -> imageResource("Icons/feather_settings.png")
    }

@Composable
fun SidebarItem(
    item: SidebarItem,
    isExpanded: Boolean,
    onClick: (SidebarItem) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.h5,
    iconColor: Color = MaterialTheme.colors.onBackground,
) {

    Row(
        modifier
            .clickable { onClick(item) }
            .defaultPadding()
            .layoutId(item.ordinal),
        Arrangement.spacedBy(MaterialTheme.dpDimensions.large),
        Alignment.CenterVertically,
    ) {

        Icon(
            item.icon,
            item.name,
            Modifier.size(24.dp),
            iconColor
        )

        if (isExpanded)
            Text(
                item.name,
                Modifier
                    .fillMaxWidth(),
                style = textStyle
            )

    }

}
