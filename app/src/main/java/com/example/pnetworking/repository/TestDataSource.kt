package com.example.pnetworking.repository

import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Question

class TestDataSource {
    fun fetchQuestions(): MutableLiveData<List<Question>> {
        val questions = MutableLiveData<List<Question>>()
        var question1 =
            Question(" 1. do you feel sometimes you're happy and sometimes blue?", 1, "emotion")
        var question2 =
            Question(" 2. do you feel sometimes you're upset without any reason?", 1, "emotion")
        val q = ArrayList<Question>()
        q.add(question1)
        q.add(question2)
        questions.value = q
        return questions
    }
}