package com.example.animall.data.repositorio

import com.example.animall.data.modelo.Producto

object BaseDeDatos {
    val productos = listOf(
        // Fíjate que ahora los precios y los ID son números (Int), sin comillas
        Producto(1, "Collar Premium", "Accesorios", 15000, ""),
        Producto(2, "Alimento Perro 15kg", "Alimentos", 45000, ""),
        Producto(3, "Juguete Hueso", "Juguetes", 5990, ""),
        Producto(4, "Rascador Gato", "Accesorios", 25000, ""),
        Producto(5, "Alimento Gato 3kg", "Alimentos", 12000, "")
    )
}