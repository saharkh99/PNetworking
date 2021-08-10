package com.example.pnetworking.repository.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileDataStore {
    fun getUID(): MutableLiveData<String?> {
        val result = MutableLiveData<String?>()
        val uid = FirebaseAuth.getInstance().uid
        result.value = uid
        return result
    }

    fun fetchCurrentUser(uid: String): MutableLiveData<User> {
        val result = MutableLiveData<User>()
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                result.value = p0.getValue(User::class.java)
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("error", p0.message)

            }
        })
        return result
    }

    fun editProfile(name: String, bio: String, uid: String, fav: String): MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val hashmap = HashMap<String, Any>()
        hashmap["name"] = name
        hashmap["bio"] = bio
        hashmap["favorites"] = fav
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.updateChildren(hashmap).addOnSuccessListener {
            result.value = true
        }.addOnFailureListener {
            result.value = false
        }
        return result
    }

    fun updatePhoto(image: String): MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val hashmap = HashMap<String, Any>()
        hashmap["imageProfile"] = image
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.updateChildren(hashmap).addOnSuccessListener {
            result.value = true
        }.addOnFailureListener {
            result.value = false
        }
        return result
    }

}