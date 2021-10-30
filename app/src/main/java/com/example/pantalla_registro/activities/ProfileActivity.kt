package com.example.pantalla_registro.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantalla_registro.R
import com.example.pantalla_registro.Singleton.Sesion
import com.example.pantalla_registro.adapters.AddUserAdapter
import com.example.pantalla_registro.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile.*

import java.util.*

class ProfileActivity : AppCompatActivity() {

    private var user = ""
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        intent.getStringExtra("user")?.let { user = it }

        if (user.isNotEmpty()){
            initViews()
        }


    }

    private fun initViews(){


        db.collection("users").document(user).get().addOnSuccessListener{users ->
            val listChats = users.toObject(User::class.java)
            if (listChats != null) {
                textNombre.setText(listChats.name)
                textCarrera.setText(listChats.carrera)
                textCorreo.setText(listChats.email)
                textUsuario.setText(listChats.user)
            }


        }
        logOut.setOnClickListener { LogOut() }
        goChat.setOnClickListener { gotoChats() }
        goGroup.setOnClickListener { gotoGroups() }




    }



    override fun onPause() {
        super.onPause()
        db.collection("users").document(user).update("status","Offline")

    }


    override fun onResume(){
        super.onResume()
        db.collection("users").document(user).update("status","Online")


    }
    private fun LogOut(){

        FirebaseAuth.getInstance().signOut()
        db.collection("users").document(user).update("status","Offline")
        Sesion.Logout()
        val intent = Intent(this,LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }
    private  fun gotoGroups(){
        val intent = Intent(this, ListOfGroupsActivity::class.java)
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


}