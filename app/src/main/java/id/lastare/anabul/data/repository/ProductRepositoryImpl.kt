package id.lastare.anabul.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import id.lastare.anabul.domain.model.Product
import id.lastare.anabul.domain.repository.ProductRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ProductRepositoryImpl(
    private val firestore: FirebaseFirestore
) : ProductRepository {

    override suspend fun addProduct(product: Product): Result<Unit> {
        return try {
            val docRef = firestore.collection("products").document()
            val productWithId = product.copy(id = docRef.id)
            docRef.set(productWithId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getProducts(): Flow<List<Product>> = callbackFlow {
        val listener = firestore.collection("products")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val products = snapshot?.toObjects(Product::class.java) ?: emptyList()
                trySend(products)
            }
        
        awaitClose { listener.remove() }
    }
}
