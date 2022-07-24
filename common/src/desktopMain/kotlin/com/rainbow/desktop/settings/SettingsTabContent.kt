package com.rainbow.desktop.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.common.utils.defaultPadding
import com.rainbow.common.utils.defaultSurfaceShape

@Composable
fun SettingsTabContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) = Column(modifier.defaultSurfaceShape().defaultPadding(), Arrangement.spacedBy(16.dp), content = content)