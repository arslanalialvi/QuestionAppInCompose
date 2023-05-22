package com.example.composejettrivia.network

import com.example.composejettrivia.model.Question
import retrofit2.http.GET

interface QuestionService {

    @GET("world.json")
    suspend fun getAllQuestions(): Question
}