package com.example.animall.ui.pantallas.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animall.data.modelo.Producto
import com.example.animall.ui.vistamodelo.AppViewModel

@Composable
fun PantallaHomeAdmin(viewModel: AppViewModel) {
    // Observamos la lista de productos
    val productos by viewModel.productosFiltrados.collectAsState()

    // --- ESTADOS PARA EL DIÁLOGO (POP-UP) ---
    var mostrarDialogo by remember { mutableStateOf(false) }
    var productoAEditar by remember { mutableStateOf<Producto?>(null) } // Si es null, estamos creando. Si tiene datos, editando.

    // Variables del formulario
    var nombreInput by remember { mutableStateOf("") }
    var precioInput by remember { mutableStateOf("") }
    var categoriaInput by remember { mutableStateOf("Accesorios") }

    // Función auxiliar para abrir el diálogo en modo "Crear" o "Editar"
    fun abrirDialogo(producto: Producto? = null) {
        productoAEditar = producto
        if (producto != null) {
            // MODO EDITAR: Cargamos los datos del producto
            nombreInput = producto.nombre
            precioInput = producto.precio.toString()
            categoriaInput = producto.categoria
        } else {
            // MODO CREAR: Limpiamos los campos
            nombreInput = ""
            precioInput = ""
            categoriaInput = "Accesorios"
        }
        mostrarDialogo = true
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { abrirDialogo(null) }, // Null significa "Nuevo Producto"
                containerColor = Color(0xFFFF5722),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Producto")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            // --- HEADER DEGRADADO (Igual al Cliente) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(listOf(Color(0xFFFF5722), Color(0xFFFF9800))))
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Panel Admin", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("Gestiona tu inventario", color = Color.White.copy(0.9f), fontSize = 14.sp)
                    }
                    // Botón Salir
                    IconButton(
                        onClick = { viewModel.cerrarSesion() },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White.copy(alpha = 0.2f), contentColor = Color.White)
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar Sesión")
                    }
                }
            }

            // --- LISTA DE PRODUCTOS ---
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(productos) { producto ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Icono o Imagen simulada
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFFF3E0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Inventory, null, tint = Color(0xFFFF9800))
                            }

                            Spacer(Modifier.width(16.dp))

                            // Textos
                            Column(modifier = Modifier.weight(1f)) {
                                Text(producto.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(
                                    text = "${producto.categoria} • $${producto.precio}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }

                            // Botones de Acción
                            Row {
                                // Editar
                                IconButton(onClick = { abrirDialogo(producto) }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF1976D2))
                                }
                                // Eliminar
                                IconButton(onClick = { viewModel.eliminarProducto(producto.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFD32F2F))
                                }
                            }
                        }
                    }
                }
                // Espacio extra al final para que el botón flotante no tape el último item
                item { Spacer(Modifier.height(80.dp)) }
            }
        }

        // --- CUADRO DE DIÁLOGO (POP-UP) ---
        if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = { mostrarDialogo = false },
                containerColor = Color.White,
                title = {
                    Text(
                        text = if (productoAEditar == null) "Nuevo Producto" else "Editar Producto",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE65100)
                    )
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = nombreInput,
                            onValueChange = { nombreInput = it },
                            label = { Text("Nombre del producto") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = precioInput,
                            onValueChange = { if (it.all { char -> char.isDigit() }) precioInput = it }, // Solo números
                            label = { Text("Precio") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Categoría:", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Selector simple de categorías
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Alimentos", "Juguetes", "Accesorios").forEach { cat ->
                                val isSelected = categoriaInput == cat
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { categoriaInput = cat },
                                    label = { Text(cat) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFFFFE0B2),
                                        selectedLabelColor = Color(0xFFE65100)
                                    )
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (nombreInput.isNotEmpty() && precioInput.isNotEmpty()) {
                                if (productoAEditar == null) {
                                    // CREAR
                                    viewModel.agregarProducto(nombreInput, precioInput, categoriaInput)
                                } else {
                                    // ACTUALIZAR (Usamos el ID del producto que estamos editando)
                                    viewModel.actualizarProducto(productoAEditar!!.id, nombreInput, precioInput, categoriaInput)
                                }
                                mostrarDialogo = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
                    ) {
                        Text(if (productoAEditar == null) "Guardar" else "Actualizar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogo = false }) {
                        Text("Cancelar", color = Color.Gray)
                    }
                }
            )
        }
    }
}