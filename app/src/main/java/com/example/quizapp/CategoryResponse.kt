package com.example.quizapp

/**
 *  A data class for the response from the Open Trivia DB API,
 *  * that contains a list of Categories.
 */
data class CategoryResponse(
    val trivia_categories: List<Category>
)

/**
 * A data class for the Categories.
 */
data class Category(
    val id: Int,
    val name: String
)
