package com.example.pnetworking.repository.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FollowRepoImpl(private val followDataSource: FollowDataSource): FollowRepository {

    override fun getFollowers():MutableLiveData<List<String>> {
        return followDataSource.getFollowers()
    }

    override fun increasingConnections(fid:String): MutableLiveData<Boolean> {
        return followDataSource.increasingConnections(fid)
    }

    override fun follow(fid: String): LiveData<Boolean> {
        return followDataSource.follow(fid)
    }

    override fun sendRequest(fid: String): MutableLiveData<Boolean> {
        return followDataSource.sendRequest(fid)
    }

    override fun getRequests(): MutableLiveData<List<String>> {
        return followDataSource.getRequests()
    }

    override fun deleteRequest(fid: String): MutableLiveData<Boolean> {
        return followDataSource.deleteRequest(fid)
    }

    override fun checkFriendship(fid: String): MutableLiveData<Boolean> {
        return followDataSource.checkFriendship(fid)
    }

    override fun disconnect(fid: String): MutableLiveData<Boolean> {
        return followDataSource.disconnect(fid)
    }

    override fun decreaseConnections(fid: String): MutableLiveData<Boolean> {
        return followDataSource.decreaseConnections(fid)
    }
}