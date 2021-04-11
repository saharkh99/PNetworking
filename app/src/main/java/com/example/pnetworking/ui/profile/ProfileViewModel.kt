package com.example.pnetworking.ui.profile

import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.User
import com.example.pnetworking.repository.profile.ProfileRepository
import com.example.pnetworking.utils.ChatViewmodel

class ProfileViewModel(private val profileRepository: ProfileRepository):ChatViewmodel() {
    fun getIDUser():MutableLiveData<String?> {
        return profileRepository.getUID()
    }
    fun getCurrentUser(uid:String):MutableLiveData<User>{
        return profileRepository.getCurrentUser(uid)
    }

}