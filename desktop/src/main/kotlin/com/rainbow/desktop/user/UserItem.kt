package com.rainbow.desktop.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.ItemHeader
import com.rainbow.domain.models.User

@Composable
fun UserItem(
    user: User,
    onClick: (User) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick(user) }
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ItemHeader(user.bannerImageUrl.toString(), user.imageUrl.toString(), user.name)
        UserItemName(user.name, Modifier.padding(horizontal = 16.dp))
        user.description?.let {
            UserItemDescription(it)
        }
    }
}