package com.example.desafio3

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

data class Recurso(
    val id: Int = 0,
    val titulo: String = "",
    val descripcion: String = "",
    val tipo: String = "",
    val enlace: String = "",
    val imagen: String = ""
) {
    constructor() : this(0, "", "", "", "", "")
}
