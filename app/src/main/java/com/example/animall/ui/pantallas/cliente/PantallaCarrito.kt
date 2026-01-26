package com.example.animall.ui.pantallas.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animall.ui.vistamodelo.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCarrito(viewModel: AppViewModel) {
    // Obtenemos el carrito y el total del ViewModel
    val itemsCarrito by viewModel.carrito.collectAsState()
    val total by viewModel.totalCarrito.collectAsState()

    var mostrarDialogoExito by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFF5722))
            )
        },
        bottomBar = {
            if (itemsCarrito.isNotEmpty()) {
                // --- BARRA INFERIOR DE PAGO ---
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(16.dp),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total a Pagar:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text("$$total", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF5722))
                        }
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {
                                viewModel.vaciarCarrito() // Limpiamos el carro
                                mostrarDialogoExito = true // Mostramos el aviso
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("PAGAR AHORA", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (itemsCarrito.isEmpty()) {
            // --- ESTADO VACÍO ---
            Column(
                modifier = Modifier.fillMaxSize().padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.ShoppingCart, null, modifier = Modifier.size(100.dp), tint = Color.LightGray)
                Spacer(Modifier.height(16.dp))
                Text("Tu carrito está vacío", fontSize = 20.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                Text("¡Agrega productos para comenzar!", color = Color.Gray)
            }
        } else {
            // --- LISTA DE ITEMS ---
            LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
                items(itemsCarrito) { producto ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(producto.nombre, fontWeight = FontWeight.Bold)
                                Text(producto.categoria, fontSize = 12.sp, color = Color.Gray)
                                Text("$${producto.precio}", color = Color(0xFFFF5722), fontWeight = FontWeight.Bold)
                            }
                            IconButton(onClick = { viewModel.quitarDelCarrito(producto) }) {
                                Icon(Icons.Default.Delete, null, tint = Color.Gray)
                            }
                        }
                    }
                }
                // Espacio para que no tape la barra de pago
                item { Spacer(Modifier.height(100.dp)) }
            }
        }

        // --- ALERTA DE PAGO EXITOSO ---
        if (mostrarDialogoExito) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoExito = false },
                icon = { Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(48.dp)) },
                title = { Text("¡Pago Exitoso!", textAlign = TextAlign.Center) },
                text = { Text("Gracias por tu compra en AniMall. Tu pedido ha sido procesado correctamente.", textAlign = TextAlign.Center) },
                confirmButton = {
                    Button(
                        onClick = { mostrarDialogoExito = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Aceptar")
                    }
                },
                containerColor = Color.White
            )
        }
    }
}