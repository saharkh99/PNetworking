package com.example.pnetworking.ui.features

import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.User
import com.example.pnetworking.repository.settings.SettingsRepository
import com.example.pnetworking.utils.ChatViewmodel

class SettingsViewModel(val settingsRepository: SettingsRepository):ChatViewmodel() {
     fun updateEmail(email: String, password: String, email2: String) {
         settingsRepository.updateEmail(email, password, email2)
    }

     fun updatePassword(email: String, password: String, password2: String) {
         settingsRepository.updatePassword(email, password, password2)
    }

     fun getBlackList(): MutableLiveData<List<User>> {
        return settingsRepository.getBlackList()
    }
}