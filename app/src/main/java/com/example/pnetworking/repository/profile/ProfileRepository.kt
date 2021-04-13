package com.example.pnetworking.repository.profile

import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.User

interface ProfileRepository {
    fun getUID():MutableLiveData<String?>
    fun getCurrentUser(uid:String): MutableLiveData<User>
    fun editProfile(name:String,bio:String,uid: String):MutableLiveData<Boolean>
}