package com.asilbek.messenger.RegisterLogin

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.asilbek.messenger.Messages.LatestMessagesActivity
import com.asilbek.messenger.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import android.os.Parcelable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.asilbek.messenger.Messages.LatestMessagesActivity.Companion.currentuser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.custom_toast.*

import kotlinx.android.synthetic.main.custom_toast.view.*



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

//              fun fetchCurrentUser(){
//                val uid = FirebaseAuth.getInstance().uid
//                val ref= FirebaseDatabase.getInstance().getReference("/users/$uid")
//                ref.addListenerForSingleValueEvent(object: ValueEventListener {
//
//                    override fun onDataChange(p0: DataSnapshot) {
//                        LatestMessagesActivity.currentuser =p0.getValue(User::class.java)
//                        Log.d("LatestMessages","Current user ${LatestMessagesActivity.currentuser?.username}")
//
//                    }
//                    override fun onCancelled(p0: DatabaseError) {
//
//                    }
//                })
//            }

            Log.d("Login", "Attempt login with email/password: $email/****")


            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {


                    if (!it.isSuccessful) return@addOnCompleteListener
                    //else if successful

                    Log.d("Login", "Successfully Signed In user: ${it.result?.user?.uid}")






                    //-------------------Dialog box


                    //Get username from Database



//                    accDialogName.text= currentUser?.username




                    val mDialogView=LayoutInflater.from(this).inflate(R.layout.custom_toast,null)



                    //Alert Dialog Builder:
                    val mBuilder=AlertDialog.Builder(this)
                        .setView(mDialogView)

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