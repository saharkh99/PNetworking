package com.example.pnetworking.repository.settings

import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.User

interface SettingsRepository {
    fun updateEmail(email: String, password: String, email2: String)
    fun updatePassword(email: String, password: String, password2: String)
    fun getBlackList(): MutableLiveData<List<User>>
    fun signOut()
    fun unblock(fid:String)
}