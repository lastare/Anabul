package id.lastare.anabul.ui.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.lastare.anabul.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            // Optional: Tambahkan delay buatan agar splash screen terlihat
            delay(1500)
            
            val user = authRepository.getCurrentUser().first()
            if (user != null) {
                _uiState.value = SplashUiState.NavigateToDashboard
            } else {
                _uiState.value = SplashUiState.NavigateToLogin
            }
        }
    }
}

sealed class SplashUiState {
    object Loading : SplashUiState()
    object NavigateToDashboard : SplashUiState()
    object NavigateToLogin : SplashUiState()
}
