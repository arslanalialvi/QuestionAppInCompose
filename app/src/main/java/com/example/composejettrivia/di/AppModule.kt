package com.example.composejettrivia.di

import com.example.composejettrivia.network.QuestionService
import com.example.composejettrivia.repository.QuestionRepository
import com.example.composejettrivia.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun getQuestionService(): QuestionService = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build().create(QuestionService::class.java)

    @Provides
    @Singleton
    fun getQuestionRepository(api: QuestionService): QuestionRepository = QuestionRepository(api)
}