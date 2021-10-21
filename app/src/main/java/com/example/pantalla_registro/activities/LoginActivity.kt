package com.example.pantalla_registro.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pantalla_registro.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.login.*

class LoginActivity : AppCompatActivity() {

    private val auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        iniciar_sesion.setOnClickListener { loginUser() }
        registrarse.setOnClickListener { createUser() }

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
        val intent = Intent(this, RegisterActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)

        finish()
    }

    private fun loginUser(){
        val email = correo.text.toString()
        val password = contra.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    checkUser()
                } else {
                    task.exception?.let {
                        Toast.makeText(baseContext, it.message, Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
        else{
            Toast.makeText(applicationContext,"All fields required",Toast.LENGTH_LONG).show();
        }

    }
}