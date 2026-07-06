package navarez.katia.proyectofinal.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
                        navController.navigate(ListaLibros) {
                            popUpTo(ListaLibros) { inclusive = true }
                        }
                    },
                    onNavigateToEstadisticas = { navController.navigate(Estadisticas) },
                    onNavigateToPerfil = { navController.navigate(Perfil) }
                )
            }

            composable<AgregarLibro> {
                AgregarLibroScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToListaLibros = {
                        navController.navigate(ListaLibros) {
                            popUpTo(ListaLibros) { inclusive = true }
                        }
                    },
                    onNavigateToEstadisticas = { navController.navigate(Estadisticas) },
                    onNavigateToPerfil = { navController.navigate(Perfil) }
                )
            }

            composable<Estadisticas> {
                EstadisticasScreen()
            }

            composable<Perfil> {
                PerfilScreen(
                    onCerrarSesion = {
                        navController.navigate(Login) {
                            popUpTo(ListaLibros) { inclusive = true }
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