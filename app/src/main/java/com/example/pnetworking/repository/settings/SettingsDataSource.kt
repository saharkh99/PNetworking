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


class SettingsDataSource {
    private fun updateEmail(email:String,password:String,email2:String){
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
    private fun updatePassword(email:String,password:String,password2:String){
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
    private fun getBlackList(): MutableLiveData<List<User>>{
        val users = MutableLiveData<List<User>>()
        val uid = FirebaseAuth.getInstance().uid!!
        val l = ArrayList<User>()
        val ref =
            FirebaseDatabase.getInstance().getReference("/black_lists/$uid/")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val ref = FirebaseDatabase.getInstance().getReference("/users/${it}")
                    ref.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                                Log.d("NewMessage", it.toString())
                                val user = it.getValue(User::class.java)
                                if (user != null) {
                                    l.add(user)
                                }

                        }

                        override fun onCancelled(p0: DatabaseError) {

                        }
                    })
                }
                users.value = l
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
        return users
    }

}