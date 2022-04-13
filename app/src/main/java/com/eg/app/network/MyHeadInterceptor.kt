package com.eg.app.network

import com.eg.app.util.CacheUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 自定义头部参数拦截器，传入heads
 */
class MyHeadInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("lang", "zh")
                .addHeader("token", CacheUtil.token)
                .addHeader("ran", Math.random().toString())
        return chain.proceed(builder.build())
    }

}