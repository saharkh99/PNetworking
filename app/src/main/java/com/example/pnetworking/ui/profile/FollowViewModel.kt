package com.example.pnetworking.ui.profile

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.repository.follow.FollowRepository
import com.example.pnetworking.repository.profile.ProfileRepository
import com.example.pnetworking.utils.ChatViewmodel

class FollowViewModel(private val profileRepository: FollowRepository): ChatViewmodel() {
    fun getFollowers():MutableLiveData<List<String>> {
        return profileRepository.getFollowers()
    }

    fun increasingConnections(): MutableLiveData<Boolean> {
        return profileRepository.increasingConnections()
    }

    fun follow(fid: String):LiveData<Boolean>{
        return profileRepository.follow(fid)
    }
}