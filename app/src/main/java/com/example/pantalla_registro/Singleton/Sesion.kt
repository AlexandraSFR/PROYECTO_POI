package com.example.pantalla_registro.Singleton

object Sesion {

    var id: String = "";
    var name: String = "";
    var apellido: String = "";
    var email: String = "";
    var password: String = "";
    var carrera: String = "";
    var user: String ="";

    fun Logout(){
        id="";
        name="";
        apellido="";
        email="";
        password="";
        carrera="";
        user="";
    }

}
