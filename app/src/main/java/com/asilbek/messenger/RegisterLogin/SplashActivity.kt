package com.asilbek.messenger.RegisterLogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.asilbek.messenger.Messages.LatestMessagesActivity
import com.asilbek.messenger.R

class SplashActivity : AppCompatActivity() {
    internal val TIME_OUT=6000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(
            {
            startActivity(Intent (this@SplashActivity,LatestMessagesActivity::class.java))
            finish()
        },TIME_OUT.toLong()
        )






        val image =findViewById<ImageView>(R.id.imageView4) as ImageView
        val logo =findViewById<ImageView>(R.id.imageView5) as ImageView
        val text =findViewById<TextView>(R.id.textView3) as TextView


        val animation: Animation = AnimationUtils.loadAnimation(this@SplashActivity,
            R.anim.zoom_in
        )
        val animation2: Animation = AnimationUtils.loadAnimation(this@SplashActivity,
            R.anim.fade_in
        )

        image.startAnimation(animation)
        logo.startAnimation(animation2)
        text.startAnimation(animation2)

    }
}
