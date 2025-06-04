package com.jhainusa.jss_student

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.jvm.java

interface GeminiApiService {
    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    fun generateContent(
        @Query("key") apiKey: String,
        @Body body: GeminiRequest
    ): Call<GeminiResponse>
}



object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)  // default was 10s
        .readTimeout(60, TimeUnit.SECONDS)     // Gemini might take longer
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val instance: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // ⬅ Attach custom client here
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiApiService::class.java)
    }
}

data class GeminiRequest(
    val contents: List<Content>
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String? = null,
    val inline_data: InlineData? = null
)

data class InlineData(
    val mime_type: String,
    val data: String
)
data class GeminiResponse(
    val candidates: List<Candidate>
)

data class Candidate(
    val content: ContentResponse
)

data class ContentResponse(
    val parts: List<TextPart>
)

data class TextPart(
    val text: String
)
