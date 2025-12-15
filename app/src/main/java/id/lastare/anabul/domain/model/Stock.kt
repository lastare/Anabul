package id.lastare.anabul.domain.model

data class Stock(
    val id: String = "",
    val created: Long = System.currentTimeMillis(),
    val productId: String = "",
    val inWarehouse: Boolean = true,
    val count: Int = 0,
)
