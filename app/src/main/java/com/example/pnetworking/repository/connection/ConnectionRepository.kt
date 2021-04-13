package com.example.pnetworking.repository.connection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.User

class ConnectionRepository {
    fun fakeUsers():LiveData<List<User>>{
        val users=MutableLiveData<List<User>>()
        var u=User()
        u.emailText="sahar@gmail.com"
        u.name="sahar"
        var u1=User()
        u1.emailText="sahar@gmail.com"
        u1.name="saha"
        val l= ArrayList<User>()
        l.add(u)
        l.add(u1)
        users.value=l
        return users
    }
}