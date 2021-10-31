package com.example.pantalla_registro.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pantalla_registro.models.Chat
import com.example.pantalla_registro.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_new_chat.*
import java.util.*


class CreateChatActivity : AppCompatActivity() {

    private var user = ""
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat)

        intent.getStringExtra("user")?.let { user = it }

        if (user.isNotEmpty()){
            initViews()
        }



    }

    private fun initViews() {
        goToChats.setOnClickListener { gotoChats() }
        goToGroup.setOnClickListener{gotoGroups()}
        CreateChat.setOnClickListener{createChat()}
    }

    private fun createChat() {
        val chatId = UUID.randomUUID().toString()
        val otherUser = GroupName.text.toString()
        val users = listOf(user, otherUser)

        if(otherUser.isNotEmpty()) {

            val chat = Chat(
                id = chatId,
                name = "Chat with $otherUser",
                users = users
            )

            db.collection("chats").document(chatId).set(chat)
            db.collection("users").document(user).collection("chats").document(chatId).set(chat)
            db.collection("users").document(otherUser).collection("chats").document(chatId)
                .set(chat)

            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("chatId", chatId)
            intent.putExtra("user", user)
            startActivity(intent)

        }
        else{
            Toast.makeText(applicationContext, "Por favor ingrese un Email", Toast.LENGTH_SHORT).show()
        }
    }

    private fun gotoChats(){
        val intent = Intent(this, ListOfChatsActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)

        finish()
    }

    private  fun gotoGroups(){
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