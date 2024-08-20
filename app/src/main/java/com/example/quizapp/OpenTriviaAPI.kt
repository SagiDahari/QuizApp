package com.example.quizapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenTriviaAPI {
        @GET("/api.php")
        suspend fun getQuestions(
            @Query("amount") amount: Int,
            @Query("category") category: Int?,
            @Query("difficulty") difficulty: String?,
            @Query("type") type: String = "multiple"
        ): Response<TriviaResponse>

        @GET("/api_category.php")
        suspend fun getCategories(): Response<CategoryResponse>
}
