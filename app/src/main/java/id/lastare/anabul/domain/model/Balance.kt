package id.lastare.anabul.domain.model

import id.lastare.anabul.domain.model.enum.Shift

data class Balance(
    val id: String = "",
    val created: Long = System.currentTimeMillis(),
    val userId: String = "",
    val shift: Shift = Shift.PAGI,
    val amount: Double = 0.0,
    val total: Double = 0.0,
    val open: Boolean = false,
)
