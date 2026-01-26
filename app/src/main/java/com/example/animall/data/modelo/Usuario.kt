package com.example.animall.data.modelo

data class Usuario(
    val id: String,
    val nombre: String,
    val email: String,
    val esAdmin: Boolean = false,
    val puntos: Int = 0,
    val ahorrado: Double = 0.0
)