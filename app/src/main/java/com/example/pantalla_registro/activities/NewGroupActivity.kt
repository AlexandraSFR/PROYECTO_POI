package com.example.pantalla_registro.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantalla_registro.R
import com.example.pantalla_registro.adapters.AddUserAdapter
import com.example.pantalla_registro.adapters.GroupChatAdapter
import com.example.pantalla_registro.models.GroupChat
import com.example.pantalla_registro.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_list_of_groups.*
import kotlinx.android.synthetic.main.activity_new_group.*
import java.util.*


class NewGroupActivity : AppCompatActivity() {

    private var user = ""
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_group)

        intent.getStringExtra("user")?.let { user = it }

        if (user.isNotEmpty()){
            initViews()
        }
    }

    private fun initViews(){
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = AddUserAdapter{user->
            userSelected(user)
        }
        goToChats.setOnClickListener { gotoChats() }
        goToGroup.setOnClickListener{gotoGroups()}
        CreateGroup.setOnClickListener{creategroup()}

        val users = db.collection("users")

        users
            .get()
            .addOnSuccessListener { users ->
                val listChats = users.toObjects(User::class.java)

                (userRecyclerView.adapter as AddUserAdapter).setData(listChats)
            }

        users
            .addSnapshotListener { users, error ->
                if(error == null){
                    users?.let {
                        val listChats = it.toObjects(User::class.java)

                        (userRecyclerView.adapter as AddUserAdapter).setData(listChats)
                    }
                }
            }
    }

    private fun creategroup() {
        val groupId = UUID.randomUUID().toString()
        val groupName :String = GroupName.text.toString()

        if(groupName.isNotEmpty()) {
            val grupo = GroupChat(
                Nombre = groupName,
                id = groupId
            )
            db.collection("gchat").document(groupId).set(grupo)
            (userRecyclerView.adapter as AddUserAdapter).getAddList(groupId, groupName, user)
            db.collection("users").document(user).collection("groups").document(groupId).set(grupo)

            val intent = Intent(this, ListOfGroupsActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)

            finish()
        }
        else{
            Toast.makeText(applicationContext, "Por favor ingrese el Nombre del Grupo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun userSelected(user: User) {


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