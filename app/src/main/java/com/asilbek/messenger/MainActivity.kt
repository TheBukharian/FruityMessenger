package com.asilbek.messenger

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.drawable.AnimationDrawable
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val your_Layout = findViewById<LinearLayout>(R.id.main_container)
        val animationDrawable = your_Layout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(4000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()




        RegBtn.setOnClickListener {

            performRegistter()


        }
        Already.setOnClickListener {
            //This will underline a text
            Already.setPaintFlags(Already.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            Already.setText("Already have an account?")


            Log.d("MainActivity", "Try to show login activity")


            // Launch the login activity somehow

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }



    private fun performRegistter(){
        val email = EmailText.text.toString()
        val password = PasswordText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please Enter the Email and Password!", Toast.LENGTH_LONG)
                .show()
            return
        }

        Log.d("MainActivity", "Email is: $email")
        Log.d("MainActivity", "Pasword: $password")

        //Firebase Authentification to create a user with an account

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // Else if successful

                Log.d("Main", "Successfully created user with uid: ${it.result?.user?.uid}")

            }
            .addOnFailureListener {
                Log.d("Main", "Failed to create a user: ${it.message}")
                Toast.makeText(this, "Failed!Check Network Connection!", Toast.LENGTH_LONG)
                    .show()
            }
    }
}
