package com.example.mixmate.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {

  companion object {
    private const val BASE_URL = "https://predict-dn2kyiya7a-et.a.run.app/"
    private const val BASE_EXPLORE = "https://mixmate-387005.et.r.appspot.com/"

    fun create(): ApiService {
      val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

      val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

      val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

      return retrofit.create(ApiService::class.java)
    }

    fun createExplore(): ApiService {
      val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

      val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

      val retrofit = Retrofit.Builder()
        .baseUrl(BASE_EXPLORE)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

      return retrofit.create(ApiService::class.java)
    }


  }
}
