package com.example.pnetworking.repository.signup

import android.net.Uri
import androidx.lifecycle.MutableLiveData

class SignUpRepoImpl(private val signUpDataSource: SignUpDataSource) : SignUpRepository {
    override fun signup(email: String, password: String): MutableLiveData<Boolean> {
        return signUpDataSource.signup(email,password)
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