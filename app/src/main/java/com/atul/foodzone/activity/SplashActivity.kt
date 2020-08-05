package com.atul.foodzone.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.atul.foodzone.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val activity = Intent(this@SplashActivity,
                LoginActivity::class.java)
            startActivity(activity)
        },2000)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}