package com.example.pnetworking.repository.test

import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Question

interface TestRepository {
    fun getQuestions(): MutableLiveData<List<Question>>
}