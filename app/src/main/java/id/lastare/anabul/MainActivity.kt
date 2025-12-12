package id.lastare.anabul

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import id.lastare.anabul.ui.dashboard.DashboardScreen
import id.lastare.anabul.ui.theme.AnabulTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnabulTheme {
                DashboardScreen()
            }
        }
    }
}
