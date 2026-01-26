package com.example.animall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.animall.ui.pantallas.admin.PantallaHomeAdmin
import com.example.animall.ui.pantallas.auth.PantallaLogin
import com.example.animall.ui.pantallas.auth.PantallaRegistro
import com.example.animall.ui.pantallas.cliente.PantallaPrincipalCliente
import com.example.animall.ui.vistamodelo.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 1. Instanciamos el ViewModel único para toda la app
            val viewModel: AppViewModel = viewModel()

            // 2. Observamos el estado del usuario (si está logueado o no)
            val usuario by viewModel.usuarioActual.collectAsState()

            // 3. Estado local para saber si mostramos Login o Registro (cuando no hay usuario)
            var mostrarRegistro by remember { mutableStateOf(false) }

            // --- LÓGICA DE NAVEGACIÓN ---

            if (usuario != null) {
                // CASO A: El usuario ESTÁ logueado
                // Reiniciamos el estado de registro por si cierra sesión luego
                mostrarRegistro = false

                if (usuario!!.esAdmin) {
                    // Si es Admin -> Panel de Administración
                    PantallaHomeAdmin(viewModel)
                } else {
                    // Si es Cliente -> Pantalla Principal con pestañas (Inicio, Carrito, Perfil)
                    PantallaPrincipalCliente(viewModel)
                }

            } else {
                // CASO B: El usuario NO está logueado
                if (mostrarRegistro) {
                    // Mostramos Pantalla de Registro
                    PantallaRegistro(
                        onRegistrarseClick = { nombre, email, pass ->
                            viewModel.registrarse(nombre, email)
                            // Al registrarse, el usuarioActual cambia y automáticamente entrará al "CASO A"
                        },
                        onVolverLogin = {
                            mostrarRegistro = false
                        }
                    )
                } else {
                    // Mostramos Pantalla de Login
                    PantallaLogin(
                        onLoginClick = { email, pass ->
                            viewModel.login(email, pass)
                            // La función login devuelve true/false, PantallaLogin se encarga de mostrar error si falla
                        },
                        onIrARegistro = {
                            mostrarRegistro = true
                        }
                    )
                }
            }
        }
    }
}