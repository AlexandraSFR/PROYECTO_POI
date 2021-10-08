package com.example.pantalla_registro

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {

    private lateinit var buttonLogin: Button;
    private lateinit var buttonRegister: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_form)
        initViews()


    }
    private fun initViews(){
        buttonLogin = findViewById(R.id.button2);
        buttonRegister = findViewById(R.id.button3);

        buttonLogin.setOnClickListener{ goToMain() }
        buttonRegister.setOnClickListener{ goToRegister() }
    }

    private fun goToRegister(){
        val intent = Intent(this,MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }

    private fun goToMain(){
        val intent = Intent(this,MainMenu::class.java)
        //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }


}