package com.example.desafio3

import java.io.Serializable

data class Recurso(
    val id: String = "",
    val titulo: String,
    val descripcion: String,
    val tipo: String,
    val enlace: String,
    val imagen: String
) : Serializable