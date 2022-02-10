package app.khodko.catfood.api.onliner

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

private const val PAGE_LIMIT = 30

interface OnlinerService {

    @GET("search/products")
    suspend fun search(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int = PAGE_LIMIT
    ): SearchResponse

    /*
    Ключи для paramsMap:
    page
    mfr - производитель
    typefood4cat - тип корма
     */
    @GET("search/catfood")
    suspend fun getCatfood(
        @QueryMap paramsMap: Map<String, String>
    ): SearchResponse

    @GET("search/catfood")
    suspend fun getCatfood(
        @Query("typefood4cat") typefood: String,
        @Query("page") page: Int
    ): SearchResponse

    @GET("products/{key}")
    suspend fun getProduct(
        @Path("key") key: String
    ): ProductResponse



    companion object {
        private const val BASE_URL = "https://catalog.api.onliner.by/"

        fun create(): OnlinerService {
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(OnlinerService::class.java)
        }
    }
}
