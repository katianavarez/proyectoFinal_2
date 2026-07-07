package navarez.katia.proyectofinal.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import navarez.katia.proyectofinal.ui.screens.AgregarLibroScreen
import navarez.katia.proyectofinal.ui.screens.DetalleLibroScreen
import navarez.katia.proyectofinal.ui.screens.EstadisticasScreen
import navarez.katia.proyectofinal.ui.screens.ListaLibrosScreen
import navarez.katia.proyectofinal.ui.screens.LoginScreen
import navarez.katia.proyectofinal.ui.screens.PerfilScreen
import navarez.katia.proyectofinal.ui.screens.RegistroScreen
import navarez.katia.proyectofinal.viewmodel.LibroViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Login,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Login> {
                LoginScreen(
                    onNavigateToRegistro = { navController.navigate(Registro) },
                    onNavigateToHome = { usuarioId ->
                        navController.navigate(ListaLibros(usuarioId)) {
                            popUpTo(Login) { inclusive = true }
                        }
                    }
                )
            }

            composable<Registro> {
                RegistroScreen(
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }

            composable<ListaLibros> { backStackEntry ->
                val listaLibros: ListaLibros = backStackEntry.toRoute()
                ListaLibrosScreen(
                    usuarioId = listaLibros.usuarioId,
                    onNavigateToDetalle = { libroId ->
                        navController.navigate(Detalle(libroId, listaLibros.usuarioId))
                    },
                    onNavigateToAgregar = {
                        navController.navigate(AgregarLibro(listaLibros.usuarioId))
                    }
                )
            }

            composable<Detalle> { backStackEntry ->
                val detalle: Detalle = backStackEntry.toRoute()
                DetalleLibroScreen(
                    libroId = detalle.libroId,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToListaLibros = {
                        navController.navigate(ListaLibros(detalle.usuarioId)) {
                            popUpTo(ListaLibros(detalle.usuarioId)) { inclusive = true }
                        }
                    },
                    onNavigateToEstadisticas = { navController.navigate(Estadisticas(detalle.usuarioId)) },
                    onNavigateToPerfil = { navController.navigate(Perfil(detalle.usuarioId)) }
                )
            }

            composable<AgregarLibro> { backStackEntry ->
                val agregar: AgregarLibro = backStackEntry.toRoute()
                val context = LocalContext.current
                val libroViewModel: LibroViewModel = viewModel(
                    factory = LibroViewModel.Factory(context)
                )
                AgregarLibroScreen(
                    usuarioId = agregar.usuarioId,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToListaLibros = {
                        navController.navigate(ListaLibros(agregar.usuarioId)) {
                            popUpTo(ListaLibros(agregar.usuarioId)) { inclusive = true }
                        }
                    },
                    onNavigateToEstadisticas = { navController.navigate(Estadisticas(agregar.usuarioId)) },
                    onNavigateToPerfil = { navController.navigate(Perfil(agregar.usuarioId)) },
                    onGuardarLibro = { nuevoLibro ->
                        libroViewModel.guardarLibro(agregar.usuarioId, nuevoLibro) { resultado ->
                            resultado
                                .onSuccess {
                                    Toast.makeText(context, "Libro guardado", Toast.LENGTH_SHORT).show()
                                    navController.navigate(ListaLibros(agregar.usuarioId)) {
                                        popUpTo(ListaLibros(agregar.usuarioId)) { inclusive = true }
                                    }
                                }
                                .onFailure {
                                    Toast.makeText(
                                        context,
                                        "No se pudo guardar: ${it.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }
                    }
                )
            }

            composable<Estadisticas> {
                EstadisticasScreen()
            }

            composable<Perfil> { backStackEntry ->
                val perfil: Perfil = backStackEntry.toRoute()
                PerfilScreen(
                    onCerrarSesion = {
                        navController.navigate(Login) {
                            popUpTo(ListaLibros(perfil.usuarioId)) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}


/**
@Composable
fun AppNavigation() {
val navController = rememberNavController()

NavHost(navController = navController, startDestination = Screen.Login.route,
modifier = Modifier.statusBarsPadding()) {

composable(Screen.Perfil.route) {
PerfilScreen(navController)
}
composable(Screen.AgregarLibro.route) {
AgregarLibroScreen(navController)
}

composable(Screen.Detalle.route) { backStackEntry ->
val libroId = backStackEntry.arguments?.getInt("libroId") ?: return@composable
DetalleLibroScreen(navController, libroId)
}
}
} **/