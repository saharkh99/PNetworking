package com.example.pnetworking.repository.test

import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Question

class TestDataSource {
    fun fetchQuestions(): MutableLiveData<List<Question>> {
        val questions = MutableLiveData<List<Question>>()
        var question1 = Question(" 1. Do you feel sometimes you're happy and sometimes blue?", 1, "emotion")
        var question2 = Question(" 2. Do you get easily distracted in the class or group's conversations?", 1, "emotion")
        var question3=Question(" 3. Do you feel sometimes you're upset without any reason?", 1, "emotion")
        var question4=Question(" 4. Are you easily get angry?", 1, "emotion")
        var question5=Question(" 5. Have it happened to you that you wanted to concentrate, but you distracted?", 1, "emotion")
        var question6=Question(" 6. Are you sometimes depressed and sometimes full of energy ?", 1, "emotion")
        var question7=Question(" 7. Do you prefer actions to plans?", 1, "extroversion")
        var question8=Question(" 8. Do you prefer contests that include speed?", 1, "extroversion")
        var question9=Question(" 9. Are you good at finding new friends?", 1, "extroversion")
        var question10=Question(" 10. Are you fast and secure?", 1, "extroversion")
        var question11=Question(" 11. Do you consider yourself as a full of life person ?", 1, "extroversion")
        var question12=Question(" 12. Do you prefer outside?", 1, "extroversion")
        val q = ArrayList<Question>()
        q.add(question1)
        q.add(question2)
        q.add(question3)
        q.add(question4)
        q.add(question5)
        q.add(question6)
        q.add(question7)
        q.add(question8)
        q.add(question9)
        q.add(question10)
        q.add(question11)
        q.add(question12)

        questions.value = q
        return questions
    }
}