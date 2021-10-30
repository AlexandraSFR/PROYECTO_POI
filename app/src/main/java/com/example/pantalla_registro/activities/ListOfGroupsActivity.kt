package com.example.pantalla_registro.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantalla_registro.models.GroupChat
import com.example.pantalla_registro.adapters.GroupChatAdapter
import com.example.pantalla_registro.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_list_of_groups.*
import kotlinx.android.synthetic.main.activity_list_of_groups.listChatsRecyclerView
import kotlinx.android.synthetic.main.activity_list_of_groups.profileButton
import kotlinx.android.synthetic.main.activity_list_of_groups.gotoChats

class ListOfGroupsActivity : AppCompatActivity() {
    private var user = ""

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_groups)

        intent.getStringExtra("user")?.let { user = it }

        if (user.isNotEmpty()){
            initViews()
        }
    }
    private fun initViews(){
        gotoChats.setOnClickListener { gotoChats() }
        profileButton.setOnClickListener{ logOut() }
        goToCreate.setOnClickListener{gotoCreate()}
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

    private fun gotoCreate() {
        val intent = Intent(this, NewGroupActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)

        finish()
    }

    private fun chatSelected(chat: GroupChat){
        val intent = Intent(this, GroupChatActivity::class.java)
        intent.putExtra("chatId", chat.id)
        intent.putExtra("chatName", chat.Nombre)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    private fun logOut(){
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)

        finish()
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