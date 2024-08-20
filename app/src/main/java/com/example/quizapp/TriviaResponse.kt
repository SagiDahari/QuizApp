package com.example.quizapp

data class TriviaResponse(
    val response_code: Int,
    val results: List<TriviaQuestion>
)
