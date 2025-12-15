package id.lastare.anabul.domain.usecase

import id.lastare.anabul.domain.model.User
import id.lastare.anabul.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, pass: String): Result<User> {
        if (email.isBlank() || pass.isBlank()) {
            return Result.failure(IllegalArgumentException("Email and password cannot be empty"))
        }
        return repository.signIn(email, pass)
    }
}
