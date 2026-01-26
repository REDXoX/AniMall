package com.example.animall.ui.vistamodelo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animall.data.modelo.Producto
import kotlinx.coroutines.flow.MutableStateFlow
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

    // --- USUARIO ---
    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual
    private val _usuariosRegistrados = mutableListOf<Usuario>()

    // --- PRODUCTOS ---
    private val _productos = MutableStateFlow(
        listOf(
            Producto(1, "Collar Premium", "Accesorios", 15000, ""),
            Producto(2, "Alimento Perro 15kg", "Alimentos", 45000, ""),
            Producto(3, "Juguete Hueso", "Juguetes", 5990, ""),
            Producto(4, "Rascador Gato", "Accesorios", 25000, ""),
            Producto(5, "Alimento Gato 3kg", "Alimentos", 12000, "")
        )
    )

    // --- CARRITO DE COMPRAS (NUEVO) ---
    private val _carrito = MutableStateFlow<List<Producto>>(emptyList())
    val carrito: StateFlow<List<Producto>> = _carrito

    // Calculamos el total automáticamente cada vez que cambia el carrito
    val totalCarrito: StateFlow<Int> = _carrito.map { lista ->
        lista.sumOf { it.precio }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    fun agregarAlCarrito(producto: Producto) {
        // Añadimos el producto a la lista actual
        _carrito.value = _carrito.value + producto
    }

    fun vaciarCarrito() {
        // Se usa al pagar
        _carrito.value = emptyList()
    }

    fun quitarDelCarrito(producto: Producto) {
        // Elimina solo la primera instancia de ese producto encontrada
        val listaMutable = _carrito.value.toMutableList()
        listaMutable.remove(producto)
        _carrito.value = listaMutable
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
        val precioInt = precio.toIntOrNull() ?: 0
        val nuevoId = (_productos.value.maxOfOrNull { it.id } ?: 0) + 1
        _productos.value = _productos.value + Producto(nuevoId, nombre, categoria, precioInt, "")
    }

    fun actualizarProducto(id: Int, nombre: String, precio: String, categoria: String) {
        val precioInt = precio.toIntOrNull() ?: 0
        _productos.value = _productos.value.map {
            if (it.id == id) it.copy(nombre = nombre, precio = precioInt, categoria = categoria) else it
        }
    }

    fun eliminarProducto(id: Int) {
        _productos.value = _productos.value.filter { it.id != id }
    }

    fun onBusquedaChanged(query: String) { _busqueda.value = query }
    fun onCategoriaChanged(categoria: String) { _categoriaSeleccionada.value = categoria }
}