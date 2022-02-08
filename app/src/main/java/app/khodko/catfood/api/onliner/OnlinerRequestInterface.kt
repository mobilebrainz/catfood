package app.khodko.catfood.api.onliner

import retrofit2.Response


interface OnlinerRequestInterface {

    suspend fun getProductsAsync(typefood: CatFoodType, page: Int): Response<SearchResponse>

    suspend fun getProductAsync(key: String): Response<ProductResponse>
}