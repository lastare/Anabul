package id.lastare.anabul.domain.usecase

import id.lastare.anabul.domain.model.Product
import id.lastare.anabul.domain.repository.ProductRepository

class AddProductUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: Product): Result<Unit> {
        return repository.addProduct(product)
    }
}
