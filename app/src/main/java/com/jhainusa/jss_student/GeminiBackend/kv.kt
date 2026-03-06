package com.jhainusa.jss_student.GeminiBackend

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.concurrent.TimeUnit

interface SupabaseApiService {
    @Multipart
    @POST("super-action")
    fun sendTimetable(
        @Part("user_id") userId: String, // Changed from RequestBody to String
        @Part image: MultipartBody.Part
    ): Call<String>
}

object SupabaseClient {
    private const val BASE_URL = "https://lawyxhtjelxjstyygxij.supabase.co/functions/v1/"
    private const val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imxhd3l4aHRqZWx4anN0eXlneGlqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzI3MDE5MDQsImV4cCI6MjA4ODI3NzkwNH0.gf8bvo6ZXX5MQSKHfzR1O_Dd_tce9Zi7EpgLef5NNCs"

    val api: SupabaseApiService by lazy {
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("apikey", API_KEY)
                    .addHeader("Authorization", "Bearer $API_KEY")
                    .build()
                chain.proceed(request)
            }
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SupabaseApiService::class.java)
    }
}
// ... rest of your data classes
