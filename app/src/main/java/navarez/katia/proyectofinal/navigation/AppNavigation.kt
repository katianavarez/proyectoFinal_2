package navarez.katia.proyectofinal.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import navarez.katia.proyectofinal.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Splash,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Splash> {
                val usuarioViewModel: UsuarioViewModel = viewModel(
                    factory = UsuarioViewModel.Factory(context)
                )
                LaunchedEffect(Unit) {
                    usuarioViewModel.resolverSesionActiva { usuarioId ->
                        if (usuarioId != null) {
                            navController.navigate(ListaLibros(usuarioId)) {
                                popUpTo(Splash) { inclusive = true }
                            }
                        } else {
                            navController.navigate(Login) {
                                popUpTo(Splash) { inclusive = true }
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            composable<Login> {
                val usuarioViewModel: UsuarioViewModel = viewModel(
                    factory = UsuarioViewModel.Factory(context)
                )
                LoginScreen(
                    viewModel = usuarioViewModel,
                    onNavigateToRegistro = { navController.navigate(Registro) },
                    onNavigateToHome = { usuarioId ->
                        navController.navigate(ListaLibros(usuarioId)) {
                            popUpTo(Login) { inclusive = true }
                        }
                    }
                )
            }

            composable<Registro> {
                val usuarioViewModel: UsuarioViewModel = viewModel(
                    factory = UsuarioViewModel.Factory(context)
                )
                RegistroScreen(
                    viewModel = usuarioViewModel,
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }

            composable<ListaLibros> { backStackEntry ->
                val listaLibros: ListaLibros = backStackEntry.toRoute()
                val libroViewModel: LibroViewModel = viewModel(
                    factory = LibroViewModel.Factory(context)
                )
                ListaLibrosScreen(
                    usuarioId = listaLibros.usuarioId,
                    viewModel = libroViewModel,
                    onNavigateToDetalle = { libroId ->
                        navController.navigate(Detalle(libroId, listaLibros.usuarioId))
                    },
                    onNavigateToAgregar = {
                        navController.navigate(AgregarLibro(listaLibros.usuarioId))
                    },
                    onNavigateToEstadisticas = {
                        navController.navigate(Estadisticas(listaLibros.usuarioId))
                    },
                    onNavigateToPerfil = {
                        navController.navigate(Perfil(listaLibros.usuarioId))
                    }
                )
            }

            composable<Detalle> { backStackEntry ->
                val detalle: Detalle = backStackEntry.toRoute()
                val libroViewModel: LibroViewModel = viewModel(
                    factory = LibroViewModel.Factory(context)
                )
                DetalleLibroScreen(
                    libroId = detalle.libroId,
                    viewModel = libroViewModel,
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
                                    Toast.makeText(context, "No se pudo guardar: ${it.message}", Toast.LENGTH_LONG).show()
                                }
                        }
                    }
                )
            }

            composable<Estadisticas> { backStackEntry ->
                val estadisticas: Estadisticas = backStackEntry.toRoute()
                val libroViewModel: LibroViewModel = viewModel(
                    factory = LibroViewModel.Factory(context)
                )
                EstadisticasScreen(
                    usuarioId = estadisticas.usuarioId,
                    viewModel = libroViewModel,
                    onNavigateToListaLibros = {
                        navController.navigate(ListaLibros(estadisticas.usuarioId)) {
                            popUpTo(ListaLibros(estadisticas.usuarioId)) { inclusive = true }
                        }
                    },
                    onNavigateToPerfil = { navController.navigate(Perfil(estadisticas.usuarioId)) }
                )
            }

            composable<Perfil> { backStackEntry ->
                val perfil: Perfil = backStackEntry.toRoute()
                val usuarioViewModel: UsuarioViewModel = viewModel(
                    factory = UsuarioViewModel.Factory(context)
                )
                PerfilScreen(
                    usuarioId = perfil.usuarioId,
                    viewModel = usuarioViewModel,
                    onNavigateToListaLibros = {
                        navController.navigate(ListaLibros(perfil.usuarioId)) {
                            popUpTo(ListaLibros(perfil.usuarioId)) { inclusive = true }
                        }
                    },
                    onNavigateToEstadisticas = { navController.navigate(Estadisticas(perfil.usuarioId)) },
                    onCerrarSesion = {
                        usuarioViewModel.cerrarSesion()
                        navController.navigate(Login) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }

}

