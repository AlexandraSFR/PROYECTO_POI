package com.example.pantalla_registro.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantalla_registro.models.Chat
import com.example.pantalla_registro.adapters.ChatAdapter
import com.example.pantalla_registro.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_list_of_chats.*

class ListOfChatsActivity : AppCompatActivity() {
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
        gotoChats.setOnClickListener { newChat() }
        GroupChats.setOnClickListener{ gotogroups()}
        profileButton.setOnClickListener{ profile() }

        listChatsRecyclerView.layoutManager = LinearLayoutManager(this)
        listChatsRecyclerView.adapter =
            ChatAdapter { chat ->
                chatSelected(chat)
            }

        val userRef = db.collection("users").document(user)

        userRef.collection("chats")
            .get()
            .addOnSuccessListener { chats ->
                val listChats = chats.toObjects(Chat::class.java)

                (listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)
            }

        userRef.collection("chats")
            .addSnapshotListener { chats, error ->
                if(error == null){
                    chats?.let {
                        val listChats = it.toObjects(Chat::class.java)

                        (listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)
                    }
                }
            }


    }

    private fun profile() {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)

        finish()
    }

    private fun chatSelected(chat: Chat){
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatId", chat.id)
        intent.putExtra("user", user)
        intent.putExtra("user1",chat.users[0])
        intent.putExtra("user2",chat.users[1])
        startActivity(intent)
    }

    private fun newChat(){
        val intent = Intent(this, CreateChatActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)

        finish()
    }

    private fun logOut(){

        FirebaseAuth.getInstance().signOut()
        db.collection("users").document(user).update("status","Offline")
        val intent = Intent(this,LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private  fun gotogroups(){
        val intent = Intent(this, ListOfGroupsActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)

        finish()
    }

    override fun onPause() {
        super.onPause()
        db.collection("users").document(user).update("status","Offline")

    }

    override fun onResume(){
        super.onResume()
        db.collection("users").document(user).update("status","Online")


    }
}