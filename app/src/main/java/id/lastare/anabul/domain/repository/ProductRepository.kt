package id.lastare.anabul.domain.repository

import id.lastare.anabul.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun addProduct(product: Product): Result<Unit>
    fun getProducts(): Flow<List<Product>>
}
