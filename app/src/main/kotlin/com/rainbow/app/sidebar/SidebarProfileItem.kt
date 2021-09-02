package com.rainbow.app.sidebar


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rainbow.app.profile.ProfileViewModel
import com.rainbow.app.ui.dpDimensions
import com.rainbow.app.utils.*
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

@Composable
fun SidebarProfileItem(
    onClick: () -> Unit,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
) {

    val (state) = rememberViewModel(ProfileViewModel())

    state.currentUser.composed { user ->

        Row(
            modifier = modifier
                .clickable(onClick = onClick)
                .padding(MaterialTheme.dpDimensions.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dpDimensions.medium),
        ) {


            KamelImage(
                resource = lazyPainterResource(user.imageUrl!!),
                contentDescription = null,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .size(25.dp),
                onFailure = {
                    Icon(RainbowIcons.Person, contentDescription = RainbowStrings.Me)
                },
                onLoading = {
                    Icon(RainbowIcons.Person, contentDescription = RainbowStrings.Me)
                },
            )
//
            if (isExpanded)
                Text(
                    user.name,
                    Modifier
                )

        }

    }
}