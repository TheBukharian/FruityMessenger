package com.asilbek.messenger.RegisterLogin

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.asilbek.messenger.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val your_Layout2 = findViewById<LinearLayout>(R.id.login_container)
        val animationDrawable2 = your_Layout2.background as AnimationDrawable
        animationDrawable2.setEnterFadeDuration(4000)
        animationDrawable2.setExitFadeDuration(4000)
        animationDrawable2.start()

backText.setOnClickListener {
    Log.d("RegisterActivity","We are back!")

    // Launch the main activity

    finish()
}

        loginBtn.setOnClickListener {
            val email=EmailLogin.text.toString()
            val password=PasswordLogin.text.toString()
            Log.d("Login","Attempt login with email/password: $email/****")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
               .addOnCompleteListener{

                   if (!it.isSuccessful) return@addOnCompleteListener
                   //else if successful

                   Log.d("Login","Successfully Signed In user: ${it.result?.user?.uid}")




               }
                .addOnFailureListener{
                    Log.d("Login","Failed to Sign in User: ${it.message}")
                }


        }
    }
}