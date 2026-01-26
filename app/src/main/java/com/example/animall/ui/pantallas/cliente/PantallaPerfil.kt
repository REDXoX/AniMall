package com.example.animall.ui.pantallas.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animall.ui.vistamodelo.AppViewModel

@Composable
fun PantallaPerfil(viewModel: AppViewModel) {
    val usuario by viewModel.usuarioActual.collectAsState()

    // Si por alguna razón es nulo, no mostramos nada (seguridad)
    if (usuario == null) return

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).background(Color(0xFFF5F5F5))
    ) {
        // --- CABECERA ---
        Box(
            modifier = Modifier.fillMaxWidth().height(280.dp)
                .background(Brush.verticalGradient(colors = listOf(Color(0xFFFF5722), Color(0xFFFF9800))))
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("AniMall", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.ShoppingCart, null, tint = Color.White)
                }
                Spacer(Modifier.height(30.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(80.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, null, tint = Color.Gray, modifier = Modifier.size(50.dp))
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(usuario!!.nombre, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(usuario!!.email, color = Color.White.copy(0.8f))
                    }
                }
            }
        }

        // --- ESTADÍSTICAS ---
        Row(Modifier.fillMaxWidth().padding(16.dp).offset(y = (-60).dp), horizontalArrangement = Arrangement.SpaceBetween) {
            InfoCard("2", "Pedidos", Icons.Outlined.ShoppingBag)
            InfoCard(usuario!!.puntos.toString(), "Puntos", Icons.Outlined.Star)
            InfoCard("$${usuario!!.ahorrado}", "Ahorrado", Icons.Outlined.Savings)
        }

        // --- MENÚ ---
        Column(Modifier.offset(y = (-40).dp).padding(horizontal = 16.dp)) {
            MenuOption("Mis pedidos", "Ver historial de compras", Icons.Outlined.Inventory2)
            MenuOption("Direcciones", "Administrar envíos", Icons.Outlined.LocationOn)
            MenuOption("Métodos de pago", "Tarjetas y opciones", Icons.Outlined.CreditCard)
            MenuOption("Configuración", "Preferencias de cuenta", Icons.Outlined.Settings)

            Spacer(Modifier.height(20.dp))
            Button(
                onClick = { viewModel.cerrarSesion() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) { Text("Cerrar sesión") }
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
fun InfoCard(valText: String, label: String, icon: ImageVector) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(4.dp), modifier = Modifier.size(105.dp, 100.dp)) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(icon, null, tint = Color.Gray); Text(valText, fontWeight = FontWeight.Bold); Text(label, fontSize = 12.sp, color = Color(0xFFFF9800))
        }
    }
}

@Composable
fun MenuOption(title: String, sub: String, icon: ImageVector) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Color(0xFFFF9800))
            Spacer(Modifier.width(16.dp))
            Column { Text(title, fontWeight = FontWeight.Bold); Text(sub, fontSize = 12.sp, color = Color.Gray) }
            Spacer(Modifier.weight(1f))
            Icon(Icons.Outlined.ArrowForwardIos, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
        }
    }
}