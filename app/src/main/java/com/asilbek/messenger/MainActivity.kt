package com.asilbek.messenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.drawable.AnimationDrawable
import android.util.Log
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val your_Layout = findViewById<LinearLayout>(R.id.main_container)
        val animationDrawable = your_Layout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(4000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()



        RegBtn.setOnClickListener {
            val email=EmailText.text.toString()
            val password=PasswordText.text.toString()

            Log.d("MainActivity","Email is: $EmailText")
            Log.d("MainActivity","Pasword: $PasswordText")
        }


        Already.setOnClickListener {
            Log.d("MainActivity","Try to show login activity")
        }
    }
}
