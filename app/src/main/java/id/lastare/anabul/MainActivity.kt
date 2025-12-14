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
import id.lastare.anabul.ui.screen.dashboard.DashboardScreen
import id.lastare.anabul.ui.screen.store.StoreScreen
import id.lastare.anabul.ui.screen.showcase.ShowcaseScreen
import id.lastare.anabul.ui.screen.product.AddProductScreen
import id.lastare.anabul.ui.screen.balance.BalanceScreen
import id.lastare.anabul.ui.theme.AnabulTheme

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
    Dashboard, Store, Showcase, AddProduct, Balance
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf(Screen.Dashboard) }

    when (currentScreen) {
        Screen.Dashboard -> DashboardScreen(
            onNavigateToStore = { currentScreen = Screen.Store },
            onNavigateToShowcase = { currentScreen = Screen.Showcase },
            onNavigateToBalance = { currentScreen = Screen.Balance }
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
    }
}
