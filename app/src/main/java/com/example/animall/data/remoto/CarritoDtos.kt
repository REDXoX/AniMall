package com.example.animall.data.remoto

data class AgregarItemCarritoBody(
    val productoId: Long,
    val cantidad: Int
)

data class ActualizarItemCarritoBody(
    val cantidad: Int
)

data class ConfirmarPedidoBody(
    val direccionId: Long,
    val metodoPagoId: Long
)