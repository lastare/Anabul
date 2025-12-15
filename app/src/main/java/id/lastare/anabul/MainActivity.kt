package id.lastare.anabul

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import id.lastare.anabul.ui.screen.balance.BalanceScreen
import id.lastare.anabul.ui.screen.dashboard.DashboardScreen
import id.lastare.anabul.ui.screen.login.LoginScreen
import id.lastare.anabul.ui.screen.product.AddProductScreen
import id.lastare.anabul.ui.screen.register.RegisterScreen
import id.lastare.anabul.ui.screen.report.ReportScreen
import id.lastare.anabul.ui.screen.showcase.ShowcaseScreen
import id.lastare.anabul.ui.screen.store.StoreScreen
import id.lastare.anabul.ui.theme.AnabulTheme
import com.google.firebase.auth.FirebaseAuth
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnabulTheme {
                AppNavigation()
            }
        }
    }
}

enum class Screen {
    Login, Register, Dashboard, Store, Showcase, AddProduct, Balance, Report
}

@Composable
fun AppNavigation() {
    val auth: FirebaseAuth = koinInject()
    // Simple start destination check
    val startDestination = if (auth.currentUser != null) Screen.Dashboard else Screen.Login
    
    var currentScreen by remember { mutableStateOf(startDestination) }

    when (currentScreen) {
        Screen.Login -> LoginScreen(
            onLoginSuccess = { currentScreen = Screen.Dashboard },
            onNavigateToRegister = { currentScreen = Screen.Register }
        )
        Screen.Register -> RegisterScreen(
            onRegisterSuccess = { currentScreen = Screen.Dashboard },
            onNavigateToLogin = { currentScreen = Screen.Login }
        )
        Screen.Dashboard -> DashboardScreen(
            onNavigateToStore = { currentScreen = Screen.Store },
            onNavigateToShowcase = { currentScreen = Screen.Showcase },
            onNavigateToBalance = { currentScreen = Screen.Balance },
            onNavigateToReport = { currentScreen = Screen.Report },
            onLogout = {
                auth.signOut()
                currentScreen = Screen.Login
            }
        )
        Screen.Store -> StoreScreen(
            onNavigateBack = { currentScreen = Screen.Dashboard }
        )
        Screen.Showcase -> ShowcaseScreen(
            onNavigateBack = { currentScreen = Screen.Dashboard },
            onAddProduct = { currentScreen = Screen.AddProduct }
        )
        Screen.AddProduct -> AddProductScreen(
            onNavigateBack = { currentScreen = Screen.Showcase }
        )
        Screen.Balance -> BalanceScreen(
            onNavigateBack = { currentScreen = Screen.Dashboard },
            onOpenStore = { currentScreen = Screen.Dashboard },
            onCloseStore = { currentScreen = Screen.Dashboard }
        )
        Screen.Report -> ReportScreen(
            onNavigateBack = { currentScreen = Screen.Dashboard }
        )
    }
}
