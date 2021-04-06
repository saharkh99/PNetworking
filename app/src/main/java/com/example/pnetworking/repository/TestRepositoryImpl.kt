package com.example.pnetworking.repository

import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Question

class TestRepositoryImpl(private val testDataSource: TestDataSource):TestRepository {
    override fun getQuestions(): MutableLiveData<List<Question>> {
        return testDataSource.fetchQuestions()
    }
}