package com.example.pnetworking.ui.profile


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.repository.follow.FollowRepository
import com.example.pnetworking.utils.ChatViewmodel

class FollowViewModel(private val followRepository: FollowRepository): ChatViewmodel() {
    companion object{
        val changeConnection=MutableLiveData<Boolean>()
    }
    fun getFollowers():MutableLiveData<List<String>> {
        return followRepository.getFollowers()
    }

    fun increasingConnections(fid: String): MutableLiveData<Boolean> {
        return followRepository.increasingConnections(fid)
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
    fun deleteRequest(fid:String):MutableLiveData<Boolean>{
        return followRepository.deleteRequest(fid)
    }
    fun checkFriendship(fid:String):MutableLiveData<Boolean>{
        return followRepository.checkFriendship(fid)
    }
    fun disconnect(fid:String):MutableLiveData<Boolean>{
        return followRepository.disconnect(fid)
    }
    fun decreaseConnections(fid: String): MutableLiveData<Boolean>{
        return followRepository.decreaseConnections(fid)
    }
    fun changeConnection(b:Boolean){
        changeConnection.value=b
    }
    fun getStatusConnection():MutableLiveData<Boolean>{
        return changeConnection
    }
}