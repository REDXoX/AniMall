package com.example.animall.data.modelo

data class Producto(
    val id: Long,
    val nombre: String,
    val precio: Double,
    val categoria: String,
    val imagenUrl: String? = null
)
