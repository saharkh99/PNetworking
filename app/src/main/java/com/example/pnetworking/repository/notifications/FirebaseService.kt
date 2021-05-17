package com.example.pnetworking.repository.notifications

import com.example.pnetworking.models.Token
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class FirebaseService:FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val user=FirebaseAuth.getInstance().currentUser
        val tokenRefresh=FirebaseInstanceId.getInstance().getToken()
        if(user!=null){
            updateToken(tokenRefresh!!)
        }
    }

    private fun updateToken(tokenRefresh: String) {
        val user=FirebaseAuth.getInstance().currentUser
        val ref=FirebaseDatabase.getInstance().getReference("token")
        val token= Token(tokenRefresh)
        ref.child(user.uid).setValue(token)
    }

}