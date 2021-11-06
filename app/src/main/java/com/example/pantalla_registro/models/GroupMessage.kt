package com.example.pantalla_registro.models

import java.util.*

data class GroupMessage (
    var dob: Date = Date(),
    var from:String = "",
    var message:String ="",
    var image: String =""
)