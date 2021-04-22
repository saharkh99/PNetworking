package com.example.pnetworking.ui.profile


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.repository.follow.FollowRepository
import com.example.pnetworking.utils.ChatViewmodel

class FollowViewModel(private val followRepository: FollowRepository): ChatViewmodel() {
    fun getFollowers():MutableLiveData<List<String>> {
        return followRepository.getFollowers()
    }

    fun increasingConnections(): MutableLiveData<Boolean> {
        return followRepository.increasingConnections()
    }

    fun follow(fid: String):LiveData<Boolean>{
        return followRepository.follow(fid)
    }

    fun sendRequest(fid:String):MutableLiveData<Boolean>{
        return followRepository.sendRequest(fid)
    }

    fun getRequests(): MutableLiveData<List<String>>{
        return followRepository.getRequests()
    }
}