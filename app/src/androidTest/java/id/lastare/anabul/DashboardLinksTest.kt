package id.lastare.anabul

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@LargeTest
@RunWith(AndroidJUnit4::class)
class DashboardLinksTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var auth: FirebaseAuth
    private val testUserEmail = "dashboard_test@example.com"
    private val testUserPassword = "TestPassword123!"

    @Before
    fun setup() {
        auth = FirebaseAuth.getInstance()
        
        // Ensure we are signed out first to start clean or sign in if needed
        if (auth.currentUser != null) {
            auth.signOut()
        }
        
        // Create a test user directly via Firebase API to ensure login works
        try {
             Tasks.await(auth.createUserWithEmailAndPassword(testUserEmail, testUserPassword))
        } catch (e: Exception) {
            // User might already exist, try signing in to verify credentials
             try {
                Tasks.await(auth.signInWithEmailAndPassword(testUserEmail, testUserPassword))
            } catch (e2: Exception) {
                // Ignore
            }
        }
        
        // Now navigate to Dashboard by logging in through UI
        loginFlow()
    }

    @After
    fun tearDown() {
         // Clean up user
        val user = auth.currentUser
        if (user != null) {
             try {
                Tasks.await(user.delete(), 10, TimeUnit.SECONDS)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loginFlow() {
        composeTestRule.waitForIdle()
        
        // If we are on Splash, wait
         composeTestRule.waitUntil(timeoutMillis = 20000) {
            try {
                composeTestRule.onAllNodesWithText("Masuk Akun").fetchSemanticsNodes().isNotEmpty()
            } catch (e: Exception) {
                false
            }
        }

        // Perform Login
        composeTestRule.onNodeWithTag("login_email").performTextInput(testUserEmail)
        composeTestRule.onNodeWithTag("login_password").performTextInput(testUserPassword)
        composeTestRule.onNodeWithTag("login_button").performClick()

        // Wait for Dashboard
        waitForText("Kerja Cepat", 30000)
    }

    @Test
    fun testAllDashboardLinks() {
        // 1. Test Toko Link
        // Gunakan Content Description karena Text "Toko" tidak bisa diklik (klik ada pada icon/surface)
        composeTestRule.onNodeWithContentDescription("Toko").performClick()
        waitForText("Penjualan Toko")
        composeTestRule.onNodeWithText("Penjualan Toko").assertIsDisplayed()
        
        // Go back
        composeTestRule.onNodeWithContentDescription("Kembali").performClick()
        waitForText("Kerja Cepat")
        composeTestRule.onNodeWithText("Kerja Cepat").assertIsDisplayed()

        // 2. Test Saldo Link
        composeTestRule.onNodeWithContentDescription("Saldo").performClick()
        waitForText("Buka Toko")
        composeTestRule.onNodeWithText("Buka Toko").assertIsDisplayed()
        
        // Go back
        composeTestRule.onNodeWithContentDescription("Kembali").performClick()
        waitForText("Kerja Cepat")
        composeTestRule.onNodeWithText("Kerja Cepat").assertIsDisplayed()

        // 3. Test Etalase Link
        composeTestRule.onNodeWithContentDescription("Etalase").performClick()
        waitForText("Etalase Produk")
        composeTestRule.onNodeWithText("Etalase Produk").assertIsDisplayed()
        
        // Go back
        composeTestRule.onNodeWithContentDescription("Kembali").performClick()
        waitForText("Kerja Cepat")
        composeTestRule.onNodeWithText("Kerja Cepat").assertIsDisplayed()

        // 4. Test Gudang Link
        composeTestRule.onNodeWithContentDescription("Gudang").performClick()
        // Wait for unique content in Warehouse to avoid matching the button "Gudang" on Dashboard
        waitForText("Total Stok") 
        composeTestRule.onNodeWithText("Total Stok").assertIsDisplayed() 
        
        // Go back
        composeTestRule.onNodeWithContentDescription("Kembali").performClick()
        waitForText("Kerja Cepat")
        composeTestRule.onNodeWithText("Kerja Cepat").assertIsDisplayed()

        // 5. Test Laporan Link
        composeTestRule.onNodeWithContentDescription("Laporan").performClick()
        waitForText("Laporan Terpadu")
        composeTestRule.onNodeWithText("Laporan Terpadu").assertIsDisplayed()
        
        // Go back
        composeTestRule.onNodeWithContentDescription("Kembali").performClick()
        waitForText("Kerja Cepat")
        composeTestRule.onNodeWithText("Kerja Cepat").assertIsDisplayed()
    }

    private fun waitForText(text: String, timeoutMillis: Long = 5000) {
        composeTestRule.waitUntil(timeoutMillis = timeoutMillis) {
            try {
                composeTestRule.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
            } catch (e: Exception) {
                false
            }
        }
    }
}
