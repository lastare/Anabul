package id.lastare.anabul.domain.usecase

import id.lastare.anabul.domain.model.Product
import id.lastare.anabul.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return repository.getProducts()
    }
}
