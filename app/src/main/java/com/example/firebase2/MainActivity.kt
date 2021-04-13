package com.example.firebase2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*button.setOnClickListener {
            throw RuntimeException("Test Crash")
        }*/


        // Device token is used to send test notifications
        //getFirebaseFCMDeviceToken()


        val imageUrl = getDataFromFirebaseCloudMessaging()
        imageUrl?.let { Log.i("image url", it) }
        if (imageUrl != null){
            showOnUI(imageUrl = imageUrl)
        }else{
            Toast.makeText(this,"The Notification does not contain a valid URL",Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Update the UI/Update the Image as per received URL
     */
    private fun showOnUI(imageUrl: String) {
        Glide.with(this).load(imageUrl).into(imageView)
    }


    private fun getDataFromFirebaseCloudMessaging(): String? {
        Log.i("FCM data", "${ intent.extras?.getString(MyFirebaseMessagingService.IMAGE_URL_KEY) }")
        return intent.extras?.getString(MyFirebaseMessagingService.IMAGE_URL_KEY)

    }

    private fun getFirebaseDeviceToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if(!task.isSuccessful){

                Log.e("Token" , "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Log.i("Token", token.toString())
        })
    }
}