package com.example.pantalla_registro.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pantalla_registro.R
import com.example.pantalla_registro.models.Chat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_list_of_chats.*
import kotlinx.android.synthetic.main.registro.*

import java.util.*

class RegisterActivity : AppCompatActivity() {

    private val auth = Firebase.auth
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro)
        val spinner =findViewById<Spinner>(R.id.spinner2)
        //val lista= listOf("LMAD", "LA", "LM", "LF", "LCC", "LSTI")
        val lista = resources.getStringArray(R.array.Carreras)
        val adaptador = ArrayAdapter(this,android.R.layout.simple_spinner_item,lista)

        registrar.setOnClickListener { createUser() }
        cancelar.setOnClickListener{ goToLogin()}

        checkUser()
    }

    private fun checkUser(){
        val currentUser = auth.currentUser

        if(currentUser != null){
            val intent = Intent(this, ListOfChatsActivity::class.java)
            intent.putExtra("user", currentUser.email)
            startActivity(intent)

            finish()
        }
    }

    private fun createUser(){
        val email = textCorreo.text.toString()
        val password = textPassword.text.toString()
        val chatId = UUID.randomUUID().toString()
        val users = listOf(email)



        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(applicationContext,"User created. Logging in...",Toast.LENGTH_LONG).show();
                checkUser()
            } else {
                task.exception?.let {
                    Toast.makeText(baseContext, it.message, Toast.LENGTH_LONG).show()
                }
            }

        }


    }

    private fun goToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)

        finish()
    }


}