package com.example.pnetworking.ui.base.signin

import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.repository.auth.AuthRepository
import com.example.pnetworking.utils.ChatViewmodel

class SignInViewModel(private val authRepository: AuthRepository):ChatViewmodel() {

    fun signin(email: String, password: String): MutableLiveData<String> {
        return authRepository.signin(email, password)
    }
}