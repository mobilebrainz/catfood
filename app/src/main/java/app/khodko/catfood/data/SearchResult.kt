package app.khodko.catfood.data

import app.khodko.catfood.api.onliner.ProductResponse

sealed class SearchResult {
    data class Success(val data: List<ProductResponse>) : SearchResult()
    data class Error(val error: Exception) : SearchResult()
}

sealed class ProductResult {
    data class Success(val data: ProductResponse) : ProductResult()
    data class Error(val error: Exception) : ProductResult()
}
