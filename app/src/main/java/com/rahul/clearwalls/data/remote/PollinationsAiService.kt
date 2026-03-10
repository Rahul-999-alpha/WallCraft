package com.rahul.clearwalls.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.URLEncoder
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PollinationsAiService @Inject constructor(
    private val okHttpClient: OkHttpClient
) {
    suspend fun generateImage(prompt: String): ByteArray = withContext(Dispatchers.IO) {
        val encoded = URLEncoder.encode(prompt, "UTF-8")
        val url = "https://image.pollinations.ai/prompt/$encoded?width=1080&height=1920&nologo=true"
        val request = Request.Builder().url(url).get().build()
        val client = okHttpClient.newBuilder()
            .callTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) throw IOException("Pollinations API failed: ${response.code}")
        response.body?.bytes() ?: throw IOException("Empty response body")
    }
}
