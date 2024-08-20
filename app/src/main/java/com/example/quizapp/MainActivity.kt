package com.example.quizapp

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Enforce LTR layout direction
        enforceEnglishLocale(resources)
        setContent {
            AppTheme {
                QuizAppNavigation()
            }
        }
    }
}

fun enforceEnglishLocale(resources: Resources) {
    val config = Configuration(resources.configuration)
    config.setLocale(Locale.ENGLISH) // Set the locale to English
    config.setLayoutDirection(Locale.ENGLISH) // Enforce LTR direction
    resources.updateConfiguration(config, resources.displayMetrics)
}

