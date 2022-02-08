package app.khodko.catfood.api.onliner

import retrofit2.Response


interface OnlinerRequestInterface {

    suspend fun getProductsAsync(typefood: String, page: Int): Response<SearchResponse>

    suspend fun getProductAsync(key: String): Response<ProductResponse>
}