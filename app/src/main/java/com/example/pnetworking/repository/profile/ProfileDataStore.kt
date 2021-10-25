package com.example.pnetworking.repository.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

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

    fun saveFav(fav:String){
        val favorites=fav.split(" ")
        val hashmap=HashMap<String,Any>()
        Log.d("fav",favorites.toString())
        val uid = FirebaseAuth.getInstance().uid
        favorites.forEach { f->
            when (f) {
                "book" -> {
                     hashmap["book"]=1
                }
                "environment" -> {
                    hashmap["environment"]=1
                }
                "technology" -> {
                    hashmap["technology"]=1
                }
                "cooking" -> {
                    hashmap["cooking"]=1
                }
                "movies" -> {
                    hashmap["movies"]=1
                }
                "music" -> {
                    hashmap["music"]=1
                }
                "dance" -> {
                    hashmap["dance"]=1
                }
                "travel" -> {
                    hashmap["travel"]=1
                }

            }
            val reference =
                FirebaseDatabase.getInstance().getReference("/vectors/$uid/")
            reference.addChildEventListener(object :ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    reference.updateChildren(hashmap).addOnSuccessListener {
                        Log.d("save","save")
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

        }
    }
}