package id.lastare.anabul.ui.screen.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.lastare.anabul.domain.model.Product
import id.lastare.anabul.domain.usecase.AddProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddProductViewModel(
    private val addProductUseCase: AddProductUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddProductUiState>(AddProductUiState.Idle)
    val uiState: StateFlow<AddProductUiState> = _uiState.asStateFlow()

    fun addProduct(name: String, sku: String, volume: String, cost: String, price: String) {
        viewModelScope.launch {
            _uiState.value = AddProductUiState.Loading
            
            val costLong = cost.toLongOrNull() ?: 0L
            val priceLong = price.toLongOrNull() ?: 0L

            val product = Product(
                name = name,
                sku = sku,
                volume = volume,
                cost = costLong,
                price = priceLong
            )

            val result = addProductUseCase(product)
            result.onSuccess {
                _uiState.value = AddProductUiState.Success
            }.onFailure { error ->
                _uiState.value = AddProductUiState.Error(error.message ?: "Unknown error")
            }
        }
    }
    
    fun resetState() {
        _uiState.value = AddProductUiState.Idle
    }
}

sealed class AddProductUiState {
    data object Idle : AddProductUiState()
    data object Loading : AddProductUiState()
    data object Success : AddProductUiState()
    data class Error(val message: String) : AddProductUiState()
}
