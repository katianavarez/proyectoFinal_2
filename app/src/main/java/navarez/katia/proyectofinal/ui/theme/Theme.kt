package navarez.katia.proyectofinal.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = TealPrimario,
    onPrimary = TextoSobrePrimario,
    primaryContainer = TealPrimario,
    onPrimaryContainer = TextoSobrePrimario,
    secondary = TealPrimario,
    onSecondary = TextoSobrePrimario,
    secondaryContainer = TealPrimario,
    onSecondaryContainer = TextoSobrePrimario,
    tertiary = TealPrimario,
    onTertiary = TextoSobrePrimario,
    background = TextoPrincipal,
    onBackground = FondoApp,
    surface = TextoPrincipal,
    onSurface = FondoApp,
    surfaceVariant = TextoSecundario,
    onSurfaceVariant = FondoApp,
    outline = TextoSecundario,
    error = ErrorColor
)

private val LightColorScheme = lightColorScheme(
    primary = TealPrimario,
    onPrimary = TextoSobrePrimario,
    primaryContainer = ContenedorAcento,
    onPrimaryContainer = TealPrimario,
    secondary = TealPrimario,
    onSecondary = TextoSobrePrimario,
    secondaryContainer = ContenedorAcento,
    onSecondaryContainer = TealPrimario,
    tertiary = TealPrimario,
    onTertiary = TextoSobrePrimario,
    tertiaryContainer = ContenedorAcento,
    onTertiaryContainer = TealPrimario,
    background = FondoApp,
    onBackground = TextoPrincipal,
    surface = SuperficieCard,
    onSurface = TextoPrincipal,
    surfaceVariant = SuperficieVariante,
    onSurfaceVariant = TextoSecundario,
    outline = BordeNeutro,
    error = ErrorColor,
    onError = TextoSobrePrimario
)

@Composable
fun ProyectoFinalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}