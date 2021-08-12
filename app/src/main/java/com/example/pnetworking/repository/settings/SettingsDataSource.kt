package com.example.pnetworking.repository.settings

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.User
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class SettingsDataSource {
     fun updateEmail(email:String,password:String,email2:String){
        val user = FirebaseAuth.getInstance().currentUser
        val credential = EmailAuthProvider
            .getCredential(email, password)
        user.reauthenticate(credential)
            .addOnCompleteListener {
                Log.d("TAG", "User re-authenticated.")
                val user = FirebaseAuth.getInstance().currentUser
                user.updateEmail(email2)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("TAG", "User email address updated.")
                        }
                    }
            }
    }
     fun updatePassword(email:String,password:String,password2:String){
        val user = FirebaseAuth.getInstance().currentUser
        val credential = EmailAuthProvider
            .getCredential(email, password)
        user.reauthenticate(credential)
            .addOnCompleteListener {
                Log.d("TAG", "User re-authenticated.")
                val user = FirebaseAuth.getInstance().currentUser
                user.updatePassword(password2)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("TAG", "User email address updated.")
                        }
                    }
            }
    }
     fun getBlackList(): MutableLiveData<List<User>>{
        val users = MutableLiveData<List<User>>()
        val uid = FirebaseAuth.getInstance().uid!!
        val l = ArrayList<User>()
        val ref =
            FirebaseDatabase.getInstance().getReference("/black_lists/$uid/")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    Log.d("NewMessage", (it.value.toString().removePrefix(uid)))
                    val fid= (it.value.toString().removePrefix(uid))
                    val ref = FirebaseDatabase.getInstance().getReference("/users/${fid}")
                    ref.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                                Log.d("NewMessage", ref.key.toString())
                                val user = p0.getValue(User::class.java)
                                if (user != null) {
                                    l.add(user)
                                    Log.d("NewMessage",user.name)

                                }
                            users.value = l
                        }

                        override fun onCancelled(p0: DatabaseError) {

                        }
                    })
                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

         return users
    }
     fun signOut(){
         FirebaseAuth.getInstance().signOut()
     }
    fun unblock(fid:String){
        val users = MutableLiveData<List<User>>()
        val uid = FirebaseAuth.getInstance().uid!!
        val ref =
            FirebaseDatabase.getInstance().getReference("/black_lists/$uid/")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                     if(it.getValue(true)==fid){
                         ref.child("fid").removeValue()
                     }
                    }
                }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
}