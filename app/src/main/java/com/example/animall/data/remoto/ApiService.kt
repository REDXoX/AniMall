package com.example.animall.data.remoto

import com.example.animall.data.modelo.ItemCarrito
import com.example.animall.data.modelo.PedidoResponse
import com.example.animall.data.modelo.Producto
import retrofit2.http.*

interface ApiService {
    // --- PRODUCTOS ---
    @GET("api/productos")
    suspend fun getProductos(): List<Producto>

    // --- CARRITO ---
    @GET("api/carrito/{usuarioId}")
    suspend fun getCarrito(@Path("usuarioId") usuarioId: Long): List<ItemCarrito>

    @POST("api/carrito/{usuarioId}")
    suspend fun agregarItemCarrito(
        @Path("usuarioId") usuarioId: Long,
        @Body body: AgregarItemCarritoBody
    )

    @PUT("api/carrito/{usuarioId}/item/{itemId}")
    suspend fun actualizarItemCarrito(
        @Path("usuarioId") usuarioId: Long,
        @Path("itemId") itemId: Long,
        @Body body: ActualizarItemCarritoBody
    )

    @DELETE("api/carrito/{usuarioId}/item/{itemId}")
    suspend fun eliminarItemCarrito(
        @Path("usuarioId") usuarioId: Long,
        @Path("itemId") itemId: Long
    )

    @DELETE("api/carrito/{usuarioId}")
    suspend fun vaciarCarrito(@Path("usuarioId") usuarioId: Long)

    // --- PEDIDOS ---
    @POST("api/pedidos/{usuarioId}")
    suspend fun confirmarPedido(
        @Path("usuarioId") usuarioId: Long,
        @Body body: ConfirmarPedidoBody
    ): PedidoResponse
}