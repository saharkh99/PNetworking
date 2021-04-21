package com.example.pnetworking.repository.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.repository.profile.ProfileDataStore
import com.example.pnetworking.repository.profile.ProfileRepository

class FollowRepoImpl(private val profileDataStore: FollowDataSource): FollowRepository {

    override fun getFollowers():MutableLiveData<List<String>> {
        return profileDataStore.getFollowers()
    }

    override fun increasingConnections(): MutableLiveData<Boolean> {
        return profileDataStore.increasingConnections()
    }

    override fun follow(fid: String): LiveData<Boolean> {
        return profileDataStore.follow(fid)
    }
}