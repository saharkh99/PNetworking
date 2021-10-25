package com.example.pnetworking.ui.profile

import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.User
import com.example.pnetworking.repository.profile.ProfileRepository
import com.example.pnetworking.utils.ChatViewmodel

class ProfileViewModel(private val profileRepository: ProfileRepository):ChatViewmodel() {
    companion object{
        val phase=MutableLiveData<String>()
    }

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
    fun changePhaseOfRequest(phaseStr:String){
        phase.value=phaseStr
    }
    fun getPhaseOfRequest():MutableLiveData<String>{
        return phase
    }
    fun saveFav(fav:String) = profileRepository.saveFav(fav)



}