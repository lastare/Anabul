package id.lastare.anabul

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class FirebaseAuthTest {

    private lateinit var auth: FirebaseAuth

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }
        auth = FirebaseAuth.getInstance()
        
        // Gunakan emulator jika perlu, atau pastikan environment test siap
        // auth.useEmulator("10.0.2.2", 9099) 
    }

    @Test
    fun testRegisterUser() {
        // Gunakan email unik setiap test agar tidak konflik
        val timestamp = System.currentTimeMillis()
        val email = "test_user_$timestamp@example.com"
        val password = "TestPassword123!"

        try {
            // 1. REGISTER: Buat user baru
            val registerTask = auth.createUserWithEmailAndPassword(email, password)
            val authResult = Tasks.await(registerTask, 10, TimeUnit.SECONDS)
            
            assertNotNull("Auth result tidak boleh null", authResult)
            val user = authResult.user
            assertNotNull("User tidak boleh null setelah register", user)
            assertEquals("Email user harus sesuai", email, user?.email)

            // 2. SIGN OUT: Keluar
            auth.signOut()

            // 3. LOGIN: Masuk kembali dengan akun yang baru dibuat
            val loginTask = auth.signInWithEmailAndPassword(email, password)
            val loginResult = Tasks.await(loginTask, 10, TimeUnit.SECONDS)
            
            assertNotNull("Login result tidak boleh null", loginResult)
            assertEquals("User ID harus sama setelah login", user?.uid, loginResult.user?.uid)

            // 4. CLEANUP: Hapus user agar tidak mengotori database auth
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val deleteTask = currentUser.delete()
                Tasks.await(deleteTask, 10, TimeUnit.SECONDS)
            }

        } catch (e: TimeoutException) {
             throw Exception("TIMEOUT: Operasi Authentication terlalu lama (>10s). Pastikan koneksi internet stabil.", e)
        } catch (e: Exception) {
            throw e
        }
    }
}
