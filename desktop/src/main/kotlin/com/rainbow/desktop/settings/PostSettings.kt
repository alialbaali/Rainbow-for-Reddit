package com.rainbow.desktop.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.components.EnumDropdownMenuHolder

@Composable
fun PostSettings(modifier: Modifier = Modifier) {
    SettingsTabContent(modifier) {
        PostFullHeightOption()
        MarkPostAsReadOption()
        PostLayoutOption()
        PostSortingOptions()
    }
}

@Composable
private fun PostLayoutOption(modifier: Modifier = Modifier) {
    val postLayout by SettingsStateHolder.postLayout.collectAsState()
    SettingsOption(RainbowStrings.PostLayout, modifier) {
        EnumDropdownMenuHolder(
            postLayout,
            SettingsStateHolder::setPostLayout,
            containerColor = MaterialTheme.colorScheme.background,
        )
    }
}


@Composable
private fun PostFullHeightOption(modifier: Modifier = Modifier) {
    val isPostFulLHeight by SettingsStateHolder.isPostFullHeight.collectAsState()
    SettingsOption(RainbowStrings.PostFullHeight, modifier) {
        Switch(
            isPostFulLHeight,
            onCheckedChange = { isPostFullHeight -> SettingsStateHolder.setIsPostFullHeight(isPostFullHeight) },
        )
    }
}

@Composable
private fun MarkPostAsReadOption(modifier: Modifier = Modifier) {
    val markPostAsRead by SettingsStateHolder.markPostAsRead.collectAsState()
    SettingsOption(RainbowStrings.MarkPostAsRead, modifier) {
        EnumDropdownMenuHolder(
            markPostAsRead,
            SettingsStateHolder::setMarkPostAsRead,
            containerColor = MaterialTheme.colorScheme.background
        )
    }
}


@Composable
private fun PostSortingOptions(modifier: Modifier = Modifier) {
    val homePostSorting by SettingsStateHolder.homePostSorting.collectAsState()
    val profilePostSorting by SettingsStateHolder.profilePostSorting.collectAsState()
    val userPostSorting by SettingsStateHolder.userPostSorting.collectAsState()
    val subredditPostSorting by SettingsStateHolder.subredditPostSorting.collectAsState()
    val searchPostSorting by SettingsStateHolder.searchPostSorting.collectAsState()
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PostSortingOption(
            homePostSorting,
            SettingsStateHolder::setHomePostSorting,
            RainbowStrings.DefaultHomePostSorting,
        )
        PostSortingOption(
            profilePostSorting,
            SettingsStateHolder::setProfilePostSorting,
            RainbowStrings.DefaultProfilePostSorting,
        )
        PostSortingOption(
            userPostSorting,
            SettingsStateHolder::setUserPostSorting,
            RainbowStrings.DefaultUserPostSorting,
        )
        PostSortingOption(
            subredditPostSorting,
            SettingsStateHolder::setSubredditPostSorting,
            RainbowStrings.DefaultSubredditPostSorting,
        )
        PostSortingOption(
            searchPostSorting,
            SettingsStateHolder::setSearchPostSorting,
            RainbowStrings.DefaultSearchPostSorting,
        )
    }
}

@Composable
private inline fun <reified T : Enum<T>> PostSortingOption(
    value: T,
    crossinline onValueUpdate: (T) -> Unit,
    name: String,
    modifier: Modifier = Modifier,
) {
    SettingsOption(name, modifier) {
        EnumDropdownMenuHolder(
            value,
            onValueUpdate,
            containerColor = MaterialTheme.colorScheme.background,
        )
    }
}