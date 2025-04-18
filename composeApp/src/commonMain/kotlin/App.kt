import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import data.MongoDB
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.context.startKoin
import org.koin.dsl.module
import presentation.components.LoadingScreen
import presentation.screen.home.HomeScreen
import presentation.screen.home.HomeViewModel
import presentation.screen.task.TaskViewModel

val lightBlue = Color.Blue
val onLightBlue = Color(0xFF00344A)


val darkBlue = Color.Blue
val onDarkBlue = Color(0xFFE3F2FD)

@Composable
@Preview
fun App() {
    // Remember a mutable state to track whether initialization is done
    var isAppReady by remember { mutableStateOf(false) }

    // Launch initialization in a LaunchedEffect block
    LaunchedEffect(Unit) {
        // Simulate initialization (e.g., Koin, DB prep, etc.)
        initializeKoin()
        delay(1000) // Optional delay for smoother UX
        isAppReady = true
    }

    val lightColors = lightColorScheme(
        primary = Color.Blue,
        onPrimary = Color(0xFFE3F2FD),
        primaryContainer = Color(0xFF90CAF9),
        onPrimaryContainer = Color(0xFF0D47A1)
    )

    val darkColors = darkColorScheme(
        primary = Color.Blue,
        onPrimary = Color(0xFF00344A),
        primaryContainer = Color(0xFF4FC3F7),
        onPrimaryContainer = Color(0xFFB3E5FC)
    )

    val colors = if (isSystemInDarkTheme()) darkColors else lightColors

    MaterialTheme(colorScheme = colors) {
        if (isAppReady) {
            Navigator(HomeScreen()) {
                SlideTransition(it)
            }
        } else {
            LoadingScreen()
        }
    }
}


val mongoModule = module {
    single { MongoDB() }
    factory { HomeViewModel(get()) }
    factory { TaskViewModel(get()) }
}

fun initializeKoin() {
    startKoin {
        modules(mongoModule)
    }
}