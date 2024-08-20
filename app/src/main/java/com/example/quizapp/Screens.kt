package com.example.quizapp

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@Composable
fun HomeScreen( viewModel: QuizViewModel, navController: NavHostController ) {
    val numOfQuestions by remember { viewModel.numOfQuestions }
    val selectedDifficulty by remember { viewModel.difficulty }
    val selectedCategory by remember { viewModel.category }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Welcome to QuizApp!", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            viewModel.startNewQuiz(numOfQuestions, selectedCategory, selectedDifficulty)
            navController.navigate("quiz")
        }) {
            Text(text = "Start Quiz")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(onClick = { navController.navigate("settings") }) {
            Text(text = "Settings")
        }
    }
}

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    navController: NavHostController
) {
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()
    val score by viewModel.score.collectAsState()
    val isAnswerSelected by viewModel.isAnswerSelected

    // Handle back press
    BackHandler {
        navController.navigate("home") {
            popUpTo("home") { inclusive = true }
        }
    }

    viewModel.questions.collectAsState().value.let { questions ->
        if (questions.isNotEmpty()) {
            val currentQuestion = remember(currentQuestionIndex) {
                questions[currentQuestionIndex]
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Question ${currentQuestionIndex + 1}",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = currentQuestion.question,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)  // Restrict the width
                        .padding(8.dp),
                    textAlign = TextAlign.Center // Center the text within its bounds
                )
                Spacer(modifier = Modifier.height(16.dp))


                val allAnswers = remember(currentQuestion) {
                    currentQuestion.incorrect_answers.toMutableList().apply {
                        add(currentQuestion.correct_answer)
                        shuffle()
                    }
                }

                LazyColumn (
                    modifier = Modifier.fillMaxWidth(0.8f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center

                ){
                    items(allAnswers) { answer ->
                        // Determine the background color based on the answer
                        val backgroundColor = when {
                            isAnswerSelected && answer == currentQuestion.correct_answer -> Color.Green
                            isAnswerSelected && answer != currentQuestion.correct_answer -> Color.Red
                            else -> Color.Transparent
                        }

                        Button(
                            onClick = {
                                if (!isAnswerSelected) {
                                    viewModel.selectAnswer(answer)
                                }
                            },
                            enabled = !isAnswerSelected, // Disable buttons after selection
                            colors = ButtonDefaults.buttonColors(
                                disabledContainerColor = backgroundColor // Disabled state background color
                            )
                        ) {
                            Text(text = answer)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (currentQuestionIndex < questions.size - 1) {
                            viewModel.moveToNextQuestion()
                        } else {
                            navController.navigate("results/$score/${questions.size}")
                        }
                    },
                    enabled = isAnswerSelected
                ) {
                    Text(text = if (currentQuestionIndex < questions.size - 1) "Next Question" else "Finish Quiz")
                }
            }
        } else {
            Text(text = "Loading questions...", modifier = Modifier.fillMaxSize())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen( viewModel: QuizViewModel, navController: NavHostController ) {
    var numOfQuestions by remember { viewModel.numOfQuestions }
    val difficultyOptions = listOf("easy", "medium", "hard")
    var selectedDifficulty by remember { viewModel.difficulty }
    var expandedDifficulty by remember { mutableStateOf(false) }

    var categories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var selectedCategory by remember { viewModel.category }
    var expandedCategory by remember { mutableStateOf(false) }

    // Handle back press
    BackHandler {
        navController.navigate("home") {
            popUpTo("home") { inclusive = true }
        }
    }

    // Fetch categories from the API
    LaunchedEffect(Unit) {
        val response = RetrofitInstance.api.getCategories()
        if (response.isSuccessful) {
            categories = response.body()?.trivia_categories ?: emptyList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Settings", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        // Number of Questions
        Text(text = "Number of Questions: $numOfQuestions")
        Slider(
            value = numOfQuestions.toFloat(),
            onValueChange = { numOfQuestions = it.toInt() },
            valueRange = 1f..50f,  // Limit the range between 1 and 50
            steps = 49 // This ensures there are discrete values between 1 and 50
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Difficulty Dropdown
        ExposedDropdownMenuBox(
            expanded = expandedDifficulty,
            onExpandedChange = { expandedDifficulty = !expandedDifficulty }
        ) {
            TextField(
                readOnly = true,
                value = selectedDifficulty.replaceFirstChar { it.uppercaseChar() },
                onValueChange = {},
                label = { Text("Select Difficulty") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDifficulty)
                },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedDifficulty,
                onDismissRequest = { expandedDifficulty = false }
            ) {
                difficultyOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option.replaceFirstChar { it.uppercaseChar() }) },
                        onClick = {
                            selectedDifficulty = option
                            expandedDifficulty = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Category Dropdown
        ExposedDropdownMenuBox(
            expanded = expandedCategory,
            onExpandedChange = { expandedCategory = !expandedCategory }
        ) {
            TextField(
                readOnly = true,
                value = categories.find { it.id == selectedCategory }?.name ?: "Any Category",
                onValueChange = {},
                label = { Text("Select Category") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory)
                },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedCategory,
                onDismissRequest = { expandedCategory = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(text = category.name) },
                        onClick = {
                            selectedCategory = category.id
                            expandedCategory = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save Button
        Button(onClick = {
            viewModel.updateSettings(numOfQuestions, selectedDifficulty, selectedCategory)
            navController.navigate("home")
        }) {
            Text(text = "Save and Go Back")
        }
    }
}

@Composable
fun ResultsScreen(score: Int, totalQuestions: Int, navController: NavHostController) {

    // Handle back press
    BackHandler {
        navController.navigate("home") {
            popUpTo("home") { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Quiz Completed!")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "You answered $score out of $totalQuestions questions correctly.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("home")}) {
            Text(text = "Go to Home")
        }
    }
}


