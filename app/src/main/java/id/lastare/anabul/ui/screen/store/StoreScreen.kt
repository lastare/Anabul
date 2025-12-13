package id.lastare.anabul.ui.screen.store

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.lastare.anabul.ui.theme.AnabulTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(
    onNavigateBack: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }
    val categories = listOf("Semua", "Makanan", "Pasir", "Vitamin", "Aksesoris", "Obat")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Penjualan Toko",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    BadgedBox(
                        badge = {
                            Badge { Text("3") }
                        },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "Keranjang"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            )

            // Categories
            ScrollableTabRow(
                selectedTabIndex = categories.indexOf(selectedCategory),
                edgePadding = 24.dp,
                containerColor = Color.Transparent,
                divider = {},
                indicator = {},
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                categories.forEach { category ->
                    CategoryChip(
                        text = category,
                        isSelected = category == selectedCategory,
                        onClick = { selectedCategory = category }
                    )
                }
            }

            // Product Grid
            ProductGrid(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp)
            )
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        placeholder = { Text("Cari produk...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        singleLine = true
    )
}

@Composable
fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(end = 8.dp)
            .height(36.dp)
            .clickable(onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
fun ProductGrid(modifier: Modifier = Modifier) {
    val products = listOf(
        Product("Bolt Ikan Ungu 1kg", "Rp 18.000", Color(0xFF9C27B0)),
        Product("Whiskas Tuna 1.2kg", "Rp 65.000", Color(0xFF673AB7)),
        Product("Royal Canin Adult", "Rp 210.000", Color(0xFFF44336)),
        Product("Pasir Kucing Wangi", "Rp 25.000", Color(0xFF795548)),
        Product("Me-O Creamy Treats", "Rp 24.000", Color(0xFFFFC107)),
        Product("Kalung Kucing", "Rp 15.000", Color(0xFF2196F3))
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp, top = 8.dp),
        modifier = modifier
    ) {
        items(products) { product ->
            ProductItem(product)
        }
    }
}

data class Product(val name: String, val price: String, val color: Color)

@Composable
fun ProductItem(product: Product) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(product.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder for Image
                Icon(
                    Icons.Default.Inventory,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = product.color
                )
            }
            
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.price,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp).clickable { }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StorePreview() {
    AnabulTheme {
        StoreScreen()
    }
}
