package id.lastare.anabul

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class FirebaseConnectionTest {

    @Test
    fun testFirebaseAppInitialization() {
        val app = FirebaseApp.getInstance()
        assertNotNull("Firebase App should be initialized", app)
    }

    @Test
    fun testAuthInitialization() {
        val auth = FirebaseAuth.getInstance()
        assertNotNull("FirebaseAuth should be initialized", auth)
    }

    @Test
    fun testFirestoreInitialization() {
        val firestore = FirebaseFirestore.getInstance()
        assertNotNull("FirebaseFirestore should be initialized", firestore)
    }
}
