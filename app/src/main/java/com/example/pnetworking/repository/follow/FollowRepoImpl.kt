package com.example.pnetworking.repository.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FollowRepoImpl(private val followDataSource: FollowDataSource): FollowRepository {

    override fun getFollowers():MutableLiveData<List<String>> {
        return followDataSource.getFollowers()
    }

    override fun increasingConnections(): MutableLiveData<Boolean> {
        return followDataSource.increasingConnections()
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
}