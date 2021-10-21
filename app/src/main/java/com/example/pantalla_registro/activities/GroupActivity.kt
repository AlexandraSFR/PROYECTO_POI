package com.example.pantalla_registro.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantalla_registro.models.Chat
import com.example.pantalla_registro.adapters.ChatAdapter
import com.example.pantalla_registro.R
import com.example.pantalla_registro.adapters.GroupAdapter
import com.example.pantalla_registro.models.GroupMessage
import com.example.pantalla_registro.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_chat.messageTextField
import kotlinx.android.synthetic.main.activity_chat.messagesRecylerView
import kotlinx.android.synthetic.main.activity_chat.sendMessageButton
import kotlinx.android.synthetic.main.activity_group.*
import kotlinx.android.synthetic.main.activity_list_of_chats.*
import java.util.*


class GroupActivity : AppCompatActivity() {
    private var user = ""

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        intent.getStringExtra("user")?.let { user = it }

        if (user.isNotEmpty()){
            initViews()
        }


    }

    private fun initViews(){
        messagesRecylerView.layoutManager = LinearLayoutManager(this)
        messagesRecylerView.adapter = GroupAdapter("lmad");
        Chat.setOnClickListener { gotoChats() }

        LogOut.setOnClickListener{ logOut() }
        sendMessageButton.setOnClickListener { sendMessage() }



        val generalChat = db.collection("gchat").document("DaoZQ8UYJ0xa0VbekYtZ");
        generalChat.collection("messages").orderBy("dob", Query.Direction.ASCENDING).get().addOnSuccessListener { ChatG ->
            val listMessages = ChatG.toObjects(GroupMessage::class.java)
            (messagesRecylerView.adapter as GroupAdapter).setData(listMessages)
        }

        generalChat.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .addSnapshotListener { messages, error ->
                if(error == null){
                    messages?.let {
                        val listMessages = it.toObjects(GroupMessage::class.java)
                        (messagesRecylerView.adapter as GroupAdapter).setData(listMessages)
                    }
                }
            }
    }
    private fun sendMessage(){
        val message = GroupMessage(
            from = user,
            message = messageTextField.text.toString()

        )

        db.collection("gchat").document("DaoZQ8UYJ0xa0VbekYtZ").collection("messages").document().set(message)

        messageTextField.setText("")


    }

    private fun logOut(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this,LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private fun gotoChats(){
        val intent = Intent(this, ListOfChatsActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)

        finish()
    }
}