package com.example.animall.ui.pantallas.cliente

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset // <--- ESTA ES LA LÍNEA MÁGICA QUE FALTA
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animall.ui.vistamodelo.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(viewModel: AppViewModel) {
    val productos by viewModel.productosFiltrados.collectAsState()
    val busqueda by viewModel.busqueda.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()
    val categorias = viewModel.categoriasDisponibles

    Scaffold(
        topBar = {
            Column(Modifier.padding(16.dp)) {
                // Barra de Búsqueda
                OutlinedTextField(
                    value = busqueda,
                    onValueChange = { viewModel.onBusquedaChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar productos...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = MaterialTheme.shapes.medium
                )
                Spacer(Modifier.height(10.dp))
                // Filtros de Categoría
                ScrollableTabRow(
                    selectedTabIndex = categorias.indexOf(categoriaSeleccionada),
                    edgePadding = 0.dp,
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFFFF5722),
                    indicator = { tabPositions ->
                        // Aquí es donde ocurría el error, ahora funcionará con el import de arriba
                        if (categorias.indexOf(categoriaSeleccionada) < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[categorias.indexOf(categoriaSeleccionada)]),
                                color = Color(0xFFFF5722)
                            )
                        }
                    }
                ) {
                    categorias.forEach { categoria ->
                        Tab(
                            selected = categoriaSeleccionada == categoria,
                            onClick = { viewModel.onCategoriaChanged(categoria) },
                            text = { Text(categoria, fontWeight = if (categoriaSeleccionada == categoria) FontWeight.Bold else FontWeight.Normal) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).padding(horizontal = 16.dp)
        ) {
            items(productos) { producto ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(producto.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(producto.categoria, fontSize = 12.sp, color = Color.Gray)
                            Spacer(Modifier.height(4.dp))
                            Text("$${producto.precio}", fontSize = 16.sp, color = Color(0xFFFF5722), fontWeight = FontWeight.Bold)
                        }
                        IconButton(
                            onClick = { viewModel.agregarProductoAlCarrito(producto.id) },
                            colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFFF5722))
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Agregar", tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}