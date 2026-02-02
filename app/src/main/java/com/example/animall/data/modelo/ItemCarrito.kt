package com.example.animall.data.modelo

data class ItemCarrito(
    val id: Long,
    val producto: Producto, // Esta es la variable que no encontraba
    val cantidad: Int,
    val subtotal: Double
)