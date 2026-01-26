package com.example.animall.ui.pantallas.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaRegistro(
    onRegistrarseClick: (String, String, String) -> Unit,
    onVolverLogin: () -> Unit
) {
    // Variables de estado para los campos
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") } // Nuevo campo

    // Variable para controlar el mensaje de error específico
    var mensajeError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFFF5722), Color(0xFFFF9800))))
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- LOGO SUPERIOR ---
        Icon(
            imageVector = Icons.Default.Pets,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(80.dp)
        )
        Text(
            text = "AniMall",
            fontSize = 32.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- TARJETA DE REGISTRO ---
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Crear Cuenta",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE65100)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 1. NOMBRE
                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        mensajeError = null // Limpiar error al escribir
                    },
                    label = { Text("Nombre completo") },
                    leadingIcon = { Icon(Icons.Default.Person, null, tint = Color(0xFFFF9800)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 2. EMAIL
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        mensajeError = null
                    },
                    label = { Text("Correo electrónico") },
                    leadingIcon = { Icon(Icons.Default.Email, null, tint = Color(0xFFFF9800)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 3. CONTRASEÑA
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        mensajeError = null
                    },
                    label = { Text("Contraseña") },
                    leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color(0xFFFF9800)) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 4. CONFIRMAR CONTRASEÑA (NUEVO)
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        mensajeError = null
                    },
                    label = { Text("Confirmar contraseña") },
                    leadingIcon = { Icon(Icons.Default.VpnKey, null, tint = Color(0xFFFF9800)) }, // Icono diferente
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // --- MENSAJE DE ERROR ---
                if (mensajeError != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = mensajeError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- BOTÓN REGISTRAR CON VALIDACIONES ---
                Button(
                    onClick = {
                        // LÓGICA DE VALIDACIÓN
                        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                            mensajeError = "Por favor completa todos los campos."
                        } else if (!email.contains("@") || !email.contains(".com")) {
                            mensajeError = "El correo debe contener '@' y terminar en '.com'."
                        } else if (password.length < 6) {
                            mensajeError = "La contraseña debe tener al menos 6 caracteres."
                        } else if (password != confirmPassword) {
                            mensajeError = "Las contraseñas no coinciden."
                        } else {
                            // Si todo está correcto, procedemos
                            mensajeError = null
                            onRegistrarseClick(nombre, email, password)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722))
                ) {
                    Text("REGISTRARSE", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // VOLVER AL LOGIN
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("¿Ya tienes cuenta?", fontSize = 14.sp, color = Color.Gray)
                    TextButton(onClick = onVolverLogin) {
                        Text("Inicia Sesión", color = Color(0xFFE65100), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}