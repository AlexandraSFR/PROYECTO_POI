package com.example.pantalla_registro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_usuario)
        val spinner =findViewById<Spinner>(R.id.spinner2)
        //val lista= listOf("LMAD", "LA", "LM", "LF", "LCC", "LSTI")
        val lista = resources.getStringArray(R.array.Carreras)
        val adaptador = ArrayAdapter(this,android.R.layout.simple_spinner_item,lista)
        spinner.adapter =adaptador


    }
}