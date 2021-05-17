package com.example.pnetworking.repository.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi

class NotificcationVesionMore(val base: Context): ContextWrapper(base) {
    val id:String="id"
    val name:String="networking"
    lateinit var notificationManager:NotificationManager

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(){
        val notificatoinChannel=NotificationChannel(id,name,NotificationManager.IMPORTANCE_DEFAULT)
        notificatoinChannel.enableLights(true)
        notificatoinChannel.enableVibration(true)
        notificatoinChannel.lockscreenVisibility=Notification.VISIBILITY_PRIVATE
        getManager().createNotificationChannel(notificatoinChannel)
    }
    fun getManager():NotificationManager{
//        if(notificationManager==null)
            notificationManager= getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getNotification(title:String, body:String, sound: Uri, icon:String):Notification.Builder{
        return Notification.Builder(applicationContext,id)
//            .setContentIntent(pendingIntent)
            .setContentTitle(title).setContentText(body)
            .setSound(sound).setAutoCancel(true).setSmallIcon(icon.toInt())
    }
}