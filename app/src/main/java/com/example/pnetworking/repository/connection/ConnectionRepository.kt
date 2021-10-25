package com.example.pnetworking.repository.connection

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pnetworking.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class ConnectionRepository {
    val users = MutableLiveData<List<User>>()
    val l = ArrayList<User>()
    fun fakeUsers(): MutableLiveData<List<User>> {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        l.add(user)
                    }
                }
                users.value = l
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
        return users
    }
    fun closestUsers(lan:String,lon:String,con:String):MutableLiveData<ArrayList<String>>{
        val users = MutableLiveData<ArrayList<String>>()
        val users_string = ArrayList<String>()
        val uid = FirebaseAuth.getInstance().uid!!
        val l=ArrayList<Double>()
        val h=HashMap<String,Any>()
        val distances=ArrayList<Float>()
        val ref = FirebaseDatabase.getInstance().getReference("/vectors/location/$uid")
        ref.setValue(con).addOnSuccessListener {
            Log.d("TAG", "Saved our chat message: ${ref.key}")
        }

        val ref4 = FirebaseDatabase.getInstance().getReference("/vectors/$uid")
        ref4.addListenerForSingleValueEvent(object :ValueEventListener{
            val v: Vector<Double> = Vector<Double>(4)
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { va->
                    v.add(va.value.toString().toDouble())

                }
                h[snapshot.key.toString()]=v
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })

        val ref2 = FirebaseDatabase.getInstance().getReference("/vectors/location/")
        ref2.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    if(con==it.value.toString()){
                        val fid=it.key.toString()
                        val ref3 = FirebaseDatabase.getInstance().getReference("/vectors/$fid")
                        ref3.addListenerForSingleValueEvent(object :ValueEventListener{
                            val v: Vector<Double> = Vector<Double>(4)
                            override fun onDataChange(snapshot: DataSnapshot) {
                                Log.d("NewMessage", ref3.ref.toString())
                                snapshot.children.forEach { va->
                                    Log.d("NewMessage",va.key.toString())
                                    v.add(va.value.toString().toDouble())
                                }
                                Log.d("NewMessage",v.toString())
                                Log.d("NewMessage",h[uid].toString())
                                val c=cosineSimilarity(h[uid] as Vector<Double>,v)
                                Log.d("NewMessage",c.toString())
                                if(c>0.5){
                                    h[snapshot.key.toString()]=v
                                    Log.d("NewMessage",snapshot.key.toString())
                                    if(uid!=snapshot.key.toString())
                                      users_string.add(snapshot.key.toString())
                                }
                                users.value=users_string
                            }
                            override fun onCancelled(error: DatabaseError) {

                            }

                        })


                    }
                }
                distances.sort()
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
        return users
    }
    private fun distance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
    ): Float {
        val loc1 = Location("")
        loc1.latitude = lat1
        loc1.longitude = lon1

        val loc2 = Location("")
        loc2.latitude = lat2
        loc2.longitude = lon2

        return loc1.distanceTo(loc2)
    }

    fun cosineSimilarity(vectorA: Vector<Double>, vectorB: Vector<Double>): Double {
        var dotProduct = 0.0
        var normA = 0.0
        var normB = 0.0
        for (i in vectorA.indices) {
            dotProduct += vectorA[i] * vectorB[i]
            normA += Math.pow(vectorA[i], 2.0)
            normB += Math.pow(vectorB[i], 2.0)
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB))
    }
}