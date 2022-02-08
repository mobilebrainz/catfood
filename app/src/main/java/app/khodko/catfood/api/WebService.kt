package app.khodko.catfood.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object WebService {

    const val TIMEOUT = 20000L
    const val LEVEL_LOG_DEBUG = true
    //const val USER_AGENT_HEADER = "app.khodko.catfood"
    const val USER_AGENT_HEADER = ""

    private val headerInterceptor = Interceptor { chain ->
        val request = chain.request()
            .newBuilder()
            .header("User-Agent", USER_AGENT_HEADER)
            .build()
        chain.proceed(request)
    }

    private val loggingIntercepter = HttpLoggingInterceptor().apply {
        level = if (LEVEL_LOG_DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        //.callTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
        //.connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
        //.readTimeout(120, TimeUnit.SECONDS)
        //.writeTimeout(90, TimeUnit.SECONDS)
        .addNetworkInterceptor(loggingIntercepter)
        .addInterceptor(headerInterceptor)
        .build()

    fun <T> createJsonRetrofitService(retrofitClass: Class<T>, url: String) =
        Retrofit.Builder().baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(httpClient).build()
            .create(retrofitClass)

}