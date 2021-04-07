package com.example.pnetworking.repository.test

import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Question
import com.example.pnetworking.repository.test.TestDataSource
import com.example.pnetworking.repository.test.TestRepository

class TestRepositoryImpl(private val testDataSource: TestDataSource): TestRepository {
    override fun getQuestions(): MutableLiveData<List<Question>> {
        return testDataSource.fetchQuestions()
    }
}