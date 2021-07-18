package com.example.pnetworking.repository.chatfragment

import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Message

interface ChatFragmentRepository {
    fun getRecentMessages(): MutableLiveData<Message>
}