package com.example.pnetworking.repository.auth

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AuthDataSource {
    fun signup(email: String, password: String): MutableLiveData<String> {
        val result = MutableLiveData<String>()
        Log.d("TAG", "Attempting to create user with email: $email")
        Log.d("email", email)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d("TAG", "Successfully created user with uid: ${it.result}")
                if (it.isSuccessful)
                    result.value = "true"
            }
            .addOnFailureListener {
                Log.d("TAG", "Failed to create user: ${it.message}")
                result.value = it.message.toString()
            }
        return result
    }

     fun uploadImageToFirebaseStorage(selectedPhotoUri: Uri): MutableLiveData<String> {
        val result = MutableLiveData<String>()
        if (selectedPhotoUri.path=="") {
            result.value="true"
            return result
        }

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri)
            .addOnSuccessListener {
                Log.d("TAG", "Successfully uploaded image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener { it1 ->
                    Log.d("TAG", "File Location: $it1")
                    result.value = it1.toString()
                }
            }
            .addOnFailureListener {
                Log.d("TAG", "Failed to upload image to storage: ${it.message}")
                result.value = "false"
            }
        return result
    }

     fun saveUserToFirebaseDatabase(
        email: String,
        birth: String,
        gen:String,
        profileImageUrl: String,
        names: String

    ): MutableLiveData<Boolean> {
        var result = MutableLiveData<Boolean>()
        val uid = FirebaseAuth.getInstance().currentUser.uid
        Log.d("uid", uid)
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        Log.d("ref", ref.key!!)
        val user = User()
        with(user) {
            id = uid
            emailText = email
            imageProfile = profileImageUrl
            connection = 0
            gender=gen
            name=names
            birthday=birth
            score="0"
        }
        ref.setValue(user).addOnSuccessListener {
            Log.d("TAG", "Finally we saved the user to Firebase Database")
            result.value = true
        }
            .addOnFailureListener {
                Log.e("TAG", "Failed to set value to database: ${it.message}")
                result.value = false
            }
        return result
    }

    fun signin(email: String,password: String):MutableLiveData<String>{
        var result = MutableLiveData<String>()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result.value="true"

                } else {
                    try {
                        result.value=task.result.toString()
                    } catch(e: FirebaseAuthInvalidCredentialsException) {
                        Log.d("exception",e.message!!)
                        result.value="invalid credential. the email or password is wrong"
                    }
                     catch(e: Exception) {
                        Log.d("exception",e.message!!)
                        result.value="something is wrong please try again"
                    }
                }
            }
        return result
    }
}