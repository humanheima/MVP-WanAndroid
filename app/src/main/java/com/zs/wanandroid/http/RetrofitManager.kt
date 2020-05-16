package com.zs.wanandroid.http

import com.example.baselibrary.http.HttpLoggingInterceptor
import com.zs.wanandroid.constants.ApiConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by dmw on 2019/1/7.
 * Desc:
 */
class RetrofitManager private constructor() {

    companion object {

        private lateinit var mRetrofit: Retrofit

        fun get(): RetrofitManager {
            return Holder.MANAGER
        }
    }

    private object Holder {
        val MANAGER = RetrofitManager()
    }


    private val coroutineAPIService: CoroutineApiService

    private val okhttpClient: OkHttpClient

    init {
        val builder = OkHttpClient.Builder()
        builder.writeTimeout((5 * 1000).toLong(), TimeUnit.MILLISECONDS)
        builder.readTimeout((5 * 1000).toLong(), TimeUnit.MILLISECONDS)
        builder.connectTimeout((5 * 1000).toLong(), TimeUnit.MILLISECONDS)
        builder.addInterceptor(HttpLoggingInterceptor())

        okhttpClient = builder.build()

        mRetrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okhttpClient)
            .build()

        coroutineAPIService = mRetrofit.create(CoroutineApiService::class.java)
    }

    fun retrofit(): Retrofit = mRetrofit

    fun coroutineApiService() = coroutineAPIService

    fun okhttpClient(): OkHttpClient = okhttpClient

}