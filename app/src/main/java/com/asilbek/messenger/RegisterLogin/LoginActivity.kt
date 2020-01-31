package com.asilbek.messenger.RegisterLogin

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.asilbek.messenger.Messages.LatestMessagesActivity
import com.asilbek.messenger.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener




class LoginActivity :AppCompatActivity() {
    companion object {
        var currentuserName: User? = null
    }

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

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                if(TextUtils.isEmpty(email)) {
                    EmailLogin.error = "Can`t be empty!"
                }
                else {
                    PasswordLogin.error = "Can`t be empty!"
                }

            } else {


                Log.d("Login", "Attempt login with email/password: $email/****")

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {


                        if (!it.isSuccessful) return@addOnCompleteListener

//                        else if successful


                        //-------------------Welcome Dialog box---------------------------------

                        val mDialogView = AlertDialog.Builder(this@LoginActivity)


                        val uid = FirebaseAuth.getInstance().uid
                        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
                        ref.addValueEventListener(object : ValueEventListener {


                            override fun onDataChange(p0: DataSnapshot) {

                                currentuserName = p0.getValue(User::class.java)

                                mDialogView.setTitle("" + currentuserName?.username)
                                mDialogView.setIcon(R.mipmap.ic_launcher)
                                mDialogView.setMessage("Welcome Back!")


                                val mAlertD = mDialogView.create()
                                mAlertD.show()

                                Log.d(
                                    "LoginMessages",
                                    "Current user for Dialog box:  ${currentuserName?.username}"
                                )
                            }

                            override fun onCancelled(p0: DatabaseError) {

                            }
                        })

                        mDialogView.setPositiveButton("Continue") { _, _ ->
                            finish()
                            val intent = Intent(this, LatestMessagesActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                        Log.d(
                            "Login", "Successfully Signed In user: ${it.result?.user?.uid}"
                        )
                    }


                    .addOnFailureListener {
                        Log.d("Login", "Failed to Sign in User: ${it.message}")
                        Toast.makeText(this, "Failed! Use correct email or password and Check Network Connection!", Toast.LENGTH_LONG)
                            .show()

                    }
            }
        }
    }
}
