package id.lastare.anabul

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import id.lastare.anabul.ui.screen.balance.BalanceScreen
import id.lastare.anabul.ui.screen.dashboard.DashboardScreen
import id.lastare.anabul.ui.screen.login.LoginScreen
import id.lastare.anabul.ui.screen.product.AddProductScreen
import id.lastare.anabul.ui.screen.register.RegisterScreen
import id.lastare.anabul.ui.screen.report.ReportScreen
import id.lastare.anabul.ui.screen.showcase.ShowcaseScreen
import id.lastare.anabul.ui.screen.splash.SplashScreen
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

// Sealed class untuk rute navigasi yang lebih aman
sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Dashboard : Screen("dashboard")
    data object Store : Screen("store")
    data object Showcase : Screen("showcase")
    data object AddProduct : Screen("add_product")
    data object Balance : Screen("balance")
    data object Report : Screen("report")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth: FirebaseAuth = koinInject()
    
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToDashboard = { 
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { 
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { 
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { 
                    navController.navigate(Screen.Register.route) 
                }
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { 
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { 
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToStore = { navController.navigate(Screen.Store.route) },
                onNavigateToShowcase = { navController.navigate(Screen.Showcase.route) },
                onNavigateToBalance = { navController.navigate(Screen.Balance.route) },
                onNavigateToReport = { navController.navigate(Screen.Report.route) },
                onLogout = {
                    auth.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Store.route) {
            StoreScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Showcase.route) {
            ShowcaseScreen(
                onNavigateBack = { navController.popBackStack() },
                onAddProduct = { navController.navigate(Screen.AddProduct.route) }
            )
        }
        
        composable(Screen.AddProduct.route) {
            AddProductScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Balance.route) {
            BalanceScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenStore = { navController.popBackStack() },
                onCloseStore = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Report.route) {
            ReportScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
