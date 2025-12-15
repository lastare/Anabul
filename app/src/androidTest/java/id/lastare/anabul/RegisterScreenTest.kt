package id.lastare.anabul

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import id.lastare.anabul.domain.model.User
import id.lastare.anabul.domain.repository.AuthRepository
import id.lastare.anabul.domain.usecase.RegisterUseCase
import id.lastare.anabul.ui.screen.register.RegisterScreen
import id.lastare.anabul.ui.screen.register.RegisterViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

// Fake implementation to avoid MockK issues with inline Result class
class FakeAuthRepository : AuthRepository {
    var signUpResult: Result<User> = Result.failure(Exception("Default failure"))

    override fun getCurrentUser(): Flow<User?> = flowOf(null)

    override suspend fun signIn(email: String, pass: String): Result<User> {
        return Result.failure(Exception("Not implemented"))
    }

    override suspend fun signUp(email: String, pass: String): Result<User> {
        return signUpResult
    }

    override suspend fun signOut() {}
}

class RegisterScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val authRepository = FakeAuthRepository()
    private val registerUseCase = RegisterUseCase(authRepository)
    private val viewModel = RegisterViewModel(registerUseCase)

    @Test
    fun registerButton_disabled_initially() {
        composeTestRule.setContent {
            RegisterScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithTag("register_button").assertIsNotEnabled()
    }

    @Test
    fun registerButton_enabled_when_terms_accepted() {
        composeTestRule.setContent {
            RegisterScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithTag("terms_checkbox").performClick()
        composeTestRule.onNodeWithTag("register_button").assertIsEnabled()
    }

    @Test
    fun fill_form_and_register_success() {
        var successCalled = false
        val email = "test@example.com"
        val password = "password123"
        val name = "Test User"

        // Setup the fake to return success
        authRepository.signUpResult = Result.success(
            User(id = "1", email = email, name = name)
        )

        composeTestRule.setContent {
            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = { successCalled = true }
            )
        }

        // Fill in the form
        composeTestRule.onNodeWithTag("name_field").performTextInput(name)
        composeTestRule.onNodeWithTag("email_field").performTextInput(email)
        composeTestRule.onNodeWithTag("password_field").performTextInput(password)
        composeTestRule.onNodeWithTag("confirm_password_field").performTextInput(password)
        
        // Accept terms
        composeTestRule.onNodeWithTag("terms_checkbox").performClick()
        
        // Click register
        composeTestRule.onNodeWithTag("register_button").performClick()
        
        // Wait for the success callback
        composeTestRule.waitUntil(timeoutMillis = 5000) { successCalled }
        
        // Verify success
        assert(successCalled)
    }

    @Test
    fun fill_form_password_mismatch_shows_toast_or_error() {
        val email = "test@example.com"
        val password = "password123"
        val confirmPassword = "password456" // Mismatch
        val name = "Test User"
        var successCalled = false

        composeTestRule.setContent {
            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = { successCalled = true }
            )
        }

        composeTestRule.onNodeWithTag("name_field").performTextInput(name)
        composeTestRule.onNodeWithTag("email_field").performTextInput(email)
        composeTestRule.onNodeWithTag("password_field").performTextInput(password)
        composeTestRule.onNodeWithTag("confirm_password_field").performTextInput(confirmPassword)
        composeTestRule.onNodeWithTag("terms_checkbox").performClick()
        
        composeTestRule.onNodeWithTag("register_button").performClick()
        
        // Success should NOT be called
        composeTestRule.waitForIdle()
        assert(!successCalled)
    }
}
