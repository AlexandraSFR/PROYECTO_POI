package com.example.pantalla_registro


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.inflate
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.ToolbarWidgetWrapper
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pantalla_registro.R
import java.util.*
import androidx.recyclerview.widget.LinearLayoutManager




class MainActivity : AppCompatActivity() {

    private lateinit var buttonLogin: Button;
    private lateinit var buttonRegister: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        

        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_usuario)
        val spinner =findViewById<Spinner>(R.id.spinner2)
        //val lista= listOf("LMAD", "LA", "LM", "LF", "LCC", "LSTI")
        val lista = resources.getStringArray(R.array.Carreras)
        val adaptador = ArrayAdapter(this,android.R.layout.simple_spinner_item,lista)
        spinner.adapter =adaptador
        initViews()


    }
    private fun initViews(){
        buttonLogin = findViewById(R.id.button4);

        buttonLogin.setOnClickListener{ goToLogin() }
    }

    private fun goToLogin(){
        val intent = Intent(this,Login::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
        finish()
    }


}