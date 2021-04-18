package com.example.pnetworking.repository.connection

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.pnetworking.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class ConnectionRepository {

     fun fakeUsers() :MutableLiveData<List<User>>{
        val users=MutableLiveData<List<User>>()
        val l= ArrayList<User>()
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        l.add(user as User)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
        users.value=l
        return users
    }
}