package com.example.composejettrivia.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TriviaHome(viewModel: QuestionsViewModel = hiltViewModel()){

    Questions(viewModel)
}