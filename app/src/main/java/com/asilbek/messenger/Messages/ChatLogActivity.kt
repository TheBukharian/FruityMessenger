package com.asilbek.messenger.Messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.asilbek.messenger.R
import com.asilbek.messenger.RegisterLogin.User
import com.asilbek.messenger.models.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import java.sql.Timestamp

class ChatLogActivity : AppCompatActivity() {


    companion object{
        val TAG = "ChatLog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

//        val username=intent.getStringExtra(NewMessageActivity.USER_KEY)
        val user =intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title=user.username

//        setupDummyData()

        listenForMessages()

        sendBtn_chatlog.setOnClickListener {
Log.d(TAG,"Attempt to send a messsage...")
            performSendMessage()

        }
    }
    private fun listenForMessages(){
        val ref = FirebaseDatabase.getInstance().getReference("/message")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

              val  chatMessage=  p0.getValue(ChatMessage::class.java)
                Log.d(TAG,chatMessage?.text)


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


        val reference=FirebaseDatabase.getInstance().getReference("/messages").push()

        val chatMessage= ChatMessage(reference.key!!,text,fromId!!,toId,System.currentTimeMillis()/1000)

        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG,"Saved our chat message: ${reference.key}")

            }


    }


    private  fun  setupDummyData(){
        val adapter =GroupAdapter<GroupieViewHolder>()

        adapter.add(ChatFromItem("From Messsssage!!!"))
        adapter.add(ChatToItem("This is the to row message that is longer"))
        adapter.add(ChatFromItem("From Messsssage!!!"))
        adapter.add(ChatToItem("This is the to row message that is longer"))
        adapter.add(ChatFromItem("From Messsssage!!!"))
        adapter.add(ChatToItem("This is the to row message that is longer"))


        recyclerview_chat_log.adapter=adapter
    }
}


class ChatFromItem(val text:String):Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.textView_from_row.text=text
    }
    override fun getLayout(): Int {
return R.layout.chat_from_row
    }

}
class ChatToItem(val text:String):Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.textView_to_row.text=text
    }
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

}