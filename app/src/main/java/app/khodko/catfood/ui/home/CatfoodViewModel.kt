package app.khodko.catfood.ui.home

import androidx.lifecycle.*
import app.khodko.catfood.api.onliner.Query
import app.khodko.catfood.data.CatfoodRepository
import app.khodko.catfood.data.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val VISIBLE_THRESHOLD = 10

class CatfoodViewModel : ViewModel() {

    val state: LiveData<UiState>
    val accept: (UiAction) -> Unit
    private val repository = CatfoodRepository()

    init {
        val queryLiveData = MutableLiveData(Query())

        state = queryLiveData.distinctUntilChanged().switchMap { query ->
            liveData {
                val uiState = repository.start(query)
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
    val query: Query,
    val searchResult: SearchResult
)

sealed class UiAction {
    data class Search(val query: Query) : UiAction()
    data class Scroll(
        val visibleItemCount: Int,
        val lastVisibleItemPosition: Int,
        val totalItemCount: Int
    ) : UiAction()
}
