package com.sevban.network.util.interceptor

import com.sevban.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val url = request
            .url()
            .newBuilder()
            .addQueryParameter(API_KEY_QUERY_PARAMETER, BuildConfig.API_KEY)
            .build()

        val requestBuilder = request.newBuilder().url(url)

        return chain.proceed(requestBuilder.build())
    }

    companion object {
        const val API_KEY_QUERY_PARAMETER = "appid"
    }
}