package com.rainbow.app.subreddit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.rainbow.app.subreddit.SubredditViewModel.SubredditIntent
import com.rainbow.app.utils.component1
import com.rainbow.app.utils.component2
import com.rainbow.app.utils.composed
import com.rainbow.domain.models.Rule

@Composable
fun SubredditRulesTab(subredditName: String, modifier: Modifier = Modifier) {

    val (state, emitIntent) = remember { SubredditViewModel() }

    remember(subredditName) {
        emitIntent(SubredditIntent.GetSubredditRules(subredditName))
    }

    state.rules.composed { rules ->

        LazyColumn(modifier) {

            items(rules.sortedByDescending { it.priority }) { rule ->

                RuleItem(rule)

            }

        }
    }

}

@Composable
private fun RuleItem(rule: Rule, modifier: Modifier = Modifier) {

    Column(modifier) {

        Text("${rule.priority}. ${rule.title}")

        Text(rule.description)

    }

}