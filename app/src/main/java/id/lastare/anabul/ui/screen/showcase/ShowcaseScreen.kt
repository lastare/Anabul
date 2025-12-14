package id.lastare.anabul.ui.screen.showcase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.lastare.anabul.ui.theme.AnabulTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowcaseScreen(
    onNavigateBack: () -> Unit = {},
    onRestockFromWarehouse: () -> Unit = {}
) {
    // Dummy Data: Low Stock & Out of Stock
    val alertItems = listOf(
        ShowcaseAlert("Whiskas Tuna 1.2kg", 0, StockStatus.EMPTY),
        ShowcaseAlert("Royal Canin Adult", 2, StockStatus.LOW),
        ShowcaseAlert("Kalung Kucing", 3, StockStatus.LOW)
    )

    // Dummy Data: Transactions (Sales & Warehouse Restock)
    val transactions = listOf(
        ShowcaseTransaction("Bolt Ikan Ungu 1kg", "Penjualan", 2, Color(0xFFF44336)), // Red for Sales (Out)
        ShowcaseTransaction("Whiskas Tuna 1.2kg", "Dari Gudang", 10, Color(0xFF4CAF50)), // Green for Warehouse (In)
        ShowcaseTransaction("Pasir Kucing Wangi", "Penjualan", 1, Color(0xFFF44336)),
        ShowcaseTransaction("Me-O Creamy Treats", "Dari Gudang", 50, Color(0xFF4CAF50)),
        ShowcaseTransaction("Royal Canin Adult", "Penjualan", 1, Color(0xFFF44336))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Etalase Toko",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onRestockFromWarehouse,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Ambil dari Gudang") }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
        ) {
            // Section: Alerts (Low & Empty Stock)
            if (alertItems.isNotEmpty()) {
                item {
                    Text(
                        text = "Perhatian Stok",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                items(alertItems) { item ->
                    StockAlertCard(item)
                }
            }

            // Section: History
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Riwayat Etalase",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Transaksi Penjualan & Masuk dari Gudang",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            items(transactions) { transaction ->
                ShowcaseTransactionItem(transaction)
            }
        }
    }
}

enum class StockStatus { LOW, EMPTY }

data class ShowcaseAlert(
    val productName: String,
    val remainingStock: Int,
    val status: StockStatus
)

data class ShowcaseTransaction(
    val productName: String,
    val type: String, // "Penjualan" or "Dari Gudang"
    val amount: Int,
    val color: Color
)

@Composable
fun StockAlertCard(item: ShowcaseAlert) {
    val containerColor = if (item.status == StockStatus.EMPTY) 
        MaterialTheme.colorScheme.errorContainer 
    else 
        Color(0xFFFFE082) // Warning Yellow/Amber

    val contentColor = if (item.status == StockStatus.EMPTY) 
        MaterialTheme.colorScheme.onErrorContainer 
    else 
        Color(0xFF5D4037) // Dark Brown for contrast on Yellow

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (item.status == StockStatus.EMPTY) Icons.Default.Inventory else Icons.Default.Warning,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = item.productName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = contentColor
                    )
                    Text(
                        text = if (item.status == StockStatus.EMPTY) "Stok Kosong!" else "Stok Menipis",
                        style = MaterialTheme.typography.bodySmall,
                        color = contentColor.copy(alpha = 0.8f)
                    )
                }
            }
            
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = contentColor.copy(alpha = 0.1f)
            ) {
                Text(
                    text = "${item.remainingStock} pcs",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ShowcaseTransactionItem(transaction: ShowcaseTransaction) {
    val isSale = transaction.type == "Penjualan"
    
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = transaction.color.copy(alpha = 0.1f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isSale) Icons.AutoMirrored.Filled.TrendingDown else Icons.Default.Warehouse,
                            contentDescription = transaction.type,
                            tint = transaction.color,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Column {
                    Text(
                        text = transaction.productName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = transaction.type,
                        style = MaterialTheme.typography.labelMedium,
                        color = transaction.color
                    )
                }
            }

            Text(
                text = "${if (isSale) "-" else "+"}${transaction.amount}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = transaction.color
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowcasePreview() {
    AnabulTheme {
        ShowcaseScreen()
    }
}
