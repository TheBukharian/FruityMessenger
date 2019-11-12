package com.asilbek.messenger.RegisterLogin

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.asilbek.messenger.Messages.LatestMessagesActivity
import com.asilbek.messenger.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import android.view.Gravity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.asilbek.messenger.Messages.LatestMessagesActivity.Companion.currentUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.custom_toast.*
import kotlinx.android.synthetic.main.custom_toast.view.*
import kotlinx.android.synthetic.main.user_row_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*


class LoginActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val your_Layout2 = findViewById<LinearLayout>(R.id.login_container)
        val animationDrawable2 = your_Layout2.background as AnimationDrawable
        animationDrawable2.setEnterFadeDuration(4000)
        animationDrawable2.setExitFadeDuration(4000)
        animationDrawable2.start()

        backText.setOnClickListener {
            Log.d("RegisterActivity", "We are back!")

            // Launch the main activity

            finish()
        }

        loginBtn.setOnClickListener {
            val email = EmailLogin.text.toString()
            val password = PasswordLogin.text.toString()
            Log.d("Login", "Attempt login with email/password: $email/****")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {

                    if (!it.isSuccessful) return@addOnCompleteListener
                    //else if successful
                    Log.d("Login", "Successfully Signed In user: ${it.result?.user?.uid}")




                    //-------------------Dialog box
                    val mDialogView=LayoutInflater.from(this).inflate(R.layout.custom_toast,null)

                    //Alert Dialog Builder:
                    val mBuilder=AlertDialog.Builder(this)
                        .setView(mDialogView)



                    //get Username from Firebase Database and reassign into TextView :




                    //Show Dialog:
                    val mAlertDialog=mBuilder.show()


                    mDialogView.continueBtn.setOnClickListener {

                        mAlertDialog.dismiss()
                        val intent = Intent(this, LatestMessagesActivity::class.java)
                        startActivity(intent)
                    }




                }

                .addOnFailureListener {
                    Log.d("Login", "Failed to Sign in User: ${it.message}")
                }



        }





    }



}