package app.khodko.catfood.ui.search

import androidx.lifecycle.*
import app.khodko.catfood.core.utils.UiAction
import app.khodko.catfood.data.SearchRepository
import app.khodko.catfood.data.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val VISIBLE_THRESHOLD = 5
private const val DEFAULT_QUERY = "Royal Canin"

class SearchViewModel : ViewModel() {

    val state: LiveData<UiState>
    val accept: (UiAction) -> Unit
    private val repository = SearchRepository()

    init {
        val queryLiveData = MutableLiveData(DEFAULT_QUERY)

        state = queryLiveData.distinctUntilChanged().switchMap { query ->
            liveData {
                val uiState = repository.getSearchResultStream(query)
                    .map { UiState(query = query, searchResult = it) }
                    .asLiveData(Dispatchers.Main)
                emitSource(uiState)
            }
        }

        accept = { action ->
            when (action) {
                is UiAction.Search -> queryLiveData.postValue(action.query)
                is UiAction.Scroll ->
                    if (action.shouldFetchMore) {
                        queryLiveData.value?.let {
                            viewModelScope.launch { repository.requestMore(it) }
                        }
                    }
                }
            }
        }
}

private val UiAction.Scroll.shouldFetchMore
    get() = visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount


data class UiState(
    val query: String,
    val searchResult: SearchResult
)
