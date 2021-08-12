package com.example.pnetworking.repository.settings

import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.User

class SettingsRepoImpl(private val seetingDataSource: SettingsDataSource): SettingsRepository {
    override fun updateEmail(email: String, password: String, email2: String) {
        seetingDataSource.updateEmail(email, password, email2)
    }

    override fun updatePassword(email: String, password: String, password2: String) {
        seetingDataSource.updatePassword(email, password, password2)
    }

    override fun getBlackList(): MutableLiveData<List<User>> {
       return seetingDataSource.getBlackList()
    }

    override fun signOut() =seetingDataSource.signOut()
    override fun unblock(fid: String)=seetingDataSource.unblock(fid)
}