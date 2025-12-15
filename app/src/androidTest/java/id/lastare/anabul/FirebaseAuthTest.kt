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
class FirebaseAuthTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var auth: FirebaseAuth
    private var testUserEmail = ""
    private val testUserPassword = "TestPassword123!"

    @Before
    fun setup() {
        auth = FirebaseAuth.getInstance()
        val timestamp = System.currentTimeMillis()
        testUserEmail = "test_user_$timestamp@example.com"
    }

    @After
    fun tearDown() {
        // Clean up: Delete created user
        val user = auth.currentUser
        if (user != null) {
            try {
                // Await deletion to ensure cleanup happens before next test or process death
                Tasks.await(user.delete(), 10, TimeUnit.SECONDS)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        // Ensure we are signed out locally
        if (auth.currentUser != null) {
            auth.signOut()
        }
    }

    @Test
    fun testRegisterAndLoginFlow() {
        // --- 0. Handle Initial State ---
        
        composeTestRule.waitForIdle()
        
        // Check for a Dashboard-specific element to confirm we are on Dashboard
        val isOnDashboard = try {
            composeTestRule.onNodeWithText("Kerja Cepat").assertExists()
            true
        } catch (e: AssertionError) {
            false
        }

        if (isOnDashboard) {
            // Logout first
            composeTestRule.onNodeWithText("H").performClick()
            composeTestRule.onNodeWithText("Keluar").performClick()
            composeTestRule.waitForIdle()
        }

        // Now we should be at Login Screen
        composeTestRule.onNodeWithText("Masuk Akun").assertIsDisplayed()
        
        // --- 1. Navigate to Register Screen ---
        composeTestRule.onNodeWithTag("register_link").performClick()
        
        // --- 2. Register New User ---
        // The text is uppercased in the UI: "MEMBUAT PENGGUNA BARU"
        composeTestRule.onNodeWithText("MEMBUAT PENGGUNA BARU").assertIsDisplayed()

        // Fill Registration Form
        composeTestRule.onNodeWithTag("name_field").performTextInput("Test User")
        composeTestRule.onNodeWithTag("email_field").performTextInput(testUserEmail)
        composeTestRule.onNodeWithTag("password_field").performTextInput(testUserPassword)
        composeTestRule.onNodeWithTag("confirm_password_field").performTextInput(testUserPassword)
        
        // Check Terms
        composeTestRule.onNodeWithTag("terms_checkbox").performClick()

        // Click Register Button
        composeTestRule.onNodeWithTag("register_button").performClick()

        // --- 3. Verify Navigation to Dashboard (Success) ---
        // Wait for dashboard specific element "Kerja Cepat". 
        composeTestRule.waitUntil(timeoutMillis = 30000) {
            try {
                composeTestRule.onNodeWithText("Kerja Cepat").assertExists()
                true
            } catch (e: AssertionError) {
                false
            }
        }
        
        composeTestRule.onNodeWithText("Point Of Sales").assertIsDisplayed()

        // --- 4. Logout ---
        // Open Profile Menu
        composeTestRule.onNodeWithText("H").performClick()
        
        // Click Logout
        composeTestRule.onNodeWithText("Keluar").performClick()

        // Verify we are back to Login Screen
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            try {
                composeTestRule.onNodeWithText("Masuk Akun").assertExists()
                true
            } catch (e: AssertionError) {
                false
            }
        }

        // --- 5. Login with New User ---
        composeTestRule.onNodeWithTag("login_email").performTextInput(testUserEmail)
        composeTestRule.onNodeWithTag("login_password").performTextInput(testUserPassword)
        
        // Removed performImeAction() as field uses default action. 
        // Try closing keyboard via back press if needed, but usually clicking button works.
        // composeTestRule.activity.onBackPressed() // Alternative if button is covered
        
        composeTestRule.onNodeWithTag("login_button").performClick()

        // --- 6. Verify Dashboard again ---
        // Wait for Dashboard unique element
        composeTestRule.waitUntil(timeoutMillis = 30000) {
            try {
                composeTestRule.onNodeWithText("Kerja Cepat").assertExists()
                true
            } catch (e: AssertionError) {
                false
            }
        }
        composeTestRule.onNodeWithText("Point Of Sales").assertIsDisplayed()
    }
}
