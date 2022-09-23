package com.rainbow.desktop.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.EnumDropdownMenuHolder
import com.rainbow.desktop.utils.RainbowStrings

@Composable
fun PostSettings(modifier: Modifier = Modifier) {
    SettingsTabContent(modifier) {
        MarkPostAsReadOption()
        PostLayoutOption()
        PostSortingOptions()
    }
}

@Composable
private fun PostLayoutOption(modifier: Modifier = Modifier) {
    val postLayout by SettingsStateHolder.Instance.postLayout.collectAsState()
    SettingsOption(RainbowStrings.PostLayout, modifier) {
        EnumDropdownMenuHolder(
            postLayout,
            SettingsStateHolder.Instance::setPostLayout,
            containerColor = MaterialTheme.colorScheme.background,
        )
    }
}


@Composable
private fun MarkPostAsReadOption(modifier: Modifier = Modifier) {
    val markPostAsRead by SettingsStateHolder.Instance.markPostAsRead.collectAsState()
    SettingsOption(RainbowStrings.MarkPostAsRead, modifier) {
        EnumDropdownMenuHolder(
            markPostAsRead,
            SettingsStateHolder.Instance::setMarkPostAsRead,
            containerColor = MaterialTheme.colorScheme.background
        )
    }
}


@Composable
private fun PostSortingOptions(modifier: Modifier = Modifier) {
    val homePostSorting by SettingsStateHolder.Instance.homePostSorting.collectAsState()
    val profilePostSorting by SettingsStateHolder.Instance.profilePostSorting.collectAsState()
    val userPostSorting by SettingsStateHolder.Instance.userPostSorting.collectAsState()
    val subredditPostSorting by SettingsStateHolder.Instance.subredditPostSorting.collectAsState()
    val searchPostSorting by SettingsStateHolder.Instance.searchPostSorting.collectAsState()
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PostSortingOption(
            homePostSorting,
            SettingsStateHolder.Instance::setHomePostSorting,
            RainbowStrings.DefaultHomePostSorting,
        )
        PostSortingOption(
            profilePostSorting,
            SettingsStateHolder.Instance::setProfilePostSorting,
            RainbowStrings.DefaultProfilePostSorting,
        )
        PostSortingOption(
            userPostSorting,
            SettingsStateHolder.Instance::setUserPostSorting,
            RainbowStrings.DefaultUserPostSorting,
        )
        PostSortingOption(
            subredditPostSorting,
            SettingsStateHolder.Instance::setSubredditPostSorting,
            RainbowStrings.DefaultSubredditPostSorting,
        )
        PostSortingOption(
            searchPostSorting,
            SettingsStateHolder.Instance::setSearchPostSorting,
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