package com.example.pnetworking.repository.notifications

import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class NotificationFirebaseMessage:FirebaseMessagingService(){

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.d("listen", "listen")
        val sp=getSharedPreferences("SP_USER", MODE_PRIVATE)
        val saveCurrentUser=sp.getString("CURRENT_USERID", "None")
        val sent= p0.data.get("sent")
        val user=p0.data.get("user")
        val fuser=FirebaseAuth.getInstance().currentUser
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notifificationForRecentVersion(p0)
        }
        else
            normalVersion(p0)

    }

    private fun normalVersion(p0: RemoteMessage) {
        val icon= p0.data.get("icon")
        var user=p0.data.get("user")
        val title=p0.data.get("title")
        val body=p0.data.get("body")
        val notification=p0.notification
//        val i=user?.replace("[\\D]", "")!!.toInt()
//        val intent=Intent(this,PrivateChat::class.java)
//        val bundle=Bundle()
//        bundle.putString("",user)
//         intent.putExtras(bundle)
        //intent.addFlag(clear_top)
        user=FirebaseAuth.getInstance().uid
//        val pIntent=PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT)
        val soundUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder=NotificationCompat.Builder(this).setSmallIcon(icon!!.toInt()).setContentText(
            body
        ).setAutoCancel(true)
            .setContentTitle(title).setSound(soundUri)
        val notificationManager:NotificationManager= getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val oneTimeID = SystemClock.uptimeMillis().toInt()
        notificationManager.notify(oneTimeID, builder.build())
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun notifificationForRecentVersion(p0: RemoteMessage) {
        val icon= p0.data.get("icon")
        var user=p0.data.get("user")
        val title=p0.data.get("title")
        val body=p0.data.get("body")
        val notification=p0.notification
//        val i=user?.replace("[\\D]", "")!!.toInt()
//        val intent=Intent(this,PrivateChat::class.java)
//        val bundle=Bundle()
//        bundle.putString("",user)
//         intent.putExtras(bundle)
        //intent.addFlag(clear_top)
        user=FirebaseAuth.getInstance().uid
//        val pIntent=PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT)
        val soundUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notification1= NotificcationVesionMore(this)
        val builder=notification1.getNotification(title!!, body!!, soundUri, icon!!)
        val notificationManager:NotificationManager= getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        Log.d("title",body)
        val oneTimeID = SystemClock.uptimeMillis().toInt()
        notification1.getManager().notify(oneTimeID, builder.build())
    }
}