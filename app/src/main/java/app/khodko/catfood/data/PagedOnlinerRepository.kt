package app.khodko.catfood.data

import android.util.Log
import app.khodko.catfood.api.onliner.OnlinerService
import app.khodko.catfood.api.onliner.ProductResponse
import app.khodko.catfood.api.onliner.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.HttpException
import java.io.IOException

class PagedOnlinerRepository {

    private val service = OnlinerService.create()
    private val inMemoryCache = mutableListOf<ProductResponse>()
    private val results = MutableSharedFlow<SearchResult>(replay = 1)
    private var lastRequestedPage = 1
    private var lastPage = 1
    private var isRequestInProgress = false

    /**
     * Search repositories whose names match the query, exposed as a stream of data that will emit
     * every time we get more data from the network.
     */
    suspend fun start(query: Query): Flow<SearchResult> {
        lastRequestedPage = 1
        inMemoryCache.clear()
        requestAndSaveData(query)
        return results
    }

    suspend fun requestMore(query: Query) {
        if (isRequestInProgress || lastRequestedPage > lastPage) return
        val successful = requestAndSaveData(query)
        if (successful) {
            lastRequestedPage++
        }
    }

    suspend fun retry(query: Query) {
        if (isRequestInProgress) return
        requestAndSaveData(query)
    }

    private suspend fun requestAndSaveData(query: Query): Boolean {
        isRequestInProgress = true
        var successful = false
        try {
            val response = if (query.searchQuery != null) {
                service.search(query.searchQuery!!, lastRequestedPage)
            } else {
                query.page = lastRequestedPage
                service.getCatfood(query.map())
            }
            //val response = service.getCatfood(query.map())

            lastPage = response.page.last
            Log.d("CatfoodRepository", "response $response")
            inMemoryCache.addAll(response.products)

            val newList = mutableListOf<ProductResponse>()
            newList.addAll(inMemoryCache)

            results.emit(SearchResult.Success(newList))
            successful = true
        } catch (exception: IOException) {
            results.emit(SearchResult.Error(exception))
        } catch (exception: HttpException) {
            results.emit(SearchResult.Error(exception))
        }
        isRequestInProgress = false
        return successful
    }

}