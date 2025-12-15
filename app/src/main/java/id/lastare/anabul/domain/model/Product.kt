package id.lastare.anabul.domain.model

data class Product(
    val id: String = "",
    val created: Long = System.currentTimeMillis(),
    val updated: Long = System.currentTimeMillis(),
    val name: String = "",
    val sku: String = "",
    val volume: String = "",
    val cost: Long = 0,
    val price: Long = 0,
)
