package com.asilbek.messenger.Messages

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.asilbek.messenger.R
import com.asilbek.messenger.RegisterLogin.User
import com.asilbek.messenger.models.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class ChatLogActivity : AppCompatActivity() {


    companion object{
        val TAG = "ChatLog"
    }

    val adapter =GroupAdapter<GroupieViewHolder>()
    var toUser:User?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        recyclerview_chat_log.adapter=adapter

        toUser =intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title=toUser?.username


        listenForMessages()

        sendBtn_chatlog.setOnClickListener {
Log.d(TAG,"Attempt to send a messsage...")
            performSendMessage()

        }
    }

    private fun listenForMessages(){
        val fromId=FirebaseAuth.getInstance().uid
        val toId=toUser?.uid


        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
              val  chatMessage=  p0.getValue(ChatMessage::class.java)


                if (chatMessage!=null) {
                    Log.d(TAG, chatMessage.text)

                    if(chatMessage.fromId==FirebaseAuth.getInstance().uid){
                        val currentUser=LatestMessagesActivity.currentuser?:return
                        adapter.add(ChatFromItem(chatMessage.text,currentUser!!))


                    }
                    else{
                        adapter.add(ChatToItem(chatMessage.text,toUser!!))



                    }
                }

                //Scroll to the bottom of list:
                recyclerview_chat_log.scrollToPosition(adapter.itemCount-1)
            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }

    private  fun performSendMessage(){

        //Here we will send messages to Firebase...


      val text= editText_chat_log.text.toString()
        val fromId =FirebaseAuth.getInstance().uid
        val user =intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        val toId=user.uid

        if(fromId==null)return

        val reference=FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toReference=FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage= ChatMessage(reference.key!!,text,fromId,toId,System.currentTimeMillis()/1000)


        if(!TextUtils.isEmpty(text)) {
            reference.setValue(chatMessage)
                .addOnSuccessListener {
                    Log.d(TAG, "Saved our chat message: ${reference.key}")
                    editText_chat_log.text.clear()
                    recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)

                }

            toReference.setValue(chatMessage)


            val latestMessagesRef =
                FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
            latestMessagesRef.setValue(chatMessage)
        }
        else{
            Log.d(TAG, "Empty string")

        }
    }
}


class ChatFromItem(val text1:String,val user:User):Item<GroupieViewHolder>(){
    @RequiresApi(Build.VERSION_CODES.O)
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_from_row.text=text1

        val time =LocalTime.now()
        viewHolder.itemView.time_from_row.text=time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))


        val uri=user.profileImage
        val targetImageView=viewHolder.itemView.imageView_chat_from_row
        Picasso.get().load(uri).into(targetImageView)
    }
    override fun getLayout(): Int {

return R.layout.chat_from_row

    }

}
class ChatToItem(val text2:String,val user:User):Item<GroupieViewHolder>(){
    @RequiresApi(Build.VERSION_CODES.O)
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_to_row.text=text2

        val time =LocalTime.now()
        viewHolder.itemView.time_to_row.text=time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

        //load our image into star
        val uri=user.profileImage
        val targetImageView=viewHolder.itemView.imageView_chat_to_row
        Picasso.get().load(uri).into(targetImageView)
    }
    override fun getLayout(): Int {
        return R.layout.chat_to_row

    }

}