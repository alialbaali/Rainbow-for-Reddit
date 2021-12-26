package com.rainbow.app.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.app.components.DropdownMenuHolder
import com.rainbow.app.utils.RainbowStrings

@Composable
fun PostSortingOptions(modifier: Modifier = Modifier) {
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