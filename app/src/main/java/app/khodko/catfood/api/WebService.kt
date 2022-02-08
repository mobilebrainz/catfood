package app.khodko.catfood.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object WebService {

    const val LEVEL_LOG_DEBUG = true

    private val headerInterceptor = Interceptor { chain ->
        val request = chain.request()
            .newBuilder()
            .build()
        chain.proceed(request)
    }

    private val loggingIntercepter = HttpLoggingInterceptor().apply {
        level = if (LEVEL_LOG_DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(loggingIntercepter)
        .addInterceptor(headerInterceptor)
        .build()

    fun <T> createJsonRetrofitService(retrofitClass: Class<T>, url: String) =
        Retrofit.Builder().baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(httpClient).build()
            .create(retrofitClass)
}