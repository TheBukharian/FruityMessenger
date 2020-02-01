package com.asilbek.messenger.models

import com.asilbek.messenger.R
import com.asilbek.messenger.RegisterLogin.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.latest_messages_row.view.*


class LatestMessageRow(val chatMessage: ChatMessage,val lastTime:ChatMessage): Item<GroupieViewHolder>(){
    var chatPartnerUser:User?=null


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.message_textView_latestmessage.text=chatMessage.text
        viewHolder.itemView.lastMessageTime.text=chatMessage.time

        val chatPartner:String
        if(chatMessage.fromId==(FirebaseAuth.getInstance().uid)){
            chatPartner=chatMessage.toId
        }else{
            chatPartner=chatMessage.fromId
        }


        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartner")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                chatPartnerUser=p0.getValue(User::class.java)
                viewHolder.itemView.username_textView_latestmessage.text=chatPartnerUser?.username

                val targetImageView=viewHolder.itemView.imageview_latestmessage
                Picasso.get().load(chatPartnerUser?.profileImage).into(targetImageView)
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
    override fun getLayout(): Int {

        return R.layout.latest_messages_row
    }
}