package com.example.pantalla_registro.models

data class User (


    var id: String = "",
    var name: String = "",
    var apellido: String = "",
    var email: String = "",
    var password: String = "",
    var carrera: String = "",
    var user: String ="",
    var status: String ="",
    var addGroup: Boolean = false,
    var encrypted: Boolean = false,
    var pfp: String =""

        )