package com.example.firebase2


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object{
        const val IMAGE_URL_KEY = "image_url"
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        Log.i("Messaging Service", "Message ID ${p0.messageId.toString()}")
        p0.notification.let {
            sendNotification(it?.title , it?.body , p0.data)
        }
    }


    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        Log.i("onNewToken", "Refreshed token: $p0")
    }


    private fun sendNotification(messageTitle: String?, messageBody: String?, data: MutableMap<String, String>) {

        val intent = Intent(this, MainActivity::class.java)

        intent.putExtra(IMAGE_URL_KEY, data[IMAGE_URL_KEY])
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this , 0 , intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = getString(R.string.default_notification_channel_Id)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder =  NotificationCompat.Builder(this,channelId)
                .setContentTitle(messageTitle).setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId,getString(R.string.channel_name),NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify( 0,notificationBuilder.build())
    }
}