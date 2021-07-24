package com.example.chat.ui.main.connection

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.User
import com.example.pnetworking.repository.connection.ConnectionRepository
import com.example.pnetworking.utils.ChatViewmodel
import java.util.*
import javax.inject.Inject

class ConnectionViewModel(private val repoRepository: ConnectionRepository): ChatViewmodel() {

    fun getUsers():LiveData<List<User>>{
        return  repoRepository.fakeUsers()
    }

}
