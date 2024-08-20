package com.example.quizapp


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.apache.commons.text.StringEscapeUtils

class QuizViewModel: ViewModel() {

    private val _questions = MutableStateFlow<List<TriviaQuestion>>(emptyList())
    val questions: StateFlow<List<TriviaQuestion>> get() = _questions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> get() = _currentQuestionIndex.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> get() = _score.asStateFlow()

    var selectedAnswer = mutableStateOf<String?>(null)
        private set

    var isAnswerSelected = mutableStateOf(false)
        private set

    // MutableState for the settings
    var numOfQuestions = mutableStateOf(10)
        private set

    var difficulty = mutableStateOf("easy")
        private set

    var category = mutableStateOf<Int?>(null)
        private set

    // HTML Decoder function
    fun decodeHtmlEntities(input: String): String {
        return StringEscapeUtils.unescapeHtml4(input)
    }

    // Async function to fetch questions from API (!! is a non-null assertion operator)
    fun fetchQuestions(amount: Int, category: Int?, difficulty: String?) {
        val validAmount = amount.coerceIn(1, 50) // Ensure the number is between 1 and 50
        viewModelScope.launch {
            _currentQuestionIndex.value = 0 // Reset the index here
            _score.value = 0 // Reset the score here
            _questions.value = emptyList()   // Clear previous questions
            val response = RetrofitInstance.api.getQuestions(validAmount, category, difficulty)
            println("Fetched ${response.body()!!.results.size} questions")
            if (response.isSuccessful && response.body() != null) {
                // Decode HTML entities for each question and its answers
                _questions.value = response.body()!!.results.map { question ->
                    question.copy(
                        question = decodeHtmlEntities(question.question),
                        correct_answer = decodeHtmlEntities(question.correct_answer),
                        incorrect_answers = question.incorrect_answers.map { decodeHtmlEntities(it) }
                    )
                }
            }
            else {
                println("Error fetching questions: ${response.message()}")
            }
        }
    }

    fun selectAnswer(answer: String) {
        if (!isAnswerSelected.value) {
            selectedAnswer.value = answer
            isAnswerSelected.value = true
            if (answer == getCurrentQuestion()?.correct_answer) {
                _score.value += 1
            }
        }
    }

    fun moveToNextQuestion() {
        if (_currentQuestionIndex.value < _questions.value.size - 1) {
            _currentQuestionIndex.value += 1
            selectedAnswer.value = null
            isAnswerSelected.value = false
        }
    }

    fun getCurrentQuestion(): TriviaQuestion? {
        return questions.value.getOrNull(currentQuestionIndex.value)
    }

    // Function to update settings and fetch new questions
    fun updateSettings(number: Int, diff: String, cat: Int?) {
        numOfQuestions.value = number
        difficulty.value = diff
        category.value = cat
    }

    fun startNewQuiz(amount: Int, category: Int?, difficulty: String?) {
        _currentQuestionIndex.value = 0  // Reset the index
        _score.value = 0 // Reset the score
        _questions.value = emptyList()   // Clear previous questions
        selectedAnswer.value = null      // Reset selected answer
        isAnswerSelected.value = false   // Reset answer selection status
        fetchQuestions(amount, category, difficulty)  // Fetch new questions
    }
}


