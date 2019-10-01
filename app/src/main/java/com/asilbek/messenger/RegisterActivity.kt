package com.asilbek.messenger

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


class RegisterActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val your_Layout = findViewById<LinearLayout>(R.id.main_container)
        val animationDrawable = your_Layout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(4000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()

        PhotoBtn.setOnClickListener {
            Log.d("RegisterActivity","Try to show photo")
            val intent=Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)


            //                //NEED TO CORRECT THIS PART FOR DELETING



//            Thread.sleep(1000)
//
//            val b = findViewById<View>(R.id.deleteImageBtn)
//            b.setVisibility(VISIBLE)
//            deleteImageBtn.setOnClickListener {
//

//
//                if (PhotoImageView!=null&& PhotoBtn!=null){ PhotoImageView.setImageResource(0)
//                    }
//
//                else {
//
//                    Toast.makeText(this, "No Image selected!", Toast.LENGTH_LONG)
//                        .show()
//                }
//
//
//            }

        }



        RegBtn.setOnClickListener {

            performRegistter()





        }
        Already.setOnClickListener {
            //This will underline a text
            Already.setPaintFlags(Already.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            Already.setText("Already have an account?")


            Log.d("RegisterActivity", "Try to show login activity")


            // Launch the login activity somehow

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    var selectedPhotoUri: Uri?=null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && resultCode== Activity.RESULT_OK && data!=null)
        {
            //proceed and check what the selected image was...
            Log.d("RegisterActivity","Photo was selected")

            selectedPhotoUri=data.data
             val bitmap=MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)

            PhotoImageView.setImageBitmap(bitmap)
            PhotoBtn.alpha=0f

            //val bitmapDrawable=BitmapDrawable(bitmap)
            //PhotoBtn.setBackgroundDrawable(bitmapDrawable)
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

        Log.d("RegisterActivity", "Email is: $email")
        Log.d("RegisterActivity", "Pasword: $password")

        //Firebase Authentification to create a user with an account

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // Else if successful

                Log.d("RegisterActivity", "Successfully created user with uid: ${it.result?.user?.uid}")
                uploadImgageToFirebaseStorage()
                Toast.makeText(this, "An account created successfully!", Toast.LENGTH_LONG)
                    .show()

                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Failed to create a user: ${it.message}")
                Toast.makeText(this, "Failed!Check Network Connection!", Toast.LENGTH_LONG)
                    .show()
            }
    }
    private fun uploadImgageToFirebaseStorage(){
        if (selectedPhotoUri==null)return
        val filename = UUID.randomUUID().toString()
        val ref=FirebaseStorage.getInstance().getReference("/images $filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity","Successfully added image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity","File Location: $it")
                    saveUserToFirebaseDatabase(it.toString())

                }
            }
            .addOnFailureListener {
                //do some logs here

            }

    }
    private fun saveUserToFirebaseDatabase(profileImageUrl: String){
        val uid =FirebaseAuth.getInstance().uid?:""
        val ref=FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user=User(uid,NameText.text.toString(),profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener{

                Log.d("RegisterActivity","Finally we saved the User to Firebase database!")
            }
            .addOnFailureListener {
                Log.d("RegisterActivity","Failed to save the User to Firebase database!")
            }
    }


}
class  User(val uid:String, val username:String,val profileImage: String)
