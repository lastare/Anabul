package id.lastare.anabul.domain.repository

import id.lastare.anabul.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUser(): Flow<User?>
    suspend fun signIn(email: String, pass: String): Result<User>
    suspend fun signUp(email: String, pass: String): Result<User>
    suspend fun signOut()
}
