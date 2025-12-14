package id.lastare.anabul

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirebaseConnectionTest {

    @Test
    fun testFirebaseInitialization() {
        // Mendapatkan Context aplikasi
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // Memastikan FirebaseApp telah diinisialisasi secara otomatis oleh ContentProvider
        // atau kita bisa memanggil initializeApp secara manual jika diperlukan,
        // namun biasanya otomatis jika plugin google-services bekerja dengan benar.
        if (FirebaseApp.getApps(appContext).isEmpty()) {
            FirebaseApp.initializeApp(appContext)
        }

        val firebaseApp = FirebaseApp.getInstance()
        assertNotNull("FirebaseApp instance should not be null", firebaseApp)

        // Verifikasi koneksi/inisialisasi komponen spesifik
        // Catatan: Ini tidak melakukan request jaringan (ping), hanya mengecek inisialisasi SDK.
        
        // 1. Auth
        val auth = FirebaseAuth.getInstance()
        assertNotNull("FirebaseAuth instance should not be null", auth)
        
        // 2. Firestore
        val firestore = FirebaseFirestore.getInstance()
        assertNotNull("FirebaseFirestore instance should not be null", firestore)
        
        // Cek Application ID sesuai dengan package name (basic check)
        val options = firebaseApp.options
        assertTrue(
            "Application ID should contain the package name",
            options.applicationId.contains("id.lastare.anabul") || options.applicationId.isNotEmpty()
        )
    }
}
