package navarez.katia.proyectofinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(onNavigateToRegistro: () -> Unit, onNavigateToHome: (Int) -> Unit) {
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Book,
                contentDescription = "Logo de la app",
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Text("Bienvenido de nuevo", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Tu biblioteca personal te espera",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Correo electrónico", style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    placeholder = { Text("nombre@ejemplo.com") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Text("Contraseña", style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("**********") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(
                            imageVector = icon,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                        )
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = { onNavigateToHome(0) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar sesión")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Row {
            Text("¿No tienes cuenta? ", style = MaterialTheme.typography.bodyMedium)
            Text(
                "Regístrate",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onNavigateToRegistro() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(onNavigateToRegistro = {}, onNavigateToHome = {})
    }
}

