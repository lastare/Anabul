package id.lastare.anabul.domain.usecase

import id.lastare.anabul.domain.model.User
import id.lastare.anabul.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RegisterUseCaseTest {

    private val authRepository: AuthRepository = mockk()
    private val registerUseCase = RegisterUseCase(authRepository)

    @Test
    fun `invoke should return success when repository returns success`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val name = "Test User"
        val user = User(id = "123", email = email, name = name)
        
        coEvery { authRepository.signUp(email, password) } returns Result.success(user)

        // When
        val result = registerUseCase(email, password, name)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(user, result.getOrNull())
    }

    @Test
    fun `invoke should return failure when inputs are empty`() = runTest {
        // Given
        val email = ""
        val password = ""
        val name = "Test User"

        // When
        val result = registerUseCase(email, password, name)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Email and password cannot be empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke should return failure when repository returns failure`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val name = "Test User"
        val errorMessage = "Registration failed"
        
        coEvery { authRepository.signUp(email, password) } returns Result.failure(Exception(errorMessage))

        // When
        val result = registerUseCase(email, password, name)

        // Then
        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }
}
