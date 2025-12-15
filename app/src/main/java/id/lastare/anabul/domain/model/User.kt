package id.lastare.anabul.domain.model

data class User(
    val id: String = "",
    val created: Long = System.currentTimeMillis(),
    val updated: Long = System.currentTimeMillis(),
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val isActive: Boolean = false,
    val isAdmin: Boolean = false,
)
