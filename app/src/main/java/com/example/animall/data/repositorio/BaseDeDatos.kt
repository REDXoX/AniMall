package com.example.animall.data.repositorio

import com.example.animall.data.modelo.Producto

object BaseDeDatos {
    val productos = listOf(
        // Fíjate: los números NO llevan comillas
        Producto(1, "Collar Premium", 15000.0, "Accesorios", ""),
        Producto(2, "Alimento Perro 15kg", 45000.0, "Alimentos", ""),
        Producto(3, "Juguete Hueso", 5990.0, "Juguetes", ""),
        Producto(4, "Rascador Gato", 25000.0, "Accesorios", ""),
        Producto(5, "Alimento Gato 3kg", 12000.0, "Alimentos", "")
    )
}