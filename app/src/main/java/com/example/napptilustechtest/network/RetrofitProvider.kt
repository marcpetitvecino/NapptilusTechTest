package com.example.napptilustechtest.network

import android.content.Context
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://2q2woep105.execute-api.eu-west-1.amazonaws.com/napptilus/oompa-loompas/"

class RetrofitProvider (context: Context) {

    private val cacheSize = 100 * 1024 * 1024 // 100 MB
    private var cache = Cache(context.cacheDir, cacheSize.toLong())

    private var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor { chain ->
            val response = chain.proceed(chain.request())
            val cacheControl = CacheControl.Builder()
                .maxAge(1, TimeUnit.MINUTES)
                .build()

            response.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .build()
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        })
        .cache(cache)
        .build()

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val retrofitService: OompaInterface by lazy {
        getRetrofit().create(OompaInterface::class.java)
    }

}