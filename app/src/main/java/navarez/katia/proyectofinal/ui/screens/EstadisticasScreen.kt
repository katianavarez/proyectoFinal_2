package navarez.katia.proyectofinal.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import navarez.katia.proyectofinal.viewmodel.LibroViewModel

@Composable
fun EstadisticasScreen(
    usuarioId: Int,
    viewModel: LibroViewModel,
    onNavigateToListaLibros: () -> Unit,
    onNavigateToPerfil: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Estadísticas")
    }
}
