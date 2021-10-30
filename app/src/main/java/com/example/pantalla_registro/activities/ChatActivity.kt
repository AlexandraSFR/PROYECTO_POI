package com.example.pantalla_registro.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantalla_registro.adapters.MessageAdapter
import com.example.pantalla_registro.R
import com.example.pantalla_registro.models.Message
import com.example.pantalla_registro.models.User
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_list_of_chats.*
import kotlinx.android.synthetic.main.item_group_contacts.*

class ChatActivity : AppCompatActivity() {
    private var chatId = ""
    private var user = ""
    private var user1 = ""
    private var user2 = ""

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        intent.getStringExtra("chatId")?.let { chatId = it }
        intent.getStringExtra("user")?.let { user = it }
        intent.getStringExtra("user1")?.let { user1 = it }
        intent.getStringExtra("user2")?.let { user2 = it }


        if(chatId.isNotEmpty() && user.isNotEmpty()) {
            initViews()
        }
    }

    private fun initViews(){

        if(user1==user){
            Name.setText(user2);

            db.collection("users").document(user2).get().addOnSuccessListener {
                val stat = it.toObject(User::class.java)
                if (stat != null) {
                    StatusChat.setText(stat.status)
                    if(stat.status=="Online"){
                        StatusChat.setTextColor((Color.GREEN))
                    }

                    if(stat.status=="Offline"){
                        StatusChat.setTextColor((Color.RED))
                    }
                }
            }

        }
        else{
            Name.setText(user1);
            db.collection("users").document(user1).get().addOnSuccessListener {
                val stat = it.toObject(User::class.java)
                if (stat != null) {
                    StatusChat.setText(stat.status)
                    if(stat.status=="Online"){
                        StatusChat.setTextColor((Color.GREEN))
                    }

                    if(stat.status=="Offline"){
                        StatusChat.setTextColor((Color.RED))
                    }
                }
            }
        }

        messagesRecylerView.layoutManager = LinearLayoutManager(this)
        messagesRecylerView.adapter = MessageAdapter(user)

        sendMessageButton.setOnClickListener { sendMessage() }
        Chats.setOnClickListener{gotoChats()}

        val chatRef = db.collection("chats").document(chatId)

        chatRef.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { messages ->
                val listMessages = messages.toObjects(Message::class.java)
                (messagesRecylerView.adapter as MessageAdapter).setData(listMessages)
            }

        chatRef.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .addSnapshotListener { messages, error ->
                if(error == null){
                    messages?.let {
                        val listMessages = it.toObjects(Message::class.java)
                        (messagesRecylerView.adapter as MessageAdapter).setData(listMessages)
                    }
                }
            }
    }

    private fun sendMessage(){
        val message = Message(
            message = messageTextField.text.toString(),
            from = user
        )

        db.collection("chats").document(chatId).collection("messages").document().set(message)

        messageTextField.setText("")


    }

    private  fun gotoChats(){
        val intent = Intent(this, ListOfChatsActivity::class.java)
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