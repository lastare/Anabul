package id.lastare.anabul.domain.model

data class SalesItem(
    val id: String = "",
    val productId: String = "",
    val price: Long = 0,
    val quantity: Int = 0,
    val subTotal: Double = 0.0,
)
