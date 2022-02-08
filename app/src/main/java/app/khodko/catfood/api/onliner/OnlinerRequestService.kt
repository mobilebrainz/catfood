package app.khodko.catfood.api.onliner

import app.khodko.catfood.api.onliner.OnlinerRequest.Companion.ONLINER_PRODUCT
import app.khodko.catfood.api.onliner.OnlinerRequest.Companion.ONLINER_SEARCH_CATFOOD
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface OnlinerRequestService {

    @GET(ONLINER_SEARCH_CATFOOD)
    suspend fun getProductsAsync(
        @Query("typefood4cat") typefood: String,
        @Query("page") page: Int
    ): Response<SearchResponse>

    @GET("$ONLINER_PRODUCT{key}")
    suspend fun getProductAsync(
        @Path("key") key: String
    ): Response<ProductResponse>
}