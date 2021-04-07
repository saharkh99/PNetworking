package com.example.pnetworking.repository.signup

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Question

interface SignUpRepository {
    fun signup(email: String, password: String): MutableLiveData<Boolean>
    fun uploadImageToFirebaseStorage(selectedPhotoUri: Uri): MutableLiveData<String>
    fun saveUserToFirebaseDatabase( email: String, birth: String, gen: String, profileImageUrl: String): MutableLiveData<Boolean>
}