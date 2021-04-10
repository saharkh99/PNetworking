package com.example.pnetworking.repository.auth

import android.net.Uri
import androidx.lifecycle.MutableLiveData

class AuthRepoImpl(private val signUpDataSource: AuthDataSource) : AuthRepository {
    override fun signup(email: String, password: String): MutableLiveData<String> {
        return signUpDataSource.signup(email,password)
    }

    override fun signin(email: String, password: String): MutableLiveData<String> {
        return signUpDataSource.signin(email,password)
    }

    override fun uploadImageToFirebaseStorage(selectedPhotoUri: Uri): MutableLiveData<String> {
       return signUpDataSource.uploadImageToFirebaseStorage(selectedPhotoUri)
    }

    override fun saveUserToFirebaseDatabase(
        email: String,
        birth: String,
        gen: String,
        profileImageUrl: String
    ): MutableLiveData<Boolean> {
        return signUpDataSource.saveUserToFirebaseDatabase(email, birth, gen, profileImageUrl)
    }

}