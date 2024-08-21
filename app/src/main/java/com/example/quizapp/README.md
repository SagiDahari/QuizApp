# QuizApp

## Overview
QuizApp is an interactive quiz application designed to test users knowledge on various topics.
It provides customizable quiz settings and an engaging user experience.

## Features
- Customizable number of questions
- Adjustable difficulty levels
- Multiple categories
- Score tracking and results display
- User-friendly interface

## Installation and Setup

### Prerequisites
- Android Studio
- Android SDK

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/SagiDahari/QuizApp.git
   ```
2. Open the project in Android Studio.
3. Sync the Gradle files.
4. Run the application on an emulator or a physical device.

## Usage Instructions (Manual)

### Home Screen
- **Start Quiz**: Begin a new quiz with the selected settings.
- **Settings**: Configure quiz parameters like: category, difficulty and number of questions.

### Quiz Screen
- **Answer Questions**: Select answers to proceed through the quiz.
- **View Results**: The correct answer is going to be displayed in green,
- and the incorrect answers are going to be displayed in red,
- At the end of the quiz, You will be given the total amount of correctly answered questions.

### Settings Screen
- **Number of Questions**: Adjust the number of questions for the quiz.
- **Difficulty Level**: Choose the difficulty of the questions.
- **Category**: Select the quiz category.

## Technical Documentation

### Architecture
- **Model-View-ViewModel (MVVM)**: Used for separating UI from business logic.

### Components
- **HomeScreen**: Displays the home screen and handles navigation.
- **SettingsScreen**: Allows users to adjust quiz settings.
- **QuizScreen**: Allows the user to choose an answer to the current question, shows the correct and incorrect answers, and navigate to the next question or results screen.
- **ResultsScreen**: Displays The number of questions that were answered correctly, and navigates to the home screen.
- **QuizViewModel**: Manages quiz logic and state.
- **Navigation**: Handles the apps navigation.
- **OpenTriviaAPI**: An Interface that handles the API requests for trivia questions and categories.
- **RetrofitInstance**: Instantiate a Retrofit singleton for making the API requests.

### Libraries and Tools
- **Jetpack Compose**: For building the UI.
- **Retrofit**: For network requests.

## Contributing

### Guidelines
- Fork the repository and create a new branch for each feature or fix.
- Ensure that code adheres to the project’s coding standards.
- Write clear commit messages and include relevant details.
- Submit a pull request with a detailed description of changes.

### Contact
- **Email**: sagidahari7@gmail.com
- **Github**: https://www.github.com/SagiDahari

