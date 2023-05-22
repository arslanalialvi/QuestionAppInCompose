package com.example.composejettrivia.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composejettrivia.data.DataOrException
import com.example.composejettrivia.model.QuestionItem
import com.example.composejettrivia.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(val questionRepository: QuestionRepository): ViewModel() {
     var dataOrException: MutableState<DataOrException<ArrayList<QuestionItem>, Boolean, Exception>>
            = mutableStateOf(DataOrException(null,true, Exception("")))
    init {
        getAllQuestions()
    }

    private fun getAllQuestions(){
        viewModelScope.launch {
            dataOrException.value.isLoading=true
            dataOrException.value= questionRepository.getAllQuestions()
            if (dataOrException.value.data.toString().isNotEmpty()) dataOrException.value.isLoading=false
        }
    }
}