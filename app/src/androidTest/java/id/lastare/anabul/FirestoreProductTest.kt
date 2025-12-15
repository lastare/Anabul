package id.lastare.anabul

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MemoryCacheSettings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class FirestoreProductTest {

    private lateinit var firestore: FirebaseFirestore

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Pastikan FirebaseApp diinisialisasi
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }
        
        firestore = FirebaseFirestore.getInstance()
        
        // Nonaktifkan persistence (offline cache) agar kita benar-benar menguji koneksi server.
        // Ini akan mencegah 'Success' palsu jika device offline, dan memaksa network call.
        try {
            val settings = FirebaseFirestoreSettings.Builder()
                .setLocalCacheSettings(MemoryCacheSettings.newBuilder().build())
                .build()
            firestore.firestoreSettings = settings
        } catch (e: Exception) {
            // Settings mungkin sudah diset sebelumnya, abaikan jika gagal set ulang
        }
    }

    @Test
    fun testAddAndRetrieveProduct() {
        val productData = hashMapOf(
            "name" to "Whiskas Tuna",
            "cost" to 15000L,
            "price" to 20000L,
            "volume" to "85g",
            "sku" to "WSK-TUN-85"
        )

        try {
            // 1. ADD: Simpan data ke Firestore
            // Kita gunakan Tasks.await untuk menunggu operasi selesai secara synchronous (memblokir test thread)
            // Timeout 15 detik untuk mengantisipasi jaringan lambat
            val addTask = firestore.collection("products").add(productData)
            val documentReference = Tasks.await(addTask, 15, TimeUnit.SECONDS)
            
            assertNotNull("DocumentReference tidak boleh null", documentReference)

            // 2. GET: Ambil data kembali dari server
            val getTask = documentReference.get()
            val documentSnapshot = Tasks.await(getTask, 15, TimeUnit.SECONDS)
            
            if (!documentSnapshot.exists()) {
                 throw Exception("Dokumen berhasil ditulis tapi tidak ditemukan saat dibaca kembali.")
            }

            // 3. VERIFY: Cek kesesuaian data
            assertEquals("Nama tidak sesuai", "Whiskas Tuna", documentSnapshot.getString("name"))
            assertEquals("Cost tidak sesuai", 15000L, documentSnapshot.getLong("cost"))
            assertEquals("Price tidak sesuai", 20000L, documentSnapshot.getLong("price"))
            assertEquals("Volume tidak sesuai", "85g", documentSnapshot.getString("volume"))
            assertEquals("SKU tidak sesuai", "WSK-TUN-85", documentSnapshot.getString("sku"))

            // 4. CLEANUP: Hapus data test
            val deleteTask = documentReference.delete()
            Tasks.await(deleteTask, 15, TimeUnit.SECONDS)

        } catch (e: TimeoutException) {
            throw Exception("TIMEOUT: Operasi memakan waktu terlalu lama (>15s). \n" +
                    "Kemungkinan penyebab:\n" +
                    "1. Koneksi internet tidak stabil atau offline pada Emulator/Device.\n" +
                    "2. Aturan Keamanan Firestore (Security Rules) memblokir akses.\n" +
                    "3. Masalah pada layanan Google Play Services di emulator.", e)
        } catch (e: Exception) {
            // Lempar ulang exception lain (misal permission denied) agar test failure jelas
            throw e
        }
    }
}
