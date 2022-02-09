package app.khodko.catfood.data

import android.util.Log
import app.khodko.catfood.api.onliner.OnlinerService
import app.khodko.catfood.api.onliner.ProductResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.HttpException
import java.io.IOException


class SearchRepository() {

    private val service = OnlinerService.create()
    // keep the list of all results received
    private val inMemoryCache = mutableListOf<ProductResponse>()

    // shared flow of results, which allows us to broadcast updates so
    // the subscriber will have the latest data
    private val searchResults = MutableSharedFlow<SearchResult>(replay = 1)

    private var lastRequestedPage = 1
    private var lastPage = 1
    private var isRequestInProgress = false

    /**
     * Search repositories whose names match the query, exposed as a stream of data that will emit
     * every time we get more data from the network.
     */
    suspend fun getSearchResultStream(query: String): Flow<SearchResult> {
        Log.d("OnlinerRepository", "New query: $query")
        lastRequestedPage = 1
        inMemoryCache.clear()
        requestAndSaveData(query)
        return searchResults
    }

    suspend fun requestMore(query: String) {
        if (isRequestInProgress || lastRequestedPage > lastPage) return
        val successful = requestAndSaveData(query)
        if (successful) {
            lastRequestedPage++
        }
    }

    suspend fun retry(query: String) {
        if (isRequestInProgress) return
        requestAndSaveData(query)
    }

    private suspend fun requestAndSaveData(query: String): Boolean {
        isRequestInProgress = true
        var successful = false
        try {
            val response = service.search(query, lastRequestedPage)
            lastPage = response.page.last
            Log.d("OnlinerRepository", "response $response")
            inMemoryCache.addAll(response.products)

            val newList = mutableListOf<ProductResponse>()
            newList.addAll(inMemoryCache)

            searchResults.emit(SearchResult.Success(newList))
            successful = true
        } catch (exception: IOException) {
            searchResults.emit(SearchResult.Error(exception))
        } catch (exception: HttpException) {
            searchResults.emit(SearchResult.Error(exception))
        }
        isRequestInProgress = false
        return successful
    }

}
