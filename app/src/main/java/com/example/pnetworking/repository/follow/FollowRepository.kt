package com.example.pnetworking.repository.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface FollowRepository {
    fun getFollowers():MutableLiveData<List<String>>
    fun increasingConnections(): MutableLiveData<Boolean>
    fun follow(fid: String): LiveData<Boolean>
    fun sendRequest(fid:String):MutableLiveData<Boolean>
    fun getRequests(): MutableLiveData<List<String>>
}