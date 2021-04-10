package com.example.pnetworking.repository.auth

import android.net.Uri
import androidx.lifecycle.MutableLiveData

interface AuthRepository {
    fun signup(email: String, password: String): MutableLiveData<String>
    fun signin(email: String, password: String): MutableLiveData<String>
    fun uploadImageToFirebaseStorage(selectedPhotoUri: Uri): MutableLiveData<String>
    fun saveUserToFirebaseDatabase( email: String, birth: String, gen: String, profileImageUrl: String): MutableLiveData<Boolean>
}