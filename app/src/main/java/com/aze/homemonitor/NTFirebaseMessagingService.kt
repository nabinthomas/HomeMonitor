package com.aze.homemonitor

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject


class NTFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        private const val TAG = "NTFirebaseMessagingService"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.from)

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            if ( /* Check if data needs to be processed by long running job */true) {

            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body)
            val intent = Intent()
            intent.action = "com.aze.homemonitor.STATUS_NOTIFICATION"
            intent.putExtra("HomeMonitorStatusUpdateJson", remoteMessage.notification!!.body.toString())
            // sendBroadcast(intent)

            var notificationMessageBody : String = ""
            try{
                val statusJson = JSONObject(remoteMessage.notification!!.body.toString())

                var temperature = statusJson.get("temperature")
                var humidity = statusJson.get("humidity")
                var lastMotionDetected = statusJson.get("lastMotionDetected")
                var lastSoundDetected = statusJson.get("lastSoundDetected")

                notificationMessageBody = "Alert Received from Home Monitor\n "
                "Temperature = $temperature °F\n Humidity = $humidity %\n" +
                        "Movement Detected @ $lastMotionDetected \n" +
                        "Sound Detected @ $lastSoundDetected"
            }
            catch (e: Exception) {
                notificationMessageBody = remoteMessage.notification!!.body.toString()
            }

            val notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
            notificationManager.sendNotification(notificationMessageBody, applicationContext)
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        super.onNewToken(token)
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
        createNotificationChannel()

    }

    fun sendRegistrationToServer(token: String) {
        Log.d(TAG, "Sending Token to Server : $token")
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "NTFirebaseMessagingService"
            val descriptionText = "Notifications for Home Monitor"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel (name, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
