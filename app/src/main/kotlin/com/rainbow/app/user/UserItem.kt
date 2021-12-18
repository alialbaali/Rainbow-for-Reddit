package com.rainbow.app.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.app.components.HeaderItem
import com.rainbow.app.utils.defaultSurfaceShape
import com.rainbow.domain.models.User

@Composable
fun UserItem(
    user: User,
    onClick: (User) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.defaultSurfaceShape()
            .clickable { onClick(user) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        HeaderItem(user.bannerImageUrl.toString(), user.imageUrl.toString(), user.name)
        UserItemName(user.name, Modifier.padding(horizontal = 16.dp))
    }
}