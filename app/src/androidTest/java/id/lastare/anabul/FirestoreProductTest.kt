package id.lastare.anabul

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@MediumTest
@RunWith(AndroidJUnit4::class)
class FirestoreProductTest {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var testProductId: String? = null

    @Before
    fun setup() {
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        
        // Ensure we are signed in anonymously or with a test user if rules require it
        // For this test, we assume public access or already authenticated state from previous tests,
        // but robust tests should handle auth. 
        if (auth.currentUser == null) {
            try {
                Tasks.await(auth.signInAnonymously(), 30, TimeUnit.SECONDS)
            } catch (e: Exception) {
                // Ignore if sign in fails, might be allowed by rules or already signed in
            }
        }
    }

    @After
    fun tearDown() {
        // Clean up test data
        testProductId?.let { id ->
            try {
                Tasks.await(firestore.collection("products").document(id).delete(), 10, TimeUnit.SECONDS)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @Test
    fun testAddAndRetrieveProduct() {
        val product = hashMapOf(
            "name" to "Test Product",
            "price" to 10000,
            "stock" to 50
        )

        // Add product
        val addTask = firestore.collection("products").add(product)
        val documentReference = Tasks.await(addTask, 30, TimeUnit.SECONDS)
        testProductId = documentReference.id
        assertNotNull("Product ID should not be null", testProductId)

        // Retrieve product
        val getTask = firestore.collection("products").document(testProductId!!).get()
        val documentSnapshot = Tasks.await(getTask, 30, TimeUnit.SECONDS)
        
        assertNotNull("Document should exist", documentSnapshot)
        assertEquals("Test Product", documentSnapshot.getString("name"))
        assertEquals(10000L, documentSnapshot.getLong("price"))
    }
}
