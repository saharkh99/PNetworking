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
    fun editProfile(name:String,bio:String,uid: String,fav:String):MutableLiveData<Boolean>{
        return profileRepository.editProfile(name, bio, uid,fav)
    }
    fun updatePhoto(image: String): MutableLiveData<Boolean>{
        return profileRepository.updatePhoto(image)
    }



}