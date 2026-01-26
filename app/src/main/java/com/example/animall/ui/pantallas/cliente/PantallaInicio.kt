package com.example.animall.ui.pantallas.cliente

// Borramos el import de Toast y LocalContext porque ya no los usaremos
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animall.ui.vistamodelo.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(viewModel: AppViewModel) {
    val productos by viewModel.productosFiltrados.collectAsState()
    val searchText by viewModel.busqueda.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()
    val categorias = viewModel.categoriasDisponibles

    // Eliminamos la variable "context" porque ya no mostraremos el Toast

    Column(Modifier.fillMaxSize()) {
        // --- BANNER SUPERIOR NARANJA ---
        Box(
            modifier = Modifier.fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(Color(0xFFFF5722), Color(0xFFFF9800))))
                .padding(20.dp)
        ) {
            Column {
                Text("AniMall", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text("Todo para tu mascota", color = Color.White.copy(0.9f))
                Spacer(Modifier.height(16.dp))

                TextField(
                    value = searchText,
                    onValueChange = { viewModel.onBusquedaChanged(it) },
                    placeholder = { Text("Buscar productos...", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(50)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }
        }

        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            // --- CATEGORÍAS ---
            item {
                Text("Categorías", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))
                LazyRow {
                    items(categorias) { cat ->
                        FilterChip(
                            selected = cat == categoriaSeleccionada,
                            onClick = { viewModel.onCategoriaChanged(cat) },
                            label = { Text(cat) },
                            modifier = Modifier.padding(end = 8.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFFFE0B2),
                                selectedLabelColor = Color(0xFFE65100)
                            )
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // --- LISTA PRODUCTOS ---
            if (productos.isEmpty()) {
                item {
                    Text("No se encontraron productos", color = Color.Gray, modifier = Modifier.padding(16.dp))
                }
            } else {
                items(productos) { producto ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFEEEEEE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(producto.nombre.take(1), fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.Gray)
                            }
                            Spacer(Modifier.width(16.dp))
                            Column(Modifier.weight(1f)) {
                                Text(producto.categoria, fontSize = 12.sp, color = Color(0xFFFF9800), fontWeight = FontWeight.Bold)
                                Text(producto.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("$${producto.precio}", color = Color(0xFFFF5722), fontWeight = FontWeight.Bold)
                            }
                            // --- BOTÓN AGREGAR AL CARRITO (SIN MENSAJE) ---
                            IconButton(
                                onClick = {
                                    viewModel.agregarAlCarrito(producto)
                                    // Aquí eliminamos el Toast. El producto se agrega silenciosamente.
                                },
                                colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFFF9800), contentColor = Color.White)
                            ) {
                                Icon(Icons.Default.AddShoppingCart, null)
                            }
                        }
                    }
                }
            }
        }
    }
}