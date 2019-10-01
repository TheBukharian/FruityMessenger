package com.asilbek.messenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

supportActionBar?.title="Select User"

        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(UserItem())
        adapter.add(UserItem())
        adapter.add(UserItem())

        recyclerView_newMessage.adapter=adapter



    }
}

class UserItem:Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

// will be called in our list for each user object later...

    }

    override fun getLayout(): Int {
return R.layout.user_row_new_message
    }
}

