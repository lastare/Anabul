package id.lastare.anabul.domain.model

data class Sales(
    val id: String = "",
    val created: Long = System.currentTimeMillis(),
    val userId: String = "",
    val items: List<SalesItem> = emptyList(),
    val total: Double = 0.0,
)
