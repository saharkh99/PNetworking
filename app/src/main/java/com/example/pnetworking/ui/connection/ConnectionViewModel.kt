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
import kotlin.collections.ArrayList

class ConnectionViewModel(private val repoRepository: ConnectionRepository): ChatViewmodel() {

    fun getUsers():LiveData<List<User>>{
        return  repoRepository.fakeUsers()
    }
    fun closestUsers(lan:String,lon:String,con:String):MutableLiveData<ArrayList<String>> {
        return repoRepository.closestUsers(lan, lon, con)
    }

}
