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
    fun getUID(): MutableLiveData<String?>  {
        var result = MutableLiveData<String?>()
        val uid = FirebaseAuth.getInstance().uid
        result.value=uid
        return result
    }
     fun fetchCurrentUser(uid:String):MutableLiveData<User> {
        var result = MutableLiveData<User>()
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                result.value = p0.getValue(User::class.java)
                Log.d("re",p0.getValue(User::class.java)!!.id)
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("error",p0.message)

            }
        })
        return result
    }
    fun editProfile(name:String,bio:String,uid: String):MutableLiveData<Boolean>{
        var result = MutableLiveData<Boolean>()
        val hashmap = HashMap<String, Any>()
        hashmap.put("name", name)
        hashmap.put("bio", bio)
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.updateChildren(hashmap).addOnSuccessListener {
            result.value=true
        }.addOnFailureListener {
            result.value=false
        }
        return result
    }
}