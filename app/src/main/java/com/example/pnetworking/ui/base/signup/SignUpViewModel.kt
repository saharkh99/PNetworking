package com.example.pnetworking.ui.base.signup

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.repository.auth.AuthRepository
import com.example.pnetworking.utils.ChatViewmodel

class SignUpViewModel(private val signUpRepository: AuthRepository):ChatViewmodel() {

    fun signup(email: String, password: String):MutableLiveData<String>{
       return signUpRepository.signup(email, password)
    }
    fun uploadImageToFirebaseStorage(selectedPhotoUri: Uri): MutableLiveData<String>{
        return signUpRepository.uploadImageToFirebaseStorage(selectedPhotoUri)
    }
    fun saveUserToFirebaseDatabase( email: String, birth: String, gen: String, profileImageUrl: String,name: String): MutableLiveData<Boolean>{
        return signUpRepository.saveUserToFirebaseDatabase(email, birth, gen, profileImageUrl,name)
    }
}