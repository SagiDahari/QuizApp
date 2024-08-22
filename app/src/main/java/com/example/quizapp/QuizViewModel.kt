package com.example.quizapp


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.apache.commons.text.StringEscapeUtils

/**
 * The QuizViewModel class handles the logic and state for the quiz, including managing
 * the current question, user score, and selected answers. It also interacts with the
 * QuizApiService to fetch quiz questions.
 */
class QuizViewModel : ViewModel() {

    /**
     * A State containing the list of questions fetched from the API.
     */
    private val _questions = MutableStateFlow<List<TriviaQuestion>>(emptyList())
    val questions: StateFlow<List<TriviaQuestion>> get() = _questions.asStateFlow()

    /**
     * A State containing the current question index.
     */
    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> get() = _currentQuestionIndex.asStateFlow()

    /**
     * A State containing the user's current score.
     */
    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> get() = _score.asStateFlow()

    /**
     * A State containing the selected answer for the current question.
     */
    var selectedAnswer = mutableStateOf<String?>(null)
        private set

    /**
     * A State indicating whether an answer has been selected for the current question.
     */
    var isAnswerSelected = mutableStateOf(false)
        private set

    /**
     * A State containing the number of questions in the quiz for the settings screen.
     */
    var numOfQuestions = mutableStateOf(10)
        private set

    /**
     * A State containing the difficulty level of the quiz for the settings screen.
     */
    var difficulty = mutableStateOf("easy")
        private set

    /**
     * A State containing the category of the quiz for the settings screen.
     */
    var category = mutableStateOf<Int?>(null)
        private set

    /**
     * Decodes HTML entities in a given string.
     *
     * @param input The string to decode.
     */
    // generated by ChatGPT
    fun decodeHtmlEntities(input: String): String {
        return StringEscapeUtils.unescapeHtml4(input)
    }

    /**
     * Fetches questions from the API based on the provided parameters.
     *
     * @param amount The number of questions to fetch.
     * @param category The category of questions to fetch.
     * @param difficulty The difficulty level of questions to fetch.
     */
    fun fetchQuestions(amount: Int, category: Int?, difficulty: String?) {
        val validAmount = amount.coerceIn(1, 50) // Ensure the number is between 1 and 50
        viewModelScope.launch {
            val response = RetrofitInstance.api.getQuestions(validAmount, category, difficulty)
            println("Fetched ${response.body()!!.results.size} questions") // for debugging purposes.
            if (response.isSuccessful && response.body() != null) {
                // Decode HTML entities for each question and its answers (generated by chatGPT)
                _questions.value = response.body()!!.results.map { question ->
                    question.copy(
                        question = decodeHtmlEntities(question.question),
                        correct_answer = decodeHtmlEntities(question.correct_answer),
                        incorrect_answers = question.incorrect_answers.map { decodeHtmlEntities(it) }
                    )
                }
            } else {
                println("Error fetching questions: ${response.message()}")
            }
        }
    }

    /**
     * Selects an answer for the current question and updated the score accordingly.
     *
     * @param answer The selected answer.
     */
    fun selectAnswer(answer: String) {
        if (!isAnswerSelected.value) {
            selectedAnswer.value = answer
            isAnswerSelected.value = true
            if (answer == getCurrentQuestion()?.correct_answer) {
                _score.value += 1
            }
        }
    }

    /**
     * Moves to the next question in the quiz.
     */
    fun moveToNextQuestion() {
        if (_currentQuestionIndex.value < _questions.value.size - 1) {
            _currentQuestionIndex.value += 1
            selectedAnswer.value = null
            isAnswerSelected.value = false
        }
    }

    /**
     * Retrieves the current question from the list of questions.
     */
    fun getCurrentQuestion(): TriviaQuestion? {
        // Ensure the index is within the valid range and returns null if not.
        return questions.value.getOrNull(currentQuestionIndex.value)
    }

    /**
     * Updates the settings for the quiz.
     *
     * @param number The number of questions to update.
     * @param diff The difficulty level of questions to update.
     * @param cat The category of questions to update.
     */
    fun updateSettings(number: Int, diff: String, cat: Int?) {
        numOfQuestions.value = number
        difficulty.value = diff
        category.value = cat
    }

    /**
     * Resets the quiz state.
     */
    private fun resetQuizState() {
        _currentQuestionIndex.value = 0 // Reset the index
        _score.value = 0 // Reset the score
        _questions.value = emptyList() // Clear previous questions
        selectedAnswer.value = null // Reset answer selection status
        isAnswerSelected.value = false // Fetch new questions
    }

    /**
     * This function is called after a quiz is completed,
     * and it resets the quiz with the provided parameters,
     * that was changed or not in the settings.
     *
     * @param amount The number of questions to fetch.
     * @param category The category of questions to fetch.
     * @param difficulty The difficulty level of questions to fetch.
     */
    fun startNewQuiz(amount: Int, category: Int?, difficulty: String?) {
        resetQuizState() // Reset quiz-related state
        fetchQuestions(amount, category, difficulty)  // Fetch new questions
    }
}


