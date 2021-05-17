package com.example.pnetworking.repository.notifications

import android.util.Log
import com.example.pnetworking.R
import com.example.pnetworking.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody


class NotificationDataSource() {
    val ap=Client.buildService(ApiService::class.java)
    fun sendNotification(msg: Message,toChat:String,notify:Boolean){
        val fromId = FirebaseAuth.getInstance().uid
        val database=FirebaseDatabase.getInstance().getReference("/chat/$toChat/message/$fromId/")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                  Log.d("change","change")
                  if(notify)
                      sendNotifications(msg.idTo,msg.context,fromId!!)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("cancel",error.message)
            }

        })
    }

    private fun sendNotifications(idTo: String, context: String,fromId:String) {
             val allTokens=FirebaseDatabase.getInstance().getReference("token")
             val query=allTokens.orderByKey().equalTo(fromId)
             Log.d("query",query.toString())
             query.addValueEventListener(object :ValueEventListener{
                 override fun onDataChange(snapshot: DataSnapshot) {
                     Log.d("query",snapshot.key!!)
                     for ( db in snapshot.children){
                         val token=db.getValue(Token::class.java)
                         val data=Data(fromId,context,"message",idTo, R.drawable.user)
                         val sender=Sender(data,token!!.token)
                         ap.postNotification(sender).subscribeOn(Schedulers.io())
                             .observeOn(AndroidSchedulers.mainThread())
                             .subscribe(object :SingleObserver<ResponseBody>{
                                 override fun onSubscribe(d: Disposable) {
                                 }

                                 override fun onSuccess(t: ResponseBody) {
                                     Log.d("response","sent")
                                 }

                                 override fun onError(e: Throwable) {
                                     TODO("Not yet implemented")
                                 }

                             })
                     }
                 }

                 override fun onCancelled(error: DatabaseError) {
                     Log.d("cancel",error.details)

                 }

             })
    }
}