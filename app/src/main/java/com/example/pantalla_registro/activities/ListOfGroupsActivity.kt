package com.example.pantalla_registro.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantalla_registro.models.GroupChat
import com.example.pantalla_registro.adapters.GroupChatAdapter
import com.example.pantalla_registro.R
import com.example.pantalla_registro.adapters.ChatAdapter
import com.example.pantalla_registro.models.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_list_of_groups.*
import kotlinx.android.synthetic.main.activity_list_of_groups.listChatsRecyclerView
import kotlinx.android.synthetic.main.activity_list_of_groups.logOutButton
import kotlinx.android.synthetic.main.activity_list_of_groups.newChatButton
import java.util.*

class ListOfGroupsActivity : AppCompatActivity() {
    private var user = ""

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_chats)

        intent.getStringExtra("user")?.let { user = it }

        if (user.isNotEmpty()){
            initViews()
        }
    }
    private fun initViews(){
        newChatButton.setOnClickListener { newChat() }
        logOutButton.setOnClickListener{ logOut() }

        listChatsRecyclerView.layoutManager = LinearLayoutManager(this)
        listChatsRecyclerView.adapter =
            GroupChatAdapter { chat ->
                chatSelected(chat)
            }

        val userRef = db.collection("users").document(user)

        userRef.collection("groups")
            .get()
            .addOnSuccessListener { groups ->
                val listChats = groups.toObjects(GroupChat::class.java)

                (listChatsRecyclerView.adapter as GroupChatAdapter).setData(listChats)
            }

        userRef.collection("groups")
            .addSnapshotListener { groups, error ->
                if(error == null){
                    groups?.let {
                        val listChats = it.toObjects(GroupChat::class.java)

                        (listChatsRecyclerView.adapter as GroupChatAdapter).setData(listChats)
                    }
                }
            }


    }

    private fun chatSelected(chat: GroupChat){
        val intent = Intent(this, GroupChatActivity::class.java)
        intent.putExtra("chatId", chat.id)
        intent.putExtra("chatName", chat.Nombre)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    private fun newChat(){
        val chatId = UUID.randomUUID().toString()
        val otherUser = newChatText.text.toString()
        val users = listOf(user, otherUser)

        val chat = Chat(
            id = chatId,
            name = "Chat with $otherUser",
            users = users
        )

        db.collection("chats").document(chatId).set(chat)
        db.collection("users").document(user).collection("chats").document(chatId).set(chat)
        db.collection("users").document(otherUser).collection("chats").document(chatId).set(chat)

        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatId", chatId)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    private fun logOut(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this,LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private  fun gotogroups(){
        val intent = Intent(this, GroupActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)

        finish()
    }


}