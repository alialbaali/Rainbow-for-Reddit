package com.rainbow.desktop.trophy

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import com.rainbow.domain.models.Trophy

fun LazyListScope.trophies(trophies: List<Trophy>) {
    items(trophies) { trophy ->
        TrophyItem(trophy, Modifier.fillParentMaxWidth())
    }
}