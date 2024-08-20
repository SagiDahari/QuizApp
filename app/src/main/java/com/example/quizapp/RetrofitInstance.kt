package com.example.quizapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Singleton Object for Retrofit
object RetrofitInstance {

    // Initialize Retrofit Instance only when accessed
    val api: OpenTriviaAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://opentdb.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenTriviaAPI::class.java)
    }
}