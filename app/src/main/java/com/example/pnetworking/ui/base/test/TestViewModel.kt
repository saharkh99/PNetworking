package com.example.pnetworking.ui.base.test

import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Question
import com.example.pnetworking.repository.TestRepository
import com.example.pnetworking.utils.ChatViewmodel

class TestViewModel(private val testRepository: TestRepository): ChatViewmodel() {
    var question= MutableLiveData<List<Question>>()
    init {
        fetchData()
    }

    private fun fetchData() {
        question= testRepository.getQuestions()
    }

}