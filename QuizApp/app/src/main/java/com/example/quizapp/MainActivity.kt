package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapp.ui.theme.QuizAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizAppTheme {
                QuizApp()
            }
        }
    }
}

@Composable
fun QuizApp() {
    val questions = listOf(
        "What is the capital of France?" to "Paris",
        "What is 2 + 2?" to "4",
        "What color is the sky?" to "Blue"
    )

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var userAnswer by remember { mutableStateOf("") }
    var quizComplete by remember { mutableStateOf(false) }
    val currentQuestion = questions.getOrNull(currentQuestionIndex)?.first.orEmpty()
    val correctAnswer = questions.getOrNull(currentQuestionIndex)?.second.orEmpty()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            // Align SnackbarHost to the top center of the screen
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.align(Alignment.TopCenter) // Place Snackbar at the top
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (quizComplete) {
                Text(text = "Quiz complete! Great job!", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    quizComplete = false
                    currentQuestionIndex = 0
                    userAnswer = ""
                }) {
                    Text(text = "Restart Quiz")
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Text(
                        text = currentQuestion,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = userAnswer,
                    onValueChange = { userAnswer = it },
                    label = { Text("Your Answer") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (userAnswer.equals(correctAnswer, ignoreCase = true)) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Correct!")
                            }
                            if (currentQuestionIndex < questions.size - 1) {
                                currentQuestionIndex++
                                userAnswer = ""
                            } else {
                                quizComplete = true
                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Incorrect! Try again.")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Submit Answer")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizAppPreview() {
    QuizAppTheme {
        QuizApp()
    }
}
