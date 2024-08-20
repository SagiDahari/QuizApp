package com.example.quizapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun QuizAppNavigation() {
    val navController = rememberNavController()
    val quizViewModel: QuizViewModel = viewModel()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(quizViewModel, navController) }
        composable("quiz") { QuizScreen(quizViewModel, navController) }
        composable("settings") { SettingsScreen(quizViewModel, navController) }
        composable(
            route = "results/{score}/{totalQuestions}",
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("totalQuestions") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val totalQuestions = backStackEntry.arguments?.getInt("totalQuestions") ?: 0
            ResultsScreen(
                score = score,
                totalQuestions = totalQuestions,
                navController = navController
            )
        }
    }
}