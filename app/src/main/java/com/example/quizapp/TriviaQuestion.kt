package com.example.quizapp

/**
 * Data class for a trivia question,
 * that contains the question, the correct answer, and the incorrect answers.
 */
data class TriviaQuestion(
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)
