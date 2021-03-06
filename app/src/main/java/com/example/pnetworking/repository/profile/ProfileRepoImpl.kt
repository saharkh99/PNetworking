package com.example.pnetworking.repository.profile

import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.User

class ProfileRepoImpl(private val profileDataStore: ProfileDataStore):ProfileRepository {
    override fun getUID(): MutableLiveData<String?> {
       return profileDataStore.getUID()
    }

    override fun getCurrentUser(uid: String): MutableLiveData<User> {
       return profileDataStore.fetchCurrentUser(uid)
    }

    override fun editProfile(name: String, bio: String, uid: String,fav:String): MutableLiveData<Boolean> {
       return profileDataStore.editProfile(name, bio, uid,fav)
    }

    override fun updatePhoto(image: String): MutableLiveData<Boolean> {
        return profileDataStore.updatePhoto(image)
    }

    override fun saveFav(fav: String) {
        return profileDataStore.saveFav(fav)
    }


}