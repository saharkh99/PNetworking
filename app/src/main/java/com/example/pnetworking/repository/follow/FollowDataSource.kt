package com.example.pnetworking.repository.follow

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.Follow
import com.example.pnetworking.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FollowDataSource {

    fun follow(fid: String): MutableLiveData<Boolean> {
        var result = MutableLiveData<Boolean>()
        val uid = FirebaseAuth.getInstance().uid!!
       // if (uid != fid) {
            val ref =
                FirebaseDatabase.getInstance().getReference("/connection/$uid/$fid")
            val follow = Follow()
            with(follow) {
                idUSer = uid
                idFriend = fid

            }
            ref.setValue(follow).addOnSuccessListener {
                Log.d("TAG", "Finally we saved the connection to Firebase Database")
                result.value = true
            }
                .addOnFailureListener {
                    Log.d("2", "2")
                    Log.e("TAG", "Failed to set value to database: ${it.message}")
                    result.value = false
                }
            val ref1 = FirebaseDatabase.getInstance().getReference("/connection/$fid/$uid")
            val follow1 = Follow()
            with(follow1) {
                idUSer = fid
                idFriend = uid
            }
            ref1.setValue(follow).addOnSuccessListener {
                Log.d("TAG", "Finally we saved the connection to Firebase Database")
                result.value = true
                Log.d("result", result.value.toString())
            }
                .addOnFailureListener {
                    Log.d("2", "2")
                    Log.e("TAG", "Failed to set value to database: ${it.message}")
                    result.value = false
                }
            return result
      //  }
        return result
    }

    fun increasingConnections(fid: String): MutableLiveData<Boolean> {
        val uid = FirebaseAuth.getInstance().uid!!
        var result = MutableLiveData<Boolean>()
        val hashmap = HashMap<String, Any>()
        var con: Int = 0
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                con = user!!.connection
                hashmap.put("connection", con + 1)
                Log.d("con", con.toString())
                ref.updateChildren(hashmap).addOnSuccessListener {
                    Log.d("con", "connnnnnnn")
                    result.value = true
                }.addOnFailureListener {
                    result.value = false
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("error", p0.message)

            }
        })

        con = 0
        val ref1 = FirebaseDatabase.getInstance().getReference("/users/$fid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                con = user!!.connection
                hashmap.put("connection", con + 1)
                ref.updateChildren(hashmap).addOnSuccessListener {
                    Log.d("con", "connnnnnnn")
                    result.value = true
                }.addOnFailureListener {
                    result.value = false
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("error", p0.message)

            }
        })
        return result
    }

    fun getFollowers(): MutableLiveData<List<String>> {
        var result = MutableLiveData<List<String>>()
        val l = ArrayList<String>()
        val uid = FirebaseAuth.getInstance().uid!!
        val ref = FirebaseDatabase.getInstance().getReference("/connection/$uid")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        Log.d("users", it.key.toString())
                        l.add(it.key.toString())
                    }
                    result.value = l
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        return result
    }

    fun sendRequest(fid: String): MutableLiveData<Boolean> {
        val uid = FirebaseAuth.getInstance().uid!!
        var result = MutableLiveData<Boolean>()
        val ref = FirebaseDatabase.getInstance().getReference("/users/$fid/requests")
        val hashmap = HashMap<String, Any>()
        hashmap.put("user", uid)
        ref.setValue(hashmap).addOnSuccessListener {
            result.value = true
        }
        return result
    }

    fun getRequests(): MutableLiveData<List<String>> {
        val uid = FirebaseAuth.getInstance().uid!!
        var result = MutableLiveData<List<String>>()
        val l = ArrayList<String>()
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/requests")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    Log.d("users", it.value.toString())
                    l.add(it.value.toString())
                }
                result.value = l

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return result
    }

    fun deleteRequest(fid:String):MutableLiveData<Boolean>{
        var result = MutableLiveData<Boolean>()
        val ref = FirebaseDatabase.getInstance().getReference("/users/$fid/requests")
        ref.child("users").removeValue().addOnSuccessListener {
            Log.d("shod","shod")
        }
         return result
    }
}