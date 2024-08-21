package com.example.quizapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for the Open Trivia API,
 * that defines the endpoints for getting questions and categories.
 */
interface OpenTriviaAPI {

    /**
     * Fetches a list of questions from the API,
     * based on the specified parameters asynchronously.
     *
     * @param amount The number of questions to fetch.
     * @param category The category ID to filter questions.
     * @param difficulty The difficulty level of the questions.
     * @return A Response object containing the list of questions.
     */
    @GET("/api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int,
        @Query("category") category: Int?,
        @Query("difficulty") difficulty: String?,
        @Query("type") type: String = "multiple"
    ): Response<TriviaResponse>

    /**
     * Fetches a list of available categories from the API asynchronously.
     *
     * @return A Response object containing the list of categories.
     */
    @GET("/api_category.php")
    suspend fun getCategories(): Response<CategoryResponse>
}
