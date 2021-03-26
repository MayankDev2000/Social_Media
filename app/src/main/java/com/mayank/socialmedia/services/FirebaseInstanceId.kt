package com.mayank.socialmedia.services

import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mayank.socialmedia.R

class FirebaseInstanceId: FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        sendNotification(p0)
    }
    fun sendNotification(p1:RemoteMessage){
        val sound : Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val msg : RemoteMessage.Notification? = p1.notification
        var builder : NotificationCompat.Builder = NotificationCompat.Builder(this,"MyNotification")
            .setContentTitle(msg?.title.toString())
            .setContentText(msg?.body)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setSound(sound)
        val notificationManager :NotificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManager.notify(999,builder.build())
    }
}