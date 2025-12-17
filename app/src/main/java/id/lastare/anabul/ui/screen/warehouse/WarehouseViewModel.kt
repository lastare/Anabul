package id.lastare.anabul.ui.screen.warehouse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.lastare.anabul.domain.model.Product
import id.lastare.anabul.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class WarehouseViewModel(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<WarehouseUiState>(WarehouseUiState.Loading)
    val uiState: StateFlow<WarehouseUiState> = _uiState.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    init {
        loadProducts()
    }

    fun setTab(index: Int) {
        _selectedTab.value = index
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = WarehouseUiState.Loading
            getProductsUseCase()
                .catch { error ->
                    _uiState.value = WarehouseUiState.Error(error.message ?: "Unknown error")
                }
                .collect { products ->
                    _uiState.value = WarehouseUiState.Success(products)
                }
        }
    }
}

sealed class WarehouseUiState {
    data object Loading : WarehouseUiState()
    data class Success(val products: List<Product>) : WarehouseUiState()
    data class Error(val message: String) : WarehouseUiState()
}
