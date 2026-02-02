package com.example.animall.ui.vistamodelo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animall.data.modelo.ItemCarrito
import com.example.animall.data.modelo.PedidoResponse
import com.example.animall.data.modelo.Producto
import com.example.animall.data.remoto.AgregarItemCarritoBody
import com.example.animall.data.remoto.ActualizarItemCarritoBody
import com.example.animall.data.remoto.ConfirmarPedidoBody
import com.example.animall.data.remoto.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class Usuario(
    val id: String,
    val nombre: String,
    val email: String,
    val esAdmin: Boolean,
    val puntos: Int = 0,
    val ahorrado: Int = 0
)

class AppViewModel : ViewModel() {

    private val USUARIO_CLIENTE_ID = 1L
    private val USUARIO_ADMIN_ID = 2L
    private val DIRECCION_CLIENTE_ID = 1L
    private val METODO_PAGO_CLIENTE_ID = 1L

    // --- USUARIO ---
    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual
    private val _usuariosRegistrados = mutableListOf<Usuario>()

    // --- PRODUCTOS ---
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())

    init {
        cargarProductos()
        cargarCarrito()
    }

    private fun cargarProductos() {
        viewModelScope.launch {
            try {
                _productos.value = RetrofitClient.apiService.getProductos()
            } catch (e: Exception) {
                _productos.value = emptyList()
            }
        }
    }

    fun recargarProductos() {
        cargarProductos()
    }

    // --- CARRITO DE COMPRAS (NUEVO) ---
    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito

    private val _pedidoConfirmado = MutableStateFlow<PedidoResponse?>(null)
    val pedidoConfirmado: StateFlow<PedidoResponse?> = _pedidoConfirmado

    private fun cargarCarrito() {
        viewModelScope.launch {
            try {
                _carrito.value = RetrofitClient.apiService.getCarrito(USUARIO_CLIENTE_ID)
            } catch (e: Exception) {
                _carrito.value = emptyList()
            }
        }
    }

    // Calculamos el total automáticamente cada vez que cambia el carrito
    val totalCarrito: StateFlow<Double> = _carrito.map { lista ->
        lista.sumOf { it.subtotal }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)

    fun confirmarPedido() {
        viewModelScope.launch {
            try {
                val respuesta = RetrofitClient.apiService.confirmarPedido(
                    usuarioId = USUARIO_CLIENTE_ID,
                    body = ConfirmarPedidoBody(direccionId = DIRECCION_CLIENTE_ID, metodoPagoId = METODO_PAGO_CLIENTE_ID)
                )
                _pedidoConfirmado.value = respuesta
                cargarCarrito()
            } catch (e: Exception) {
            }
        }
    }

    fun agregarProductoAlCarrito(productoId: Long) {
        viewModelScope.launch {
            try {
                RetrofitClient.apiService.agregarItemCarrito(
                    usuarioId = USUARIO_CLIENTE_ID,
                    body = AgregarItemCarritoBody(productoId = productoId, cantidad = 1)
                )
                cargarCarrito()
            } catch (e: Exception) {
            }
        }
    }

    fun actualizarCantidadItem(itemId: Long, cantidad: Int) {
        viewModelScope.launch {
            try {
                RetrofitClient.apiService.actualizarItemCarrito(
                    usuarioId = USUARIO_CLIENTE_ID,
                    itemId = itemId,
                    body = ActualizarItemCarritoBody(cantidad = cantidad)
                )
                cargarCarrito()
            } catch (e: Exception) {
                // No hacer nada especial
            }
        }
    }

    fun eliminarItemCarrito(itemId: Long) {
        viewModelScope.launch {
            try {
                RetrofitClient.apiService.eliminarItemCarrito(
                    usuarioId = USUARIO_CLIENTE_ID,
                    itemId = itemId
                )
                cargarCarrito()
            } catch (e: Exception) {
                // No hacer nada especial
            }
        }
    }

    fun vaciarCarrito() {
        viewModelScope.launch {
            try {
                RetrofitClient.apiService.vaciarCarrito(usuarioId = USUARIO_CLIENTE_ID)
                cargarCarrito()
            } catch (e: Exception) {
                // No hacer nada especial
            }
        }
    }

    // --- FILTROS Y BÚSQUEDA ---
    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda

    private val _categoriaSeleccionada = MutableStateFlow("Todas")
    val categoriaSeleccionada: StateFlow<String> = _categoriaSeleccionada

    val productosFiltrados: StateFlow<List<Producto>> = combine(_productos, _busqueda, _categoriaSeleccionada) { productos, busqueda, categoria ->
        productos.filter { producto ->
            val coincideNombre = producto.nombre.contains(busqueda, ignoreCase = true)
            val coincideCategoria = if (categoria == "Todas") true else producto.categoria == categoria
            coincideNombre && coincideCategoria
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _productos.value)

    val categoriasDisponibles = listOf("Todas", "Alimentos", "Juguetes", "Accesorios", "Aves")

    // --- LOGIN / REGISTRO ---
    fun login(email: String, pass: String): Boolean {
        if (email == "admin@animall.com" && pass == "123456") {
            _usuarioActual.value = Usuario("0", "Administrador", email, true)
            return true
        }
        if (email == "cliente@animall.com" && pass == "123456") {
            _usuarioActual.value = Usuario("1", "Cliente Demo", email, false, 120, 3500)
            return true
        }
        val usuarioEncontrado = _usuariosRegistrados.find { it.email == email }
        if (usuarioEncontrado != null) {
            _usuarioActual.value = usuarioEncontrado
            return true
        }
        return false
    }

    fun registrarse(nombre: String, email: String) {
        val nuevoUsuario = Usuario(System.currentTimeMillis().toString(), nombre, email, false)
        _usuariosRegistrados.add(nuevoUsuario)
        _usuarioActual.value = nuevoUsuario
    }

    fun cerrarSesion() { _usuarioActual.value = null }

    // --- CRUD ADMIN ---
    fun agregarProducto(nombre: String, precio: String, categoria: String) {
        val precioDouble = precio.toDoubleOrNull() ?: 0.0
        val nuevoId = (_productos.value.maxOfOrNull { it.id } ?: 0L) + 1L
        _productos.value = _productos.value + Producto(nuevoId, nombre, precioDouble, categoria)
    }

    fun actualizarProducto(id: Long, nombre: String, precio: String, categoria: String) {
        val precioDouble = precio.toDoubleOrNull() ?: 0.0
        _productos.value = _productos.value.map {
            if (it.id == id) it.copy(nombre = nombre, precio = precioDouble, categoria = categoria) else it
        }
    }

    fun eliminarProducto(id: Long) {
        _productos.value = _productos.value.filter { it.id != id }
    }

    fun onBusquedaChanged(query: String) { _busqueda.value = query }
    fun onCategoriaChanged(categoria: String) { _categoriaSeleccionada.value = categoria }
}