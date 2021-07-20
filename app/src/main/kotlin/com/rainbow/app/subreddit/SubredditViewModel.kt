package com.rainbow.app.subreddit

import com.rainbow.app.subreddit.SubredditViewModel.SubredditIntent
import com.rainbow.app.subreddit.SubredditViewModel.SubredditIntent.*
import com.rainbow.app.subreddit.SubredditViewModel.SubredditState
import com.rainbow.app.utils.*
import com.rainbow.data.Repos
import com.rainbow.domain.ViewModel
import com.rainbow.domain.models.Rule
import com.rainbow.domain.models.Subreddit
import com.rainbow.domain.models.SubredditsSearchSorting
import com.rainbow.domain.repository.RuleRepository
import com.rainbow.domain.repository.SubredditRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class SubredditViewModel(
    private val subredditRepository: SubredditRepository = Repos.SubredditRepo,
    private val ruleRepository: RuleRepository = Repos.RuleRepo,
) : ViewModel<SubredditIntent, SubredditState>(SubredditState()) {

    override suspend fun onIntent(intent: SubredditIntent): SubredditState = when (intent) {
        is GetMySubreddits -> withState {
            copy(
//               mySubreddits = subredditRepository.getMySubreddits(intent.lastSubredditId)
            )
        }
        is GetSubreddit -> withState {
            copy(
//                subreddit = subredditRepository
//                    .getSubreddit(intent.subredditName)
//                    .toUIState(),
            )
        }
        is GetSubredditRules -> withState {
            copy(
                rules = ruleRepository
                    .getSubredditRules(intent.subredditName)
                    .toUIState()
            )
        }
        is SubscribeSubreddit -> withState {
            copy(
                subreddit = subreddit.map { it.copy(isSubscribed = true) },
            )
        }
        is UnSubscribeSubreddit -> withState {
            copy(
                subreddit = subreddit.map { it.copy(isSubscribed = false) },
            )
        }
        is SearchSubreddits -> withState {
            copy(
//                searchedSubreddits = subredditRepository
//                    .searchSubreddit(intent.subredditName, SubredditsSearchSorting.Relevance)
//                    .toUIState()
            )
        }
        is FavoriteSubreddit -> withState {
            subredditRepository.favoriteSubreddit(intent.subredditName)
            this
        }
        is UnFavoriteSubreddit -> withState {
            subredditRepository.unFavoriteSubreddit(intent.subredditName)
            this
        }
    }

    sealed class SubredditIntent : Intent {
        data class GetMySubreddits(val lastSubredditId: String?) : SubredditIntent()
        data class GetSubreddit(val subredditName: String) : SubredditIntent()
        data class GetSubredditRules(val subredditName: String) : SubredditIntent()
        data class SubscribeSubreddit(val subredditId: String) : SubredditIntent()
        data class UnSubscribeSubreddit(val subredditId: String) : SubredditIntent()
        data class FavoriteSubreddit(val subredditName: String) : SubredditIntent()
        data class UnFavoriteSubreddit(val subredditName: String) : SubredditIntent()
        data class SearchSubreddits(val subredditName: String) : SubredditIntent()
    }

    data class SubredditState(
        val mySubreddits: Flow<List<Subreddit>> = emptyFlow(),
        val subreddit: UIState<Subreddit> = UIState.Loading,
        val rules: UIState<List<Rule>> = UIState.Loading,
        val searchedSubreddits: UIState<List<Subreddit>> = UIState.Loading,
    ) : State

}