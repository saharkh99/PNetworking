package com.example.pnetworking.repository.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface FollowRepository {
    fun getFollowers():MutableLiveData<List<String>>
    fun increasingConnections(fid: String): MutableLiveData<Boolean>
    fun follow(fid: String): LiveData<Boolean>
    fun sendRequest(fid:String):MutableLiveData<Boolean>
    fun getRequests(): MutableLiveData<List<String>>
    fun deleteRequest(fid:String):MutableLiveData<Boolean>
    fun checkFriendship(fid:String):MutableLiveData<Boolean>
    fun disconnect(fid:String):MutableLiveData<Boolean>
    fun decreaseConnections(fid: String): MutableLiveData<Boolean>
}