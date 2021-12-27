package com.rainbow.app.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.app.components.DropdownMenuHolder
import com.rainbow.app.utils.RainbowStrings

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
    val postLayout by SettingsModel.postLayout.collectAsState()
    SettingsOption(RainbowStrings.PostLayout, modifier) {
        DropdownMenuHolder(postLayout, SettingsModel::setPostLayout)
    }
}


@Composable
private fun PostFullHeightOption(modifier: Modifier = Modifier) {
    val isPostFulLHeight by SettingsModel.isPostFullHeight.collectAsState()
    SettingsOption(RainbowStrings.PostFullHeight, modifier) {
        Switch(
            isPostFulLHeight,
            onCheckedChange = { isPostFullHeight -> SettingsModel.setIsPostFullHeight(isPostFullHeight) },
        )
    }
}

@Composable
private fun MarkPostAsReadOption(modifier: Modifier = Modifier) {
    val markPostAsRead by SettingsModel.markPostAsRead.collectAsState()
    SettingsOption(RainbowStrings.MarkPostAsRead, modifier) {
        DropdownMenuHolder(markPostAsRead, SettingsModel::setMarkPostAsRead)
    }
}


@Composable
private fun PostSortingOptions(modifier: Modifier = Modifier) {
    val homePostSorting by SettingsModel.homePostSorting.collectAsState()
    val profilePostSorting by SettingsModel.profilePostSorting.collectAsState()
    val userPostSorting by SettingsModel.userPostSorting.collectAsState()
    val subredditPostSorting by SettingsModel.subredditPostSorting.collectAsState()
    val searchPostSorting by SettingsModel.searchPostSorting.collectAsState()
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PostSortingOption(
            homePostSorting,
            SettingsModel::setHomePostSorting,
            RainbowStrings.DefaultHomePostSorting,
        )
        PostSortingOption(
            profilePostSorting,
            SettingsModel::setProfilePostSorting,
            RainbowStrings.DefaultProfilePostSorting,
        )
        PostSortingOption(
            userPostSorting,
            SettingsModel::setUserPostSorting,
            RainbowStrings.DefaultUserPostSorting,
        )
        PostSortingOption(
            subredditPostSorting,
            SettingsModel::setSubredditPostSorting,
            RainbowStrings.DefaultSubredditPostSorting,
        )
        PostSortingOption(
            searchPostSorting,
            SettingsModel::setSearchPostSorting,
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
        DropdownMenuHolder(value, onValueUpdate)
    }
}