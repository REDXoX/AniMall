package com.example.animall.ui.pantallas.cliente

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.animall.ui.vistamodelo.AppViewModel

@Composable
fun PantallaPrincipalCliente(viewModel: AppViewModel) {
    // Controlamos qué pestaña se ve (0: Inicio, 1: Carrito, 2: Perfil)
    var indiceActual by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, "Inicio") },
                    label = { Text("Inicio") },
                    selected = indiceActual == 0,
                    onClick = { indiceActual = 0 },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFFFF5722))
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ShoppingCart, "Carrito") },
                    label = { Text("Carrito") },
                    selected = indiceActual == 1,
                    onClick = { indiceActual = 1 },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFFFF5722))
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, "Perfil") },
                    label = { Text("Perfil") },
                    selected = indiceActual == 2,
                    onClick = { indiceActual = 2 },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFFFF5722))
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            // Cambiamos el contenido según el índice
            when (indiceActual) {
                0 -> PantallaInicio(viewModel)
                1 -> PantallaCarrito(viewModel)
                2 -> PantallaPerfil(viewModel)
            }
        }
    }
}