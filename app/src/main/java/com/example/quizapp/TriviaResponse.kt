package com.example.quizapp

/**
 * A data class for the response from the Open Trivia DB API,
 * that contains a list of TriviaQuestions.
 */
data class TriviaResponse(
    val response_code: Int,
    val results: List<TriviaQuestion>
)
