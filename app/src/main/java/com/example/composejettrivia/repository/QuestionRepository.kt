package com.example.composejettrivia.repository

import android.util.Log
import com.example.composejettrivia.data.DataOrException
import com.example.composejettrivia.model.QuestionItem
import com.example.composejettrivia.network.QuestionService
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api: QuestionService) {
    val dataOrException = DataOrException<ArrayList<QuestionItem>,Boolean, Exception>()
    suspend fun getAllQuestions(): DataOrException<ArrayList<QuestionItem>,Boolean, Exception>
    {
        try {
            dataOrException.isLoading= true
            dataOrException.data= api.getAllQuestions()
            if (dataOrException.data.toString().isNotEmpty()) dataOrException.isLoading=false
        }
        catch (exception: Exception){
            dataOrException.exception= exception
            Log.d("Exception", dataOrException.exception!!.localizedMessage)
        }
        return dataOrException
    }
}