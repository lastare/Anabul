package id.lastare.anabul.domain.model

import id.lastare.anabul.domain.model.enum.Stock

data class Showcase(
    val id: String = "",
    val created: Long = System.currentTimeMillis(),
    val productId: String = "",
    val productName: String = "",
    val quantity: Int = 0,
    val status: Stock = Stock.IN,
    val description: String = "",
)
